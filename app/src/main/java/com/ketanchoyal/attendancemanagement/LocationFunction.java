package com.ketanchoyal.attendancemanagement;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class LocationFunction implements LocationListener {

    private double UserLat = 22.3950083;
    private double UserLon = 73.3673489;

    private LatLng User = new LatLng(UserLat,UserLon);

    public LocationFunction(double userLat, double userLon) {
        UserLat = userLat;
        UserLon = userLon;
        User = new LatLng(UserLat,UserLon);
    }

    public LocationFunction(LatLng user) {
        User = user;
    }

    @Override
    public void onLocationChanged(Location location) {
        //UserLat = location.getLatitude();
        //UserLon = location.getLongitude();

        //User = new LatLng(UserLat,UserLon);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public float getDistance(LatLng User) {
        Location l1 = new Location("One");
        l1.setLatitude(22.448939);            //College
        l1.setLongitude(73.355567);

        //l1.setLatitude(22.293698);              //Home
        //l1.setLongitude(73.231624);

        Location l2 = new Location("Two");
        l2.setLatitude(User.latitude);
        l2.setLongitude(User.longitude);

        Log.d("GPS : ",""+l1.distanceTo(l2));

        return l1.distanceTo(l2);
    }

    public boolean getloc()
    {
        float dist = getDistance(User);

        return dist <= 150;

    }

}
