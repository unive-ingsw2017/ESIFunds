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
    private FragmentOpportunities searchFragment;

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

        Bundle myArgs = getArguments();
        if(myArgs != null && myArgs.getInt("POSITION", -1) != -1)
        {
            int position = myArgs.getInt("POSITION");
            TabLayout.Tab tab = tabLayout.getTabAt(position);
            tab.select();
            loadFragmentForTab(myArgs.getInt("POSITION"));
        }
        else
        {
            loadFragmentForTab(tabLayout.getSelectedTabPosition());
        }

        return viewRoot;
    }

    private void loadFragmentForTab(int position)
    {
        switch(position)
        {
            case 0:
            {
                searchFragment = new FragmentOpportunities();
                Bundle args = new Bundle();
                args.putBoolean("IS_SEARCH", true);

                Bundle myArgs = getArguments();
                if(myArgs != null)
                {
                    args.putString("TO_SEARCH", myArgs.getString("TO_SEARCH", ""));
                }

                searchFragment.setArguments(args);

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentPlaceholderFragmentSearch, searchFragment);
                fragmentTransaction.commit();

                break;
            }

            case 1:
            {
                FragmentOpportunities fragmentFavourites = new FragmentOpportunities();
                Bundle args = new Bundle();
                args.putBoolean("LOAD_FAVOURITES", true);
                fragmentFavourites.setArguments(args);

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentPlaceholderFragmentSearch, fragmentFavourites);
                fragmentTransaction.commit();
                break;
            }

            case 2:
            {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentPlaceholderFragmentSearch, new FragmentMap());
                fragmentTransaction.commit();

                break;
            }
        }
    }

    public FragmentOpportunities getSearchFragment()
    {
        return searchFragment;
    }
}
