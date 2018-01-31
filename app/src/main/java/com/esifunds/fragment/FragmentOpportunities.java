package com.esifunds.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esifunds.R;
import com.esifunds.model.Opportunity;
import com.esifunds.model.UserFavourites;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.mikepenz.fastadapter_extensions.items.ProgressItem;
import com.mikepenz.fastadapter_extensions.scroll.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentOpportunities extends Fragment
{
    RecyclerView recyclerViewOpportunities;
    private FirebaseDatabase mDatabase;
    private ItemAdapter<Opportunity> itemAdapter = new ItemAdapter<>();
    final ItemAdapter<ProgressItem> footerAdapter = new ItemAdapter<>();
    private String lastValue = "0";
    private FragmentSearch fragmentSearch;
    private FastAdapter<Opportunity> fastAdapter;
    private boolean initialized = false;
    private String oggetto = "";
    private String tema = "";
    private String beneficiario = "";
    private String regione = "";
    private boolean isFavourites = false;
    private boolean isSearch = false;

    public FragmentOpportunities()
    {
    }

    public void setFragmentSearch(FragmentSearch fragmentSearch)
    {
        this.fragmentSearch = fragmentSearch;
    }

    public void searchWithString(final String oggetto, final String tema, final String beneficiario, final String regione)
    {
        itemAdapter.clear();

        this.oggetto = oggetto;
        this.tema = tema;
        this.beneficiario = beneficiario;
        this.regione = regione;
        filterFor(oggetto, tema, beneficiario, regione, isFavourites, isSearch, false);
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        final Bundle args = getArguments();

        if(args != null)
        {
            oggetto = args.getString("SEARCH_OGGETTO", oggetto);
            tema = args.getString("SEARCH_TEMA", tema);
            beneficiario = args.getString("SEARCH_BENEFICIARIO", beneficiario);
            regione = args.getString("SEARCH_REGIONE", regione);
            isFavourites = args.getBoolean("IS_FAVOURITES", isFavourites);
            isSearch = args.getBoolean("IS_SEARCH", isSearch);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View viewRoot = inflater.inflate(R.layout.fragment_opportunities, container, false);

        mDatabase = FirebaseDatabase.getInstance();

        recyclerViewOpportunities = viewRoot.findViewById(R.id.recyclerViewOpportunities);

        recyclerViewOpportunities.setLayoutManager(new LinearLayoutManager(getContext()));

        fastAdapter = FastAdapter.with(footerAdapter);
        fastAdapter.addAdapter(0, itemAdapter);
        fastAdapter.withSelectable(true);
        fastAdapter.registerTypeInstance(new Opportunity());
        fastAdapter.withOnClickListener(new OnClickListener<Opportunity>()
        {
            @Override public boolean onClick(View view, IAdapter<Opportunity> adapter, Opportunity item, int position)
            {
                Fragment fragmentOpportunity = new FragmentOpportunitySingleton();

                Bundle args = new Bundle();
                args.putParcelable("opportunity", item);
                fragmentOpportunity.setArguments(args);

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.fragmentPlaceholderOpportunitiesActivity, fragmentOpportunity, "OPPORTUNITY_VIEW");
                fragmentTransaction.addToBackStack("OPPORTUNITY_VIEW");
                fragmentTransaction.commit();

                return true;
            }
        });


        recyclerViewOpportunities.setAdapter(fastAdapter);

        filterFor(oggetto, tema, beneficiario, regione, isFavourites, isSearch, false);

        if(!isFavourites && !isSearch)
        {
            recyclerViewOpportunities.addOnScrollListener(new EndlessRecyclerOnScrollListener(footerAdapter)
            {
                @Override
                public void onLoadMore(int currentPage)
                {
                    filterFor(oggetto, tema, beneficiario, regione, isFavourites, isSearch, true);
                }
            });
        }

        return viewRoot;
    }

    private void filterFor(final String oggetto,
                           final String tema,
                           final String beneficiario,
                           final String regione,
                           final boolean isFavourites,
                           final boolean isSearch,
                           final boolean onScroll)
    {
        if(isSearch && oggetto.isEmpty() && tema.isEmpty() && beneficiario.isEmpty() && regione.isEmpty())
        {
            if(fragmentSearch != null  && isAdded())
            {
                fragmentSearch.setSearchResultCount(0);
            }

            itemAdapter.clear();
            return;
        }

        Query query =  mDatabase.getReference("opportunities");

        if(!isSearch)
        {
            if(onScroll)
            {
                query = query.startAt(null, lastValue).limitToFirst(25 + 1);
            }
            else if(!isFavourites)
            {
                query = query.limitToFirst(25);
            }
        }

        final Query fQuery = query;
        if(isSearch || isFavourites)
        {
            itemAdapter.clear();
            footerAdapter.clear();
            footerAdapter.add(new ProgressItem().withEnabled(true));
        }

        final LongSparseArray<Boolean> mFavourites = new LongSparseArray<>();
        if(isFavourites)
        {
            if(UserFavourites.getAll() != null)
            {
                UserFavourites.getAll().addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.exists())
                        {
                            Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();

                            mFavourites.clear();
                            for(DataSnapshot aSnapshotIterable : snapshotIterable)
                            {
                                long opID = Long.parseLong(aSnapshotIterable.getKey());
                                mFavourites.put(opID, true);
                            }

                            if(mFavourites.size() == 0)
                            {
                                clearFavourites();
                            }
                            else
                            {
                                queryDatabase(oggetto, tema, beneficiario, regione, true, isSearch, onScroll, fQuery, mFavourites);
                            }
                        }
                        else
                        {
                            clearFavourites();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {
                        clearFavourites();
                    }
                });
            }
            else
            {
                footerAdapter.clear();
            }
        }
        else
        {
            queryDatabase(oggetto, tema, beneficiario, regione, false, isSearch, onScroll, query, mFavourites);
        }
    }

    private void clearFavourites()
    {
        if(fragmentSearch != null  && isAdded())
        {
            fragmentSearch.setFavouritesCount(0);
        }

        itemAdapter.clear();
        footerAdapter.clear();
    }

    private void queryDatabase(final String lOggetto,
                               final String lTema,
                               final String lBeneficiario,
                               final String lRegione,
                               final boolean isFavourites,
                               final boolean isSearch,
                               final boolean onScroll,
                               Query query,
                               final LongSparseArray<Boolean> favourites)
    {
        query.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
                List<Opportunity> listOpportunities = new ArrayList<Opportunity>();
                String lastKey = "";

                boolean bFirstSkipped = false;
                for(DataSnapshot aSnapshotIterable : snapshotIterable)
                {
                    if(onScroll && !bFirstSkipped)
                    {
                        bFirstSkipped = true;
                        continue;
                    }

                    if(isFavourites && favourites.get(Long.parseLong(aSnapshotIterable.getKey())) == null)
                    {
                        continue;
                    }

                    Opportunity opportunity = aSnapshotIterable.getValue(Opportunity.class);
                    if(opportunity == null)
                    {
                        continue;
                    }

                    boolean isValid = true;
                    if(isSearch || isFavourites)
                    {
                        if(!lOggetto.isEmpty())
                        {
                            isValid = opportunity.getOGGETTO().toLowerCase().contains(lOggetto.toLowerCase());
                        }

                        if(!lTema.isEmpty())
                        {
                            isValid = isValid && opportunity.getTEMA_SINTETICO().toLowerCase().contains(lTema.toLowerCase());
                        }

                        if(!lBeneficiario.isEmpty())
                        {
                            isValid = isValid && opportunity.getTIPOLOGIA_BENEFICIARI().toLowerCase().contains(lBeneficiario.toLowerCase());
                        }

                        if(!lRegione.isEmpty())
                        {
                            isValid = isValid && opportunity.getLUOGO().toLowerCase().contains(lRegione.toLowerCase());
                        }
                    }

                    if(isValid)
                    {
                        opportunity.setContext(getContext());
                        listOpportunities.add(opportunity);
                    }

                    lastKey = aSnapshotIterable.getKey();
                }

                lastValue = lastKey;
                footerAdapter.clear();

                if(isSearch || isFavourites)
                {
                    itemAdapter.clear();
                }

                if(oggetto.equals(lOggetto) && tema.equals(lTema) && beneficiario.equals(lBeneficiario) && regione.equals(lRegione))
                {
                    itemAdapter.add(listOpportunities);

                    if(fragmentSearch != null && isAdded())
                    {
                        if(isSearch)
                        {
                            fragmentSearch.setSearchResultCount(listOpportunities.size());
                        }
                        else if(isFavourites)
                        {
                            fragmentSearch.setFavouritesCount(listOpportunities.size());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    public boolean isFavourites()
    {
        return isFavourites;
    }

    public boolean isSearch()
    {
        return isSearch;
    }
}
