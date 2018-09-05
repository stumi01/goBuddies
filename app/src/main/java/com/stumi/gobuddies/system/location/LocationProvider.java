package com.stumi.gobuddies.system.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.stumi.gobuddies.events.GpsStatusEvent;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * @author stumpfb on 14/08/2016.
 */
public class LocationProvider {
    private static final String TAG = LocationProvider.class.getSimpleName();

    private static final int MIN_TIME = 5000;

    private static final int MIN_DISTANCE = 10;

    private final Context context;
    private final LocationManager locationManager;
    private EventBus eventBus;

    private LocationListener locationListener;

    @Inject
    public LocationProvider(Context context, EventBus eventBus) {
        this.context = context;
        locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);
        this.eventBus = eventBus;
    }

    public void start(LocationUpdateListener listener) {
        if (locationListener == null) {
            locationListener = new InnerLocationListener(listener);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
            } else if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
            }
        }
        triggerWithLastKnownLocation(listener);
    }

    private void triggerWithLastKnownLocation(LocationUpdateListener listener) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lastKnownLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location lastKnownLocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (lastKnownLocationGPS == null && lastKnownLocationNetwork == null) {
            return;
        } else if (lastKnownLocationGPS != null && lastKnownLocationNetwork == null) {
            listener.update(lastKnownLocationGPS.getLongitude(), lastKnownLocationGPS.getLatitude());
        } else if (lastKnownLocationGPS == null && lastKnownLocationNetwork != null) {
            listener.update(lastKnownLocationNetwork.getLongitude(), lastKnownLocationNetwork.getLatitude());
        } else if (lastKnownLocationGPS.getTime() > lastKnownLocationNetwork.getTime()) {
            listener.update(lastKnownLocationGPS.getLongitude(), lastKnownLocationGPS.getLatitude());
        } else {
            listener.update(lastKnownLocationNetwork.getLongitude(), lastKnownLocationNetwork.getLatitude());
        }
    }

    public void stop() {
        if (locationListener != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(locationListener);
        }
        locationListener = null;
    }

    private class InnerLocationListener implements LocationListener {

        private final LocationUpdateListener listener;

        public InnerLocationListener(LocationUpdateListener listener) {
            this.listener = listener;
        }

        @Override
        public void onLocationChanged(Location loc) {
            String longitude = "Longitude: " + loc.getLongitude();
            Log.v(TAG, longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            Log.v(TAG, latitude);
            listener.update(loc.getLongitude(), loc.getLatitude());
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled");
            eventBus.postSticky(new GpsStatusEvent(false));
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled");
            eventBus.postSticky(new GpsStatusEvent(true));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }
}
