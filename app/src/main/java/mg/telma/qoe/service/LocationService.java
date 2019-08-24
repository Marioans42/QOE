package mg.telma.qoe.service;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;


public class LocationService  implements LocationListener {

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 50L;
    private static final long MIN_TIME_BW_UPDATES = 1000L;
    boolean canGetLocation = false;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    double latitude;
    Location location;
    protected LocationManager locationManager;
    double longitude;
    private final Context mContext;

    public LocationService(Context mContext) {
        this.mContext = mContext;
    }

    public double getLongitude() {
        if (this.location != null) {
            this.longitude = this.location.getLongitude();
        }
        return this.longitude;
    }


    public double getLatitude() {
        if (this.location != null) {
            this.latitude = this.location.getLatitude();
        }
        return this.latitude;
    }

    public void getLocation(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        return;
        }
        try {
            this.locationManager = (LocationManager) this.mContext.getSystemService(Context.LOCATION_SERVICE);
            this.isGPSEnabled = this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            this.isNetworkEnabled = this.locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!this.isGPSEnabled && !this.isNetworkEnabled) {
                return ;
            }
            this.canGetLocation = true;
            if (this.isNetworkEnabled) {
                this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, 50.0f, this);

                if (this.locationManager != null) {
                    this.location = this.locationManager.getLastKnownLocation("network");
                    if (this.location != null) {
                        this.latitude = this.location.getLatitude();
                        this.longitude = this.location.getLongitude();
                    }
                }
            }

            this.locationManager.requestLocationUpdates("gps", 1000L, MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
            this.location = this.locationManager.getLastKnownLocation("gps");
            this.latitude = this.location.getLatitude();
            this.longitude = this.location.getLongitude();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        this.getLatitude();
        this.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
