package com.esifunds;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentWelcome extends Fragment implements View.OnClickListener
{
    private Button buttonLogin;
    private Button buttonRegister;
    private Button buttonGuest;

    public FragmentWelcome()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View viewRoot = inflater.inflate(R.layout.fragment_welcome, container, false);

        buttonLogin = viewRoot.findViewById(R.id.buttonLogin);
        buttonRegister = viewRoot.findViewById(R.id.buttonRegister);
        buttonGuest = viewRoot.findViewById(R.id.buttonGuest);

        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
        buttonGuest.setOnClickListener(this);

        return viewRoot;
    }

    @Override public void onClick(View view)
    {
        Fragment fragmentReplacement = null;

        switch(view.getId())
        {
            case R.id.buttonLogin:
            {
                fragmentReplacement = new FragmentLogin();
                fragmentReplace(fragmentReplacement);

                break;
            }

            case R.id.buttonRegister:
            {
                fragmentReplacement = new FragmentRegister();
                fragmentReplace(fragmentReplacement);
            }

            case R.id.buttonGuest:
            {
                Intent intentOpportunities = new Intent(getActivity(), OpportunitiesActivity.class);

                intentOpportunities.putExtra("ACTIVITY_TYPE", "GUEST");

                startActivity(intentOpportunities);
            }
        }
    }

    public void fragmentReplace(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentPlaceholderMainActivity, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
