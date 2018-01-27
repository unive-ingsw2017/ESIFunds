package com.esifunds.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.esifunds.R;
import com.esifunds.fragment.FragmentWelcome;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
{
    private static MainActivity instance = null;

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        instance = this;

        Intent intentRoot = getIntent();
        boolean internal = intentRoot.getBooleanExtra("INTERNAL", false);

        setContentView(R.layout.activity_main);

        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            Intent intentOpportunities = new Intent(this, OpportunitiesActivity.class);
            intentOpportunities.putExtra("ACTIVITY_TYPE", "LOGIN");
            startActivity(intentOpportunities);
            finish();
            return;
        }

        SharedPreferences pref = getSharedPreferences("mypref", MODE_PRIVATE);

        if(pref.getBoolean("firststart", true) || internal){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentPlaceholderMainActivity, new FragmentWelcome());
            fragmentTransaction.commit();
        }
        else
        {
            Intent intentOpportunities = new Intent(this, OpportunitiesActivity.class);
            intentOpportunities.putExtra("ACTIVITY_TYPE", "GUEST");
            startActivity(intentOpportunities);
            finish();
        }
    }

    public void setFirstStart(boolean firstStart)
    {
        SharedPreferences.Editor editor =  getSharedPreferences("mypref", MODE_PRIVATE).edit();
        editor.putBoolean("firststart", firstStart);
        editor.apply(); // apply changes
    }
}
