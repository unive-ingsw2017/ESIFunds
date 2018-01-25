package com.esifunds.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esifunds.R;
import com.esifunds.activity.OpportunitiesActivity;

public class FragmentSearch extends Fragment
{
    public FragmentSearch()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View viewRoot = inflater.inflate(R.layout.fragment_search, container, false);

        OpportunitiesActivity opportunitiesActivity = (OpportunitiesActivity) getActivity();
        opportunitiesActivity.opportunitiesToolbar.setTitle(null);

        TabLayout tabLayout = viewRoot.findViewById(R.id.tabLayoutFragmentSearch);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override public void onTabSelected(TabLayout.Tab tab)
            {
                switch(tab.getPosition())
                {
                    case 0:
                    {
                        Log.i("TAB", "0");
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

            @Override public void onTabUnselected(TabLayout.Tab tab)
            {

            }

            @Override public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });

        return viewRoot;
    }
}
