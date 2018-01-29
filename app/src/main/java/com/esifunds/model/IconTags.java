package com.esifunds.model;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.esifunds.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by napalm on 29/01/18.
 */

public class IconTags
{
    HashMap<String, String> iconTagsMap = new HashMap<>();
    private static IconTags instance = null;

    private IconTags() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("icon_tags").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
                for(DataSnapshot aSnapshotIterable : snapshotIterable)
                {
                    iconTagsMap.put(aSnapshotIterable.getKey(), aSnapshotIterable.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    public static IconTags getInstance() {
        if(instance == null) {
            instance = new IconTags();
        }
        return instance;
    }

    public int getIconForTheme(Context context, String theme, boolean thumb)
    {
        if(iconTagsMap != null)
        {
            for(String tag : iconTagsMap.keySet())
            {
                if(theme.toUpperCase().contains(tag.toUpperCase()))
                {
                    if(thumb)
                    {
                        return iconNameToDrawable(context, iconTagsMap.get(tag) + "_icon");
                    }

                    return iconNameToDrawable(context, iconTagsMap.get(tag));
                }
            }
        }

        if(thumb)
        {
            return R.drawable.ic_default_icon;
        }

        return R.drawable.ic_default;
    }

    private int iconNameToDrawable(Context context, String name)
    {
        Resources resources = context.getResources();
        return resources.getIdentifier(name, "drawable", context.getPackageName());
    }
}
