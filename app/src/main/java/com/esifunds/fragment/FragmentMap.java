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
import com.google.android.gms.maps.model.MarkerOptions;

public class FragmentMap extends Fragment implements OnMapReadyCallback
{
    private GoogleMap fragmentGoogleMap;

    public FragmentMap()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View viewRoot = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment supportMapFragmentClass = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.supportMapFragmentView);
        supportMapFragmentClass.getMapAsync(this);

        return viewRoot;
    }

    @Override public void onMapReady(GoogleMap googleMap)
    {
        fragmentGoogleMap = googleMap;

        LatLng australiaSydney = new LatLng(-34, 151);
        fragmentGoogleMap.addMarker(new MarkerOptions().position(australiaSydney).title("Australia Sydney"));
        fragmentGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(australiaSydney));
    }
}
