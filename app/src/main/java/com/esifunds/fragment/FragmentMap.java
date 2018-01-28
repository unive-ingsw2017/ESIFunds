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
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
