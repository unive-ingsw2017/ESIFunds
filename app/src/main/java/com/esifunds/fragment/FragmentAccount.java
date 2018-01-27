package com.esifunds.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esifunds.R;

public class FragmentAccount extends Fragment
{
    public FragmentAccount()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View viewRoot = inflater.inflate(R.layout.fragment_account, container, false);

        FloatingActionButton floatingActionButton = viewRoot.findViewById(R.id.floatingActionButtonAccountManagementConfirm);
        floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                // TODO: Insert code to update user data
            }
        });

        return viewRoot;
    }
}
