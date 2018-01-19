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
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentOpportunities extends Fragment
{
    RecyclerView recyclerViewOpportunities;

    public FragmentOpportunities()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View viewRoot = inflater.inflate(R.layout.fragment_opportunities, container, false);

        recyclerViewOpportunities = viewRoot.findViewById(R.id.recyclerViewOpportunities);

        recyclerViewOpportunities.setLayoutManager(new LinearLayoutManager(getContext()));
        ItemAdapter<Opportunity> itemAdapter = new ItemAdapter<>();

        FastAdapter<Opportunity> fastAdapter = FastAdapter.with(itemAdapter);
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

        recyclerViewOpportunities.setAdapter(fastAdapter);

        // @TODO: Parametrize this with Database
        List<Opportunity> listOpportunities = new ArrayList<Opportunity>()
        {{
            add(new Opportunity("Name 1", "Description 1"));
            add(new Opportunity("Name 2", "Description 2"));
            add(new Opportunity("Name 3", "Description 3"));
            add(new Opportunity("Name 4", "Description 4"));
            add(new Opportunity("Name 5", "Description 5"));
            add(new Opportunity("Name 6", "Description 6"));
            add(new Opportunity("Name 7", "Description 7"));
            add(new Opportunity("Name 8", "Description 8"));
            add(new Opportunity("Name 9", "Description 9"));
            add(new Opportunity("Name 10", "Description 10"));
        }};

        itemAdapter.add(listOpportunities);

        return viewRoot;
    }
}
