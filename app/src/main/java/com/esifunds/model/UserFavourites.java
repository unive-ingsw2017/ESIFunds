package com.esifunds.model;

import android.util.LongSparseArray;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by napalm on 25/01/18.
 */

public class UserFavourites
{
    private static UserFavourites instance = null;
    LongSparseArray<Boolean> mFavourites = new LongSparseArray<>();

    private UserFavourites() {
        loadFavourites();
    }

    public void loadFavourites()
    {
        if(UserFavourites.getAll() == null)
        {
            return;
        }

        UserFavourites.getAll().addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();

                mFavourites.clear();
                for(DataSnapshot aSnapshotIterable : snapshotIterable)
                {
                    mFavourites.put(Long.parseLong(aSnapshotIterable.getKey()), true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    public LongSparseArray<Boolean> getmFavourites()
    {
        return mFavourites;
    }

    public static UserFavourites getInstance() {
        if(instance == null) {
            instance = new UserFavourites();
        }

        return instance;
    }

    public static DatabaseReference getAll()
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseDatabase == null || firebaseUser == null)
        {
            return null;
        }

        return firebaseDatabase.getReference(String.format("users/%s/favourites", firebaseUser.getUid()));
    }

    public static DatabaseReference get(long id)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseDatabase == null || firebaseUser == null)
        {
            return null;
        }

        return firebaseDatabase.getReference(String.format("users/%s/favourites/%d", firebaseUser.getUid(), id));
    }

    public static void toggleFavourite(final long id)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseDatabase == null || firebaseUser == null)
        {
            return;
        }

        if(UserFavourites.getInstance().getmFavourites().get(id) != null)
        {
            UserFavourites.getInstance().getmFavourites().remove(id);
        }
        else
        {
            UserFavourites.getInstance().getmFavourites().put(id, true);
        }

        firebaseDatabase.getReference(String.format("users/%s/favourites", firebaseUser.getUid(), id)).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                String sID = Long.toString(id);
                if(dataSnapshot.hasChild(sID))
                {
                    dataSnapshot.child(sID).getRef().removeValue();
                }
                else
                {
                    dataSnapshot.child(sID).getRef().setValue("1");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }
}
