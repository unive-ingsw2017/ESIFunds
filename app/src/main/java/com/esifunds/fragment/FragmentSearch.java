package com.esifunds.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.esifunds.R;
import com.esifunds.activity.OpportunitiesActivity;
import com.esifunds.adapter.SearchPagerAdapter;

public class FragmentSearch extends Fragment
{
    private FragmentOpportunities searchFragment;
    private TabLayout tabLayout;
    private RelativeLayout relativeLayoutAdvancedSearch;
    private RelativeLayout relativeLayoutAdvancedSearchToggle;
    private TextInputEditText textInputEditText;
    private TextInputEditText advancedSearchThemeInput;
    private TextInputEditText advancedSearchPayeeInput;
    private TextInputEditText advancedSearchLocationInput;
    private ImageView imageViewArrowAdvancedSearch;
    private String searchInputSearchObject = "";
    private String searchInputSearchTheme = "";
    private String searchInputSearchPayee = "";
    private String searchInputSearchLocation = "";
    private String searchInputFavouritesObject = "";
    private String searchInputFavouritesTheme = "";
    private String searchInputFavouritesPayee = "";
    private String searchInputFavouritesLocation = "";

    public FragmentSearch()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View viewRoot = inflater.inflate(R.layout.fragment_search, container, false);

        // TextInputEditText textInputEditText
        textInputEditText = getActivity().findViewById(R.id.textInputEditTextSearch);

        tabLayout = viewRoot.findViewById(R.id.tabLayoutFragmentSearch);

        relativeLayoutAdvancedSearch = viewRoot.findViewById(R.id.relativeLayoutAdvancedSearch);
        relativeLayoutAdvancedSearchToggle = viewRoot.findViewById(R.id.relativeLayoutAdvancedSearchToggle);

        ViewPager viewPager = viewRoot.findViewById(R.id.viewpagerFragmentSearch);

        imageViewArrowAdvancedSearch = viewRoot.findViewById(R.id.imageViewArrowAdvancedSearch);
        Button buttonToggleAdvancedSearch = viewRoot.findViewById(R.id.buttonToggleAdvancedSearch);
        buttonToggleAdvancedSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(relativeLayoutAdvancedSearch.getVisibility() == View.VISIBLE)
                {
                    hideAdvancedSearch();
                }
                else
                {
                    showAdvancedSearch();
                }
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                switch(tab.getPosition())
                {
                    case 0:
                    {
                        textInputEditText.setText(searchInputSearchObject);
                        if(advancedSearchThemeInput != null)
                        {
                            advancedSearchThemeInput.setText(searchInputSearchTheme);
                        }

                        if(advancedSearchPayeeInput != null)
                        {
                            advancedSearchPayeeInput.setText(searchInputSearchPayee);
                        }

                        if(advancedSearchLocationInput != null)
                        {
                            advancedSearchLocationInput.setText(searchInputSearchLocation);
                        }

                        break;
                    }

                    case 1:
                    {
                        textInputEditText.setText(searchInputFavouritesObject);

                        if(advancedSearchThemeInput != null)
                        {
                            advancedSearchThemeInput.setText(searchInputFavouritesTheme);
                        }

                        if(advancedSearchPayeeInput != null)
                        {
                            advancedSearchPayeeInput.setText(searchInputFavouritesPayee);
                        }

                        if(advancedSearchLocationInput != null)
                        {
                            advancedSearchLocationInput.setText(searchInputFavouritesLocation);
                        }

                        break;
                    }

                    case 2:
                    {
                        ((OpportunitiesActivity)getActivity()).hideSearchBar();
                        ((OpportunitiesActivity)getActivity()).hideSearchButton();
                        relativeLayoutAdvancedSearch.setVisibility(View.GONE);
                        relativeLayoutAdvancedSearchToggle.setVisibility(View.GONE);

                        break;
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {
                switch(tab.getPosition())
                {
                    case 0:
                    {
                        searchInputSearchObject = textInputEditText.getText().toString();

                        if(advancedSearchThemeInput != null)
                        {
                            searchInputSearchTheme = advancedSearchThemeInput.getText().toString();
                        }
                        else
                        {
                            searchInputSearchTheme = "";
                        }

                        if(advancedSearchPayeeInput != null)
                        {
                            searchInputSearchPayee = advancedSearchPayeeInput.getText().toString();
                        }
                        else
                        {
                            searchInputSearchPayee = "";
                        }

                        if(advancedSearchLocationInput != null)
                        {
                            searchInputSearchLocation = advancedSearchLocationInput.getText().toString();
                        }
                        else
                        {
                            searchInputSearchLocation = "";
                        }

                        break;
                    }
                    case 1:
                    {
                        searchInputFavouritesObject = textInputEditText.getText().toString();

                        if(advancedSearchThemeInput != null)
                        {
                            searchInputFavouritesTheme = advancedSearchThemeInput.getText().toString();
                        }
                        else
                        {
                            searchInputFavouritesTheme = "";
                        }

                        if(advancedSearchPayeeInput != null)
                        {
                            searchInputFavouritesPayee = advancedSearchPayeeInput.getText().toString();
                        }
                        else
                        {
                            searchInputFavouritesPayee = "";
                        }

                        if(advancedSearchLocationInput != null)
                        {
                            searchInputFavouritesLocation = advancedSearchLocationInput.getText().toString();
                        }
                        else
                        {
                            searchInputFavouritesLocation = "";
                        }

                        break;
                    }
                    case 2:
                    {
                        ((OpportunitiesActivity)getActivity()).showSearchButton();
                        relativeLayoutAdvancedSearchToggle.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });

        viewPager.setAdapter(new SearchPagerAdapter(getChildFragmentManager(), this));

        tabLayout.setupWithViewPager(viewPager);

        Bundle myArgs = getArguments();
        if(myArgs != null && myArgs.getInt("POSITION", -1) != -1)
        {
            viewPager.setCurrentItem(myArgs.getInt("POSITION"));
        }

        return viewRoot;
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

    public void setSearchFragment(FragmentOpportunities searchFragment)
    {
        this.searchFragment = searchFragment;
    }

    public void setCurrentTab(int tabIndex, boolean showAdvancedSearch)
    {
        TabLayout.Tab tab = tabLayout.getTabAt(tabIndex);

        if(tab != null)
        {
            tab.select();

            if(showAdvancedSearch)
            {
                showAdvancedSearch();
            }
        }
    }

    private void showAdvancedSearch()
    {
        relativeLayoutAdvancedSearch.setVisibility(View.VISIBLE);
        advancedSearchThemeInput = getActivity().findViewById(R.id.advancedSearchThemeInput);
        advancedSearchPayeeInput = getActivity().findViewById(R.id.advancedSearchPayeeInput);
        advancedSearchLocationInput = getActivity().findViewById(R.id.advancedSearchLocationInput);
        imageViewArrowAdvancedSearch.setImageResource(android.R.drawable.arrow_up_float);
        ((OpportunitiesActivity)getActivity()).registerAdvancedSearchInputs();
    }

    private void hideAdvancedSearch()
    {
        relativeLayoutAdvancedSearch.setVisibility(View.GONE);
        advancedSearchThemeInput = null;
        advancedSearchPayeeInput = null;
        advancedSearchLocationInput = null;
        imageViewArrowAdvancedSearch.setImageResource(android.R.drawable.arrow_down_float);
        ((OpportunitiesActivity)getActivity()).unRegisterAdvancedSearchInputs();
    }
}
