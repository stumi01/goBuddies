package com.stumi.gobuddies.events;

/**
 * Created by sourc on 2017. 02. 27..
 */
public class GpsStatusEvent {
    private boolean isEnabled;

    public GpsStatusEvent(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
