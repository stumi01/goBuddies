package com.stumi.gobuddies.system.network;

/**
 * @author stumpfb on 23/12/2016.
 */
public class RetrofitException extends Throwable {

    private final String customErrorMessage;

    public RetrofitException(String errorMessage, String customErrorMessage) {
        super(errorMessage);
        this.customErrorMessage = customErrorMessage;
    }

    public String getCustomErrorMessage() {
        return customErrorMessage;
    }
}