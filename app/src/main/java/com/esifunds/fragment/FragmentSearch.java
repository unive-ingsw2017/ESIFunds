package com.esifunds.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esifunds.R;

public class FragmentSearch extends Fragment
{
    private FragmentOpportunities fragmentOpportunities;

    public FragmentSearch()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View viewRoot = inflater.inflate(R.layout.fragment_search, container, false);

        TabLayout tabLayout = viewRoot.findViewById(R.id.tabLayoutFragmentSearch);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override public void onTabSelected(TabLayout.Tab tab)
            {
                loadFragmentForTab(tab.getPosition());
            }

            @Override public void onTabUnselected(TabLayout.Tab tab)
            {

            }

            @Override public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });

        loadFragmentForTab(tabLayout.getSelectedTabPosition());

        return viewRoot;
    }

    private void loadFragmentForTab(int position)
    {
        switch(position)
        {
            case 0:
            {
                fragmentOpportunities = new FragmentOpportunities();
                Bundle args = new Bundle();
                args.putBoolean("IS_SEARCH", true);
                fragmentOpportunities.setArguments(args);

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentPlaceholderFragmentSearch, fragmentOpportunities);
                fragmentTransaction.commit();

                break;
            }

            case 1:
            {
                Log.i("TAB", "1");
                break;
            }

            case 2:
            {
                Log.i("TAB", "2");
                break;
            }
        }
    }

    public FragmentOpportunities getFragmentOpportunities()
    {
        return fragmentOpportunities;
    }
}
