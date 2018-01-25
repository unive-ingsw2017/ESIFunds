package com.esifunds.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.esifunds.R;
import com.esifunds.fragment.FragmentWelcome;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentPlaceholderMainActivity, new FragmentWelcome());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
