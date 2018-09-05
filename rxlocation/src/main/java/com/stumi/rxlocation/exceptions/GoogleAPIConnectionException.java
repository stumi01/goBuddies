package com.stumi.rxlocation.exceptions;

import com.google.android.gms.common.ConnectionResult;

/**
 * @author stumpfb on 20/01/2017.
 */
public class GoogleAPIConnectionException extends RuntimeException {

    private final ConnectionResult connectionResult;

    public GoogleAPIConnectionException(String detailMessage, ConnectionResult connectionResult) {
        super(detailMessage);
        this.connectionResult = connectionResult;
    }

    public ConnectionResult getConnectionResult() {
        return connectionResult;
    }
}