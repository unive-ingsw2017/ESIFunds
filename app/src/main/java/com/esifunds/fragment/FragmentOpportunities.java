package com.esifunds.fragment;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esifunds.R;
import com.esifunds.model.Opportunity;
import com.esifunds.model.UserFavourites;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
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

    public FragmentOpportunities()
    {
    }

    public void searchWithString(final String toSearch)
    {
        itemAdapter.clear();
        footerAdapter.clear();
        footerAdapter.add(new ProgressItem().withEnabled(true));
        mDatabase.getReference("opportunities").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();

                List<Opportunity> listOpportunities = new ArrayList<Opportunity>();
                for(DataSnapshot aSnapshotIterable : snapshotIterable)
                {
                    Opportunity opportunity = aSnapshotIterable.getValue(Opportunity.class);
                    if(opportunity == null)
                    {
                        continue;
                    }

                    if(opportunity.getOGGETTO().contains(toSearch))
                    {
                        listOpportunities.add(aSnapshotIterable.getValue(Opportunity.class));
                    }
                }

                footerAdapter.clear();
                itemAdapter.add(listOpportunities);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View viewRoot = inflater.inflate(R.layout.fragment_opportunities, container, false);

        mDatabase = FirebaseDatabase.getInstance();

        Bundle args = getArguments();
        boolean isSearch = false;
        boolean loadFavourites = false;
        String toSearch = "";
        if(args != null)
        {
            isSearch = args.getBoolean("IS_SEARCH", false);
            loadFavourites = args.getBoolean("LOAD_FAVOURITES", false);
            toSearch = args.getString("TO_SEARCH", "");
        }

        recyclerViewOpportunities = viewRoot.findViewById(R.id.recyclerViewOpportunities);

        recyclerViewOpportunities.setLayoutManager(new LinearLayoutManager(getContext()));

        FastAdapter<Opportunity> fastAdapter = FastAdapter.with(footerAdapter);
        fastAdapter.addAdapter(0, itemAdapter);
        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener(new OnClickListener<Opportunity>()
        {
            @Override public boolean onClick(View view, IAdapter<Opportunity> adapter, Opportunity item, int position)
            {
                Fragment fragmentOpportunity = new FragmentOpportunity();

                Bundle args = new Bundle();
                args.putParcelable("opportunity", item);
                fragmentOpportunity.setArguments(args);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.fragmentPlaceholderOpportunitiesActivity, fragmentOpportunity);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                return true;
            }
        });

        if(loadFavourites && UserFavourites.getAll() != null)
        {
            footerAdapter.clear();
            footerAdapter.add(new ProgressItem().withEnabled(true));
            UserFavourites.getAll().addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    if(dataSnapshot.exists())
                    {
                        Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();

                        for(DataSnapshot aSnapshotIterable : snapshotIterable)
                        {
                            long opID = Long.parseLong(aSnapshotIterable.getKey());
                            mDatabase.getReference("opportunities/" + opID).addListenerForSingleValueEvent(new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot)
                                {
                                    itemAdapter.add(dataSnapshot.getValue(Opportunity.class));
                                    footerAdapter.clear();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError)
                                {

                                }
                            });

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }
            });
        }
        else if(isSearch)
        {
            if(!toSearch.isEmpty())
            {
                searchWithString(toSearch);
            }
        }
        else
        {
            recyclerViewOpportunities.addOnScrollListener(new EndlessRecyclerOnScrollListener(footerAdapter)
            {
                @Override
                public void onLoadMore(int currentPage)
                {
                    footerAdapter.clear();
                    footerAdapter.add(new ProgressItem().withEnabled(true));
                    // Load your items here and add it to FastAdapter

                    mDatabase.getReference("opportunities").limitToFirst(currentPage + 25).addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();

                            List<Opportunity> listOpportunities = new ArrayList<>();
                            for(DataSnapshot aSnapshotIterable : snapshotIterable)
                            {
                                listOpportunities.add(aSnapshotIterable.getValue(Opportunity.class));
                            }

                            itemAdapter.add(listOpportunities);
                        }

                        @Override
                        public void onCancelled(DatabaseError error)
                        {
                        }
                    });
                }
            });

            mDatabase.getReference("opportunities").limitToFirst(25).addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();

                    List<Opportunity> listOpportunities = new ArrayList<>();
                    for(DataSnapshot aSnapshotIterable : snapshotIterable)
                    {
                        listOpportunities.add(aSnapshotIterable.getValue(Opportunity.class));
                    }

                    itemAdapter.add(listOpportunities);
                }

                @Override
                public void onCancelled(DatabaseError error)
                {
                }
            });
        }

        recyclerViewOpportunities.setAdapter(fastAdapter);

        return viewRoot;
    }
}
