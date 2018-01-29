package com.esifunds.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.esifunds.R;
import com.esifunds.activity.OpportunitiesActivity;

public class FragmentSearch extends Fragment
{
    private FragmentOpportunities searchFragment;
    private TabLayout tabLayout;

    public FragmentSearch()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View viewRoot = inflater.inflate(R.layout.fragment_search, container, false);

        tabLayout = viewRoot.findViewById(R.id.tabLayoutFragmentSearch);

        final RelativeLayout relativeLayoutAdvancedSearch = viewRoot.findViewById(R.id.relativeLayoutAdvancedSearch);
        final ImageView imageViewArrowAdvancedSearch = viewRoot.findViewById(R.id.imageViewArrowAdvancedSearch);
        Button buttonToggleAdvancedSearch = viewRoot.findViewById(R.id.buttonToggleAdvancedSearch);
        buttonToggleAdvancedSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(relativeLayoutAdvancedSearch.getVisibility() == View.VISIBLE)
                {
                    relativeLayoutAdvancedSearch.setVisibility(View.GONE);
                    imageViewArrowAdvancedSearch.setImageResource(android.R.drawable.arrow_down_float);
                    ((OpportunitiesActivity)getActivity()).unRegisterAdvancedSearchInputs();
                }
                else
                {
                    relativeLayoutAdvancedSearch.setVisibility(View.VISIBLE);
                    imageViewArrowAdvancedSearch.setImageResource(android.R.drawable.arrow_up_float);
                    ((OpportunitiesActivity)getActivity()).registerAdvancedSearchInputs();
                }
            }
        });

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
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        tab.select();

        switch(position)
        {
            case 0:
            {
                searchFragment = new FragmentOpportunities();
                searchFragment.setFragmentSearch(this);
                Bundle args = new Bundle();
                args.putBoolean("IS_SEARCH", true);

                Bundle myArgs = getArguments();
                if(myArgs != null)
                {
                    args.putString("SEARCH_OGGETTO", myArgs.getString("SEARCH_OGGETTO", ""));
                    args.putString("SEARCH_TEMA", myArgs.getString("SEARCH_TEMA", ""));
                    args.putString("SEARCH_BENEFICIARIO", myArgs.getString("SEARCH_BENEFICIARIO", ""));
                    args.putString("SEARCH_REGIONE", myArgs.getString("SEARCH_REGIONE", ""));
                }

                searchFragment.setArguments(args);

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentPlaceholderFragmentSearch, searchFragment);
                fragmentTransaction.commit();

                break;
            }

            case 1:
            {
                searchFragment = new FragmentOpportunities();
                searchFragment.setFragmentSearch(this);

                Bundle args = new Bundle();
                args.putBoolean("IS_FAVOURITES", true);
                searchFragment.setArguments(args);

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentPlaceholderFragmentSearch, searchFragment);
                fragmentTransaction.commit();
                break;
            }

            case 2:
            {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                FragmentMap map = new FragmentMap();
                map.setFragmentSearch(this);
                fragmentTransaction.replace(R.id.fragmentPlaceholderFragmentSearch, map);
                fragmentTransaction.commit();

                break;
            }
        }
    }

    public void setSearchResultCount(int count)
    {
        if(!isAdded())
        {
            return;
        }

        TabLayout.Tab tab = tabLayout.getTabAt(0);
        if(count == 0)
        {
            tab.setText(R.string.string_search);
        }
        else
        {
            tab.setText(getResources().getString(R.string.string_search) + " ( " + count + " )");
        }
    }

    public void setFavouritesCount(int count)
    {
        if(!isAdded())
        {
            return;
        }

        TabLayout.Tab tab = tabLayout.getTabAt(1);
        if(count == 0)
        {
            tab.setText(R.string.string_favourites);
        }
        else
        {
            tab.setText(getResources().getString(R.string.string_favourites) + " ( " + count + " )");
        }
    }

    public FragmentOpportunities getSearchFragment()
    {
        return searchFragment;
    }
}
