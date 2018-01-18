package com.esifunds;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentRegister extends Fragment implements View.OnClickListener
{
    private Button buttonRegisterProcedure;

    public FragmentRegister()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View viewRoot = inflater.inflate(R.layout.fragment_register, container, false);

        buttonRegisterProcedure = viewRoot.findViewById(R.id.buttonRegisterProcedure);

        buttonRegisterProcedure.setOnClickListener(this);

        return viewRoot;
    }

    @Override public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.buttonRegisterProcedure:
            {
                Intent intentOpportunities = new Intent(getActivity(), OpportunitiesActivity.class);

                intentOpportunities.putExtra("ACTIVITY_TYPE", "REGISTER");

                intentOpportunities.putExtra("USER_FIRSTNAME", "FirstName");
                intentOpportunities.putExtra("USER_LASTNAME", "LastName");
                intentOpportunities.putExtra("USER_MAIL", "email@gmail.com");

                startActivity(intentOpportunities);

                break;
            }
        }
    }
}
