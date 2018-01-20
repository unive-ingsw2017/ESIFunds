package com.esifunds.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.esifunds.R;
import com.esifunds.activity.OpportunitiesActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FragmentLogin extends Fragment implements View.OnClickListener
{
    private Button buttonLoginProcedure;
    private FirebaseAuth mAuth;

    public FragmentLogin()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View viewRoot = inflater.inflate(R.layout.fragment_login, container, false);

        buttonLoginProcedure = viewRoot.findViewById(R.id.buttonLoginProcedure);

        buttonLoginProcedure.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        return viewRoot;
    }

    @Override public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.buttonLoginProcedure:
            {
                String email = ((TextInputEditText) getView().findViewById(R.id.loginMail)).getText().toString();
                String password = ((TextInputEditText) getView().findViewById(R.id.loginPassword)).getText().toString();

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {
                                // Sign in success, update UI with the signed-in user's information
                                Intent intentOpportunities = new Intent(getActivity(), OpportunitiesActivity.class);
                                intentOpportunities.putExtra("ACTIVITY_TYPE", "LOGIN");
                                startActivity(intentOpportunities);
                            }
                            else
                            {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                break;
            }
        }
    }
}
