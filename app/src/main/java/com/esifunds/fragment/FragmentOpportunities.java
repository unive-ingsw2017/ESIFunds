package com.esifunds.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esifunds.R;
import com.esifunds.model.Opportunity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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

    public FragmentOpportunities()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View viewRoot = inflater.inflate(R.layout.fragment_opportunities, container, false);

        mDatabase = FirebaseDatabase.getInstance();

        recyclerViewOpportunities = viewRoot.findViewById(R.id.recyclerViewOpportunities);

        recyclerViewOpportunities.setLayoutManager(new LinearLayoutManager(getContext()));
        final ItemAdapter<Opportunity> itemAdapter = new ItemAdapter<>();
        final ItemAdapter<ProgressItem> footerAdapter = new ItemAdapter<>();

        FastAdapter<Opportunity> fastAdapter = FastAdapter.with(footerAdapter).with(itemAdapter);
        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener(new OnClickListener<Opportunity>()
        {
            @Override public boolean onClick(View view, IAdapter<Opportunity> adapter, Opportunity item, int position)
            {
                /* The "item" is the clicked Item */
                // @TODO: Implement click listener for clicked item

                return true;
            }
        });

        recyclerViewOpportunities.addOnScrollListener(new EndlessRecyclerOnScrollListener(footerAdapter) {
            @Override
            public void onLoadMore(int currentPage) {
                footerAdapter.clear();
                footerAdapter.add(new ProgressItem().withEnabled(false));
                // Load your items here and add it to FastAdapter

                mDatabase.getReference("opportunities").limitToFirst(currentPage + 25).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();

                        List<Opportunity> listOpportunities = new ArrayList<Opportunity>();
                        for (DataSnapshot aSnapshotIterable : snapshotIterable) {
                            listOpportunities.add(aSnapshotIterable.getValue(Opportunity.class));
                        }

                        itemAdapter.add(listOpportunities);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
            }
        });

        recyclerViewOpportunities.setAdapter(fastAdapter);

        mDatabase.getReference("opportunities").limitToFirst(25).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();

                List<Opportunity> listOpportunities = new ArrayList<Opportunity>();
                for (DataSnapshot aSnapshotIterable : snapshotIterable) {
                    listOpportunities.add(aSnapshotIterable.getValue(Opportunity.class));
                }

                itemAdapter.add(listOpportunities);
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });

        return viewRoot;
    }
}
