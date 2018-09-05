package com.stumi.rxlocation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.stumi.rxlocation.base.BaseLocationObservable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

/**
 * @author stumpfb on 20/01/2017.
 */

public class LastKnownLocationObservable extends BaseLocationObservable<Location> {

    @RequiresPermission(
            anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    public static Observable<Location> createObservable(Context ctx) {
        return Observable.create(new LastKnownLocationObservable(ctx));
    }

    private Context ctx;

    private LastKnownLocationObservable(Context ctx) {
        super(ctx);
        this.ctx = ctx;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient,
                                          ObservableEmitter<? super Location> observer) {
        if (ActivityCompat.checkSelfPermission(ctx,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(ctx,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(apiClient);
        if (location != null) {
            observer.onNext(location);
        }
        observer.onComplete();
    }
}
