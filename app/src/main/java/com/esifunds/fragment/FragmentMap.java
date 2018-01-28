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

public class FragmentMap extends Fragment implements OnMapReadyCallback
{
    private GoogleMap fragmentGoogleMap;
    private final LatLngBounds latLngBoundsItaly = new LatLngBounds(new LatLng(5.93, 34.76), new LatLng(18.99, 47.1));
    private final LatLng latLngCenterItaly = new LatLng(12.460000, 40.930000);

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

        /*
        KmlLayer kmlLayerItaly = null;
        try
        {
            kmlLayerItaly = new KmlLayer(fragmentGoogleMap, R.raw.ita_adm3, getContext());
            kmlLayerItaly.addLayerToMap();
        }
        catch(Exception exceptionError)
        {
            Log.i("EXCEPTION", "EXCEPTION ERROR");
        }
        */

        //fragmentGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));

        //googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        /*
        // Adding Marker for Sydney
        LatLng australiaSydney = new LatLng(-34, 151);
        fragmentGoogleMap.addMarker(new MarkerOptions().position(australiaSydney).title("Australia Sydney"));
        fragmentGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(australiaSydney));
        */

        // < Check this Better >
        fragmentGoogleMap.setLatLngBoundsForCameraTarget(latLngBoundsItaly);
        fragmentGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBoundsItaly, 0));
        //fragmentGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLngCenterItaly));

        //googleMap.setMinZoomPreference(6.0f);
    }
}
