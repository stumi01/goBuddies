package com.stumi.rxlocation.base;

import android.content.Context;

import com.google.android.gms.location.LocationServices;

/**
 * @author stumpfb on 20/01/2017.
 */

public abstract class BaseLocationObservable<T> extends BaseObservable<T> {

    protected BaseLocationObservable(Context ctx) {
        super(ctx, LocationServices.API);
    }
}
