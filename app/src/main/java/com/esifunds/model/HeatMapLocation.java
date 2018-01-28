package com.esifunds.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by napalm on 28/01/18.
 */

public class HeatMapLocation
{
    private String regionName;
    private int numHits;
    private LatLng latLng;

    public HeatMapLocation(String regionName, int numHits, LatLng latLng)
    {
        this.regionName = regionName;
        this.numHits = numHits;
        this.latLng = latLng;
    }

    public void setRegionName(String regionName)
    {
        this.regionName = regionName;
    }

    public void setNumHits(int numHits)
    {
        this.numHits = numHits;
    }

    public void setLatLng(LatLng latLng)
    {
        this.latLng = latLng;
    }

    public String getRegionName()
    {
        return regionName;
    }

    public int getNumHits()
    {
        return numHits;
    }

    public LatLng getLatLng()
    {
        return latLng;
    }
}
