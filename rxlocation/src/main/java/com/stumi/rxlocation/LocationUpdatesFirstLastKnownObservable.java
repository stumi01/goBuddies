package com.stumi.rxlocation;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.stumi.rxlocation.base.BaseLocationObservable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * @author stumpfb on 20/01/2017.
 */

public class LocationUpdatesFirstLastKnownObservable extends BaseLocationObservable<Location> {

    @RequiresPermission(
            anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    public static Observable<Location> createObservable(Context ctx, LocationRequest locationRequest) {
        return Observable.create(new LocationUpdatesFirstLastKnownObservable(ctx, locationRequest));
    }

    private final LocationRequest locationRequest;

    private final Context ctx;

    private LocationListener listener;

    private LocationUpdatesFirstLastKnownObservable(Context ctx, LocationRequest locationRequest) {
        super(ctx);
        this.ctx = ctx;
        this.locationRequest = locationRequest;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient,
                                          final ObservableEmitter<? super Location> observer) {
        if (ActivityCompat.checkSelfPermission(ctx,
                ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(ctx, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(apiClient);
        if (location != null) {
            observer.onNext(location);
        }

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                observer.onNext(location);
            }
        };

        LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, listener);
    }

    @Override
    protected void onUnsubscribed(GoogleApiClient locationClient) {
        if (locationClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(locationClient, listener);
        }
    }
}
