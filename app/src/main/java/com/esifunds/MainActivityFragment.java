package com.esifunds;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivityFragment extends Fragment implements View.OnClickListener
{
    public MainActivityFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Button buttonLogin = rootView.findViewById(R.id.buttonWelcomeLogin);
        Button buttonRegister = rootView.findViewById(R.id.buttonWelcomeRegister);
        Button buttonGuest = rootView.findViewById(R.id.buttonWelcomeGuest);

        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
        buttonGuest.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v)
    {
        Fragment fragment = null;

        switch(v.getId())
        {
            case R.id.buttonWelcomeLogin:
            {
                fragment = new LoginFragment();
                replaceFragment(fragment);

                break;
            }
        }
    }

    public void replaceFragment(Fragment replacement)
    {
        // @TODO - Fix Fragment Overlapping Issue

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentMain, replacement);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
