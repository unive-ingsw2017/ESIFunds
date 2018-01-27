package com.esifunds.fragment;

import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.esifunds.R;
import com.esifunds.activity.OpportunitiesActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentAccount extends Fragment
{
    private FirebaseDatabase mDatabase;

    public FragmentAccount()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ((OpportunitiesActivity)getActivity()).hideSearchButton();
        mDatabase = FirebaseDatabase.getInstance();

        final View viewRoot = inflater.inflate(R.layout.fragment_account, container, false);

        FloatingActionButton floatingActionButton = viewRoot.findViewById(R.id.floatingActionButtonAccountManagementConfirm);
        floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                TextInputEditText textInputAccountManagementFirstName = viewRoot.findViewById(R.id.textInputAccountManagementFirstName);
                TextInputEditText textInputAccountManagementLastName = viewRoot.findViewById(R.id.textInputAccountManagementLastName);
                TextInputEditText textInputAccountManagementOldPassword = viewRoot.findViewById(R.id.textInputAccountManagementOldPassword);
                TextInputEditText textInputLayoutAccountManagementNewPassword = viewRoot.findViewById(R.id.textInputAccountManagementNewPassword);

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user == null)
                {
                    return;
                }

                final String firstName = textInputAccountManagementFirstName.getText().toString();
                final String lastName = textInputAccountManagementLastName.getText().toString();
                final String oldPassword = textInputAccountManagementOldPassword.getText().toString();
                final String newPassword = textInputLayoutAccountManagementNewPassword.getText().toString();

                if(!oldPassword.isEmpty())
                {
                    user.reauthenticate(EmailAuthProvider.getCredential(user.getEmail(), oldPassword)).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if(task.isSuccessful())
                            {
                                boolean bWaitForPassword = false;
                                boolean bAllEmpty = true;

                                if(!firstName.isEmpty())
                                {
                                    mDatabase.getReference("users/" + user.getUid() + "/firstname").setValue(firstName);
                                    bAllEmpty = false;
                                }


                                if(!lastName.isEmpty())
                                {
                                    mDatabase.getReference("users/" + user.getUid() + "/lastname").setValue(lastName);
                                    bAllEmpty = false;
                                }

                                if(!newPassword.isEmpty())
                                {
                                    bWaitForPassword = true;
                                    bAllEmpty = false;
                                    user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            Toast.makeText(getContext(), "Successfully updated", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }

                                if(!bWaitForPassword && !bAllEmpty)
                                {
                                    Toast.makeText(getContext(), "Successfully updated", Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Invalid password", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null)
        {
            mDatabase.getReference("users/" + user.getUid() + "/firstname").addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    final String firstName = dataSnapshot.getValue(String.class);
                    mDatabase.getReference("users/" + user.getUid() + "/lastname").addValueEventListener(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            String lastName = dataSnapshot.getValue(String.class);
                            TextView textViewAccountManagementProfileFName = viewRoot.findViewById(R.id.textViewAccountManagementProfileFName);
                            textViewAccountManagementProfileFName.setText(firstName + " " + lastName);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError)
                        {

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }
            });
        }

        return viewRoot;
    }
}
