package com.esifunds.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esifunds.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;

public class FragmentMap extends Fragment implements OnMapReadyCallback
{
    private GoogleMap fragmentGoogleMap;
    private final LatLngBounds latLngBoundsItaly = new LatLngBounds(new LatLng(34.76, 5.93), new LatLng(47.1, 18.99));
    //private final LatLng latLngCenterItaly = new LatLng(40.93, 12.46);

    public FragmentMap()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View viewRoot = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment supportMapFragmentClass = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.supportMapFragmentView);
        supportMapFragmentClass.getMapAsync(this);

        return viewRoot;
    }

    @Override public void onMapReady(GoogleMap googleMap)
    {
        fragmentGoogleMap = googleMap;

        fragmentGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));

        fragmentGoogleMap.setLatLngBoundsForCameraTarget(latLngBoundsItaly);
        fragmentGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBoundsItaly, 0));
    }
}
