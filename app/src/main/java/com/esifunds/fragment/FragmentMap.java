package com.esifunds.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentMap extends Fragment implements OnMapReadyCallback
{
    private GoogleMap fragmentGoogleMap;
    private FirebaseDatabase mDatabase;

    private final LatLngBounds latLngBoundsItaly = new LatLngBounds(new LatLng(34.76, 5.93), new LatLng(47.1, 18.99));
    private FragmentSearch fragmentSearch;
    //private List<LatLng> latLngList;
    //private final LatLng latLngCenterItaly = new LatLng(40.93, 12.46);

    public FragmentMap()
    {
    }

    public void setFragmentSearch(FragmentSearch fragmentSearch)
    {
        this.fragmentSearch = fragmentSearch;
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

        // LatLng Bounds Code
        fragmentGoogleMap.setLatLngBoundsForCameraTarget(latLngBoundsItaly);
        fragmentGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBoundsItaly, 0));

        // HeatMap Data
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
                            opportunity.setContext(getContext());

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

                        List<LatLng> latLngList = new ArrayList<>();

                        for(Map.Entry<String, HeatMapLocation> heatMapLocationEntry : heatMapLocations.entrySet())
                        {
                            for(int i = 0; i < heatMapLocationEntry.getValue().getNumHits(); i++)
                            {
                                latLngList.add(heatMapLocationEntry.getValue().getLatLng());
                            }

                            fragmentGoogleMap.addMarker
                            (
                                new MarkerOptions()
                                    .position(heatMapLocationEntry.getValue().getLatLng())
                                    .title(heatMapLocationEntry.getKey())
                                    .snippet(getResources().getString(R.string.string_marker_snippet) + " " + heatMapLocationEntry.getValue().getNumHits())
                            ).setTag(heatMapLocationEntry.getKey());
                        }

                        HeatmapTileProvider heatmapTileProvider = new HeatmapTileProvider.Builder().data(latLngList).radius(50).build();
                        TileOverlayOptions tileOverlayOptions = new TileOverlayOptions().tileProvider(heatmapTileProvider);
                        fragmentGoogleMap.addTileOverlay(tileOverlayOptions);

                        fragmentGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
                        {
                            @Override public boolean onMarkerClick(Marker marker)
                            {
                                FragmentSearch fragmentSearch = new FragmentSearch();
                                Bundle args = new Bundle();
                                args.putInt("POSITION", 0);
                                args.putBoolean("IS_SEARCH", true);
                                args.putString("SEARCH_REGIONE", (String)marker.getTag());
                                fragmentSearch.setArguments(args);

                                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.fragmentPlaceholderOpportunitiesActivity, fragmentSearch);
                                fragmentTransaction.commit();
                                return false;
                            }
                        });
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
}
