package com.esifunds.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.esifunds.R;
import com.esifunds.fragment.FragmentOpportunities;
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
    private Toolbar opportunitiesToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opportunities);

        // Intent Parsing
        Intent intentRoot = getIntent();
        final String activityType = intentRoot.getStringExtra("ACTIVITY_TYPE");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
        {
            final String userEmail = user.getEmail();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference userRef = database.getReference("users/" + user.getUid());
            userRef.child("firstname").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final String userFirstName = dataSnapshot.getValue(String.class);

                    userRef.child("lastname").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            init(activityType, userEmail, userFirstName + " " + dataSnapshot.getValue(String.class));
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {}
                    });
                }

                @Override
                public void onCancelled(DatabaseError error) {}
            });
        }
    }

    private void init(String activityType, String email, String userName)
    {
        // Toolbar Code
        opportunitiesToolbar = findViewById(R.id.toolbarOpportunitiesActivity);
        setSupportActionBar(opportunitiesToolbar);

        // Drawer Code
        PrimaryDrawerItem drawerItemOpportunitiesList = new PrimaryDrawerItem().withIdentifier(0).withName(R.string.string_opportunities_list).withIcon(GoogleMaterial.Icon.gmd_list);
        SecondaryDrawerItem drawerItemSearch = new SecondaryDrawerItem().withIdentifier(1).withName(R.string.string_search).withIcon(GoogleMaterial.Icon.gmd_search);
        SecondaryDrawerItem drawerItemFavourites = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.string_favourites).withIcon(GoogleMaterial.Icon.gmd_star);

        SecondaryDrawerItem drawerItemLoginRegister = new SecondaryDrawerItem().withIdentifier(100).withName(R.string.string_login_register).withIcon(FontAwesome.Icon.faw_sign_in);
        SecondaryDrawerItem drawerItemLogout = new SecondaryDrawerItem().withIdentifier(100).withName(R.string.string_logout).withIcon(FontAwesome.Icon.faw_sign_out);

        ProfileDrawerItem profileDrawerItemGuest = new ProfileDrawerItem().withName(R.string.string_guest).withIcon(R.drawable.ic_login_user);
        ProfileDrawerItem profileDrawerItemUser = new ProfileDrawerItem().withName(userName).withEmail(email).withIcon(R.drawable.ic_login_user);

        AccountHeader drawerHeaderResult = new AccountHeaderBuilder().withActivity(this).withHeaderBackground(R.drawable.material_drawer_badge)
                .addProfiles
                        (
                                activityType.equals("GUEST") ? profileDrawerItemGuest : profileDrawerItemUser
                        )
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener()
                {
                    @Override public boolean onProfileImageClick(View view, IProfile profile, boolean current)
                    {
                        return false;
                    }

                    @Override public boolean onProfileImageLongClick(View view, IProfile profile, boolean current)
                    {
                        return false;
                    }
                })
                .withSelectionListEnabledForSingleProfile(false)
                .build();

        Drawer drawerResult = new DrawerBuilder().withActivity(this).withToolbar(opportunitiesToolbar)
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
                        return false;
                    }
                })
                .build();

        if(!activityType.equals("GUEST"))
            drawerResult.addItem(drawerItemFavourites);

        drawerResult.setSelection(drawerItemOpportunitiesList);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentPlaceholderOpportunitiesActivity, new FragmentOpportunities());
        fragmentTransaction.commit();
    }
}
