package com.stumi.rxlocation.exceptions;

/**
 * @author stumpfb on 20/01/2017.
 */
public class GoogleAPIConnectionSuspendedException extends RuntimeException {

    private final int cause;

    public GoogleAPIConnectionSuspendedException(int cause) {
        this.cause = cause;
    }

    public int getErrorCause() {
        return cause;
    }
}
