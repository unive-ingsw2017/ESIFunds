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
import com.esifunds.activity.MainActivity;
import com.esifunds.activity.OpportunitiesActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FragmentRegister extends Fragment implements View.OnClickListener
{
    private Button buttonRegisterProcedure;
    private FirebaseAuth mAuth;

    public FragmentRegister()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View viewRoot = inflater.inflate(R.layout.fragment_register, container, false);

        buttonRegisterProcedure = viewRoot.findViewById(R.id.buttonRegisterProcedure);

        buttonRegisterProcedure.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        return viewRoot;
    }

    @Override public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.buttonRegisterProcedure:
            {
                String email = ((TextInputEditText)getView().findViewById(R.id.registerMail)).getText().toString();
                String password = ((TextInputEditText)getView().findViewById(R.id.registerPassword)).getText().toString();
                final String firstName = ((TextInputEditText)getView().findViewById(R.id.registerFirstName)).getText().toString();
                final String lastName = ((TextInputEditText)getView().findViewById(R.id.registerLastName)).getText().toString();

                if(!email.isEmpty() && !password.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty())
                {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference userRef = database.getReference("users/" + user.getUid());
                                userRef.child("firstname").setValue(firstName);
                                userRef.child("lastname").setValue(lastName);

                                Intent intentOpportunities = new Intent(getActivity(), OpportunitiesActivity.class);

                                intentOpportunities.putExtra("ACTIVITY_TYPE", "REGISTER");

                                intentOpportunities.putExtra("USER_FIRSTNAME", firstName);
                                intentOpportunities.putExtra("USER_LASTNAME", lastName);
                                intentOpportunities.putExtra("USER_MAIL", "email@gmail.com");

                                startActivity(intentOpportunities);
                                if(MainActivity.getInstance() != null)
                                {
                                    MainActivity.getInstance().finish();
                                }
                            }
                            else
                            {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                break;
            }
        }
    }
}
