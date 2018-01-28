package com.esifunds.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esifunds.R;
import com.esifunds.model.HeatMapLocation;
import com.esifunds.model.Opportunity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FragmentMap extends Fragment implements OnMapReadyCallback
{
    private GoogleMap fragmentGoogleMap;
    private FirebaseDatabase mDatabase;
    private final LatLngBounds latLngBoundsItaly = new LatLngBounds(new LatLng(34.76, 5.93), new LatLng(47.1, 18.99));
    //private final LatLng latLngCenterItaly = new LatLng(40.93, 12.46);

    public FragmentMap()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View viewRoot = inflater.inflate(R.layout.fragment_map, container, false);

        mDatabase = FirebaseDatabase.getInstance();
        SupportMapFragment supportMapFragmentClass = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.supportMapFragmentView);
        supportMapFragmentClass.getMapAsync(this);

        return viewRoot;
    }

    @Override public void onMapReady(GoogleMap googleMap)
    {
        fragmentGoogleMap = googleMap;

        fragmentGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));

        mDatabase.getReference("region_coordinates").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                final Map<String, HeatMapLocation> regionLocations = new HashMap<>();

                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();

                for(DataSnapshot aSnapshotIterable : snapshotIterable)
                {
                    if(aSnapshotIterable.child("lat").getValue(Double.class) == null || aSnapshotIterable.child("lng").getValue(Double.class) == null)
                    {
                        continue;
                    }

                    LatLng latLng = new LatLng(aSnapshotIterable.child("lat").getValue(Double.class), aSnapshotIterable.child("lng").getValue(Double.class));
                    regionLocations.put(aSnapshotIterable.getKey(), new HeatMapLocation(aSnapshotIterable.getKey(), 0, latLng));
                }

                mDatabase.getReference("opportunities").addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();

                        Map<String, HeatMapLocation> heatMapLocations = new HashMap<>();
                        for(DataSnapshot aSnapshotIterable : snapshotIterable)
                        {
                            Opportunity opportunity = aSnapshotIterable.getValue(Opportunity.class);

                            if(opportunity == null)
                            {
                                continue;
                            }

                            String region = opportunity.getLUOGO();
                            if(heatMapLocations.containsKey(region))
                            {
                                HeatMapLocation location = heatMapLocations.get(region);
                                location.setNumHits(location.getNumHits() + 1);
                                heatMapLocations.put(region, location);
                            }
                            else
                            {
                                if(regionLocations.containsKey(region))
                                {
                                    HeatMapLocation location = regionLocations.get(region);
                                    location.setNumHits(1);

                                    heatMapLocations.put(region, location);
                                }
                            }
                        }

                        heatMapLocations.containsKey("Veneto");
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

        // HeatMap Code
        List<LatLng> latLngList = null;
        try
        {
            latLngList = buildLatLngList(R.raw.map_heat);
        }
        catch(JSONException jsonException)
        {
            jsonException.printStackTrace();
        }

        HeatmapTileProvider heatmapTileProvider = new HeatmapTileProvider.Builder().data(latLngList).radius(50).build();
        TileOverlayOptions tileOverlayOptions = new TileOverlayOptions().tileProvider(heatmapTileProvider);
        fragmentGoogleMap.addTileOverlay(tileOverlayOptions);

        fragmentGoogleMap.setLatLngBoundsForCameraTarget(latLngBoundsItaly);
        fragmentGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBoundsItaly, 0));
    }

    private ArrayList<LatLng> buildLatLngList(int resIdentifier) throws JSONException
    {
        ArrayList<LatLng> latLngArrayList = new ArrayList<LatLng>();
        InputStream inputStream = getResources().openRawResource(resIdentifier);

        String jsonString = new Scanner(inputStream).useDelimiter("\\A").next();
        JSONArray jsonArray = new JSONArray(jsonString);

        for(int i = 0; i < jsonArray.length(); i++)
        {
            JSONObject jsonObject =  jsonArray.getJSONObject(i);

            double lat = jsonObject.getDouble("lat");
            double lng = jsonObject.getDouble("lng");

            latLngArrayList.add(new LatLng(lat, lng));
        }

        return latLngArrayList;
    }
}
