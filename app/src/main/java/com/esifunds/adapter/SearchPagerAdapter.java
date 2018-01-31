package com.esifunds.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.esifunds.R;
import com.esifunds.fragment.FragmentMap;
import com.esifunds.fragment.FragmentOpportunities;
import com.esifunds.fragment.FragmentSearch;

/**
 * Created by napalm on 31/01/18.
 */
public class SearchPagerAdapter extends FragmentStatePagerAdapter
{
    final int PAGE_COUNT = 3;
    private FragmentSearch fragmentSearch;

    public SearchPagerAdapter(FragmentManager fm, FragmentSearch fragmentSearch) {
        super(fm);
        this.fragmentSearch = fragmentSearch;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment result = null;
        switch(position)
        {
            case 0:
            {
                FragmentOpportunities searchFragment = new FragmentOpportunities();
                searchFragment.setFragmentSearch(fragmentSearch);
                Bundle args = new Bundle();
                args.putBoolean("IS_SEARCH", true);

                Bundle myArgs = fragmentSearch.getArguments();
                if(myArgs != null)
                {
                    args.putString("SEARCH_OGGETTO", myArgs.getString("SEARCH_OGGETTO", ""));
                    args.putString("SEARCH_TEMA", myArgs.getString("SEARCH_TEMA", ""));
                    args.putString("SEARCH_BENEFICIARIO", myArgs.getString("SEARCH_BENEFICIARIO", ""));
                    args.putString("SEARCH_REGIONE", myArgs.getString("SEARCH_REGIONE", ""));
                }

                searchFragment.setArguments(args);

                result = searchFragment;
                break;
            }

            case 1:
            {
                FragmentOpportunities searchFragment = new FragmentOpportunities();
                searchFragment.setFragmentSearch(fragmentSearch);

                Bundle args = new Bundle();
                args.putBoolean("IS_FAVOURITES", true);
                searchFragment.setArguments(args);

                result = searchFragment;
                break;
            }

            case 2:
            {
                FragmentMap map = new FragmentMap();
                map.setFragmentSearch(fragmentSearch);

                result = map;
                break;
            }
        }

        return result;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if(object instanceof FragmentOpportunities)
        {
            fragmentSearch.setSearchFragment(((FragmentOpportunities)object));
        }

        super.setPrimaryItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position)
        {
            case 0:
            {
                return fragmentSearch.getResources().getString(R.string.string_search);
            }
            case 1:
            {
                return fragmentSearch.getResources().getString(R.string.string_favourites);
            }
            case 2:
            {
                return fragmentSearch.getResources().getString(R.string.string_map);
            }
        }

        return "";
    }
}