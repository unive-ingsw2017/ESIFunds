package com.esifunds;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentLogin extends Fragment implements View.OnClickListener
{
    private Button buttonLoginProcedure;

    public FragmentLogin()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View viewRoot = inflater.inflate(R.layout.fragment_login, container, false);

        buttonLoginProcedure = viewRoot.findViewById(R.id.buttonLoginProcedure);

        buttonLoginProcedure.setOnClickListener(this);

        return viewRoot;
    }

    @Override public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.buttonLoginProcedure:
            {
                Intent intentOpportunities = new Intent(getActivity(), OpportunitiesActivity.class);

                intentOpportunities.putExtra("ACTIVITY_TYPE", "LOGIN");

                intentOpportunities.putExtra("USER_FIRSTNAME", "FirstName");
                intentOpportunities.putExtra("USER_LASTNAME", "LastName");
                intentOpportunities.putExtra("USER_MAIL", "email@gmail.com");

                startActivity(intentOpportunities);

                break;
            }
        }
    }
}
