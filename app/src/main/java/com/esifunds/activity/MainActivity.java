package com.esifunds.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

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

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent)
    {
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
        {
            View view = getCurrentFocus();

            if(view instanceof EditText)
            {
                Rect outRect = new Rect();

                view.getGlobalVisibleRect(outRect);
                if(!outRect.contains((int)motionEvent.getRawX(), (int)motionEvent.getRawY()))
                {
                    view.clearFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }

        return super.dispatchTouchEvent(motionEvent);
    }
}
