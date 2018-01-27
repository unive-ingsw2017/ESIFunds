package com.esifunds.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.esifunds.R;
import com.esifunds.fragment.FragmentAccount;
import com.esifunds.fragment.FragmentOpportunities;
import com.esifunds.fragment.FragmentSearch;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

public class OpportunitiesActivity extends AppCompatActivity
{
    private ImageButton opportunitiesSearch;
    private ImageButton imageButtonFullSearch;
    private TextInputEditText textInputEditTextSearch;
    private FragmentSearch fragmentSearch;
    private Drawer drawerResult;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opportunities);

        // Intent Parsing
        Intent intentRoot = getIntent();
        final String activityType = intentRoot.getStringExtra("ACTIVITY_TYPE");

        String userName = "";
        String email = "";

        // Toolbar Code
        Toolbar opportunitiesToolbar = findViewById(R.id.toolbarOpportunitiesActivity);

        opportunitiesToolbar.setTitle("");

        setSupportActionBar(opportunitiesToolbar);


        opportunitiesSearch = findViewById(R.id.imageButtonOpportunitiesActivitySearch);
        imageButtonFullSearch = findViewById(R.id.imageButtonFullSearch);
        textInputEditTextSearch = findViewById(R.id.textInputEditTextSearch);

        textInputEditTextSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                fragmentSearch.getFragmentOpportunities().searchWithString(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

        opportunitiesSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentSearch = new FragmentSearch();
                fragmentTransaction.replace(R.id.fragmentPlaceholderOpportunitiesActivity, fragmentSearch);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                textInputEditTextSearch.setVisibility(View.VISIBLE);

                opportunitiesSearch.setVisibility(View.GONE);
                imageButtonFullSearch.setVisibility(View.VISIBLE);
            }
        });

        imageButtonFullSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                textInputEditTextSearch.setVisibility(View.GONE);

                opportunitiesSearch.setVisibility(View.VISIBLE);
                imageButtonFullSearch.setVisibility(View.GONE);

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentPlaceholderOpportunitiesActivity, new FragmentOpportunities());
                fragmentTransaction.commit();
            }
        });

        // Drawer Code
        final PrimaryDrawerItem drawerItemOpportunitiesList = new PrimaryDrawerItem().withIdentifier(0).withName(R.string.string_opportunities_list).withIcon(GoogleMaterial.Icon.gmd_list);
        final SecondaryDrawerItem drawerItemSearch = new SecondaryDrawerItem().withIdentifier(1).withName(R.string.string_search).withIcon(GoogleMaterial.Icon.gmd_search);
        SecondaryDrawerItem drawerItemFavourites = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.string_favourites).withIcon(GoogleMaterial.Icon.gmd_star);

        final SecondaryDrawerItem drawerItemLoginRegister = new SecondaryDrawerItem().withIdentifier(100).withName(R.string.string_login_register).withIcon(FontAwesome.Icon.faw_sign_in);
        final SecondaryDrawerItem drawerItemLogout = new SecondaryDrawerItem().withIdentifier(101).withName(R.string.string_logout).withIcon(FontAwesome.Icon.faw_sign_out);

        ProfileDrawerItem profileDrawerItemGuest = new ProfileDrawerItem().withName(R.string.string_guest).withIcon(R.drawable.ic_login_user);
        final ProfileDrawerItem profileDrawerItemUser = new ProfileDrawerItem().withIcon(R.drawable.ic_login_user).withIdentifier(1);

        final AccountHeader drawerHeaderResult = new AccountHeaderBuilder().withActivity(this).withHeaderBackground(R.drawable.material_drawer_badge)
                .addProfiles
                        (
                                activityType.equals("GUEST") ? profileDrawerItemGuest : profileDrawerItemUser
                        )
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener()
                {
                    @Override public boolean onProfileImageClick(View view, IProfile profile, boolean current)
                    {
                        if(!activityType.equals("GUEST"))
                        {
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragmentPlaceholderOpportunitiesActivity, new FragmentAccount());
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), R.string.string_login_register_action, Toast.LENGTH_SHORT).show();
                        }

                        return false;
                    }

                    @Override public boolean onProfileImageLongClick(View view, IProfile profile, boolean current)
                    {
                        return false;
                    }
                })
                .withSelectionListEnabledForSingleProfile(false)
                .build();

        drawerResult = new DrawerBuilder().withActivity(this).withToolbar(opportunitiesToolbar)
                .withAccountHeader(drawerHeaderResult)
                .addDrawerItems
                        (
                                drawerItemOpportunitiesList,
                                new DividerDrawerItem(),
                                drawerItemSearch
                        )
                .addStickyDrawerItems
                        (
                                activityType.equals("GUEST") ? drawerItemLoginRegister : drawerItemLogout
                        )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener()
                {
                    @Override public boolean onItemClick(View view, int position, IDrawerItem drawerItem)
                    {
                        if(drawerItem == drawerItemSearch)
                        {
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentSearch = new FragmentSearch();
                            fragmentTransaction.add(R.id.fragmentPlaceholderOpportunitiesActivity, fragmentSearch);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                        else if(drawerItem == drawerItemOpportunitiesList)
                        {
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragmentPlaceholderOpportunitiesActivity, new FragmentOpportunities());
                            fragmentTransaction.commit();
                        }
                        else if(drawerItem == drawerItemLogout)
                        {
                            FirebaseAuth.getInstance().signOut();
                            Intent intentOpportunities = new Intent(getApplication(), MainActivity.class);
                            startActivity(intentOpportunities);
                            finish();
                        }
                        else if(drawerItem == drawerItemLoginRegister)
                        {
                            finish();
                        }

                        return false;
                    }
                })
                .build();

        // FastAdapter Code
        if(!activityType.equals("GUEST"))
        {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user != null)
            {
                final String userEmail = user.getEmail();
                profileDrawerItemUser.withEmail(userEmail);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference userRef = database.getReference("users/" + user.getUid());
                userRef.child("firstname").addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        final String userFirstName = dataSnapshot.getValue(String.class);

                        userRef.child("lastname").addListenerForSingleValueEvent(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                profileDrawerItemUser.withName(userFirstName + " " + dataSnapshot.getValue(String.class));
                                drawerHeaderResult.updateProfile(profileDrawerItemUser);
                            }

                            @Override
                            public void onCancelled(DatabaseError error)
                            {
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError error)
                    {
                    }
                });
            }
        }

        if(!activityType.equals("GUEST"))
            drawerResult.addItem(drawerItemFavourites);

        drawerResult.setSelection(drawerItemOpportunitiesList);
    }

    @Override
    public void onBackPressed()
    {
        if(textInputEditTextSearch.getVisibility() == View.VISIBLE) // If search bar is open, close
        {
            textInputEditTextSearch.setVisibility(View.GONE);

            opportunitiesSearch.setVisibility(View.VISIBLE);
            imageButtonFullSearch.setVisibility(View.GONE);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentPlaceholderOpportunitiesActivity, new FragmentOpportunities());
            fragmentTransaction.commit();
        }
        else if(!drawerResult.isDrawerOpen()) // If drawer isn't open and we're at top-level, exit
        {
            finish();
        }
        else if(drawerResult.isDrawerOpen())
        {
            drawerResult.closeDrawer();
        }
        else // let android handle it
        {
            super.onBackPressed();
        }
    }
}
