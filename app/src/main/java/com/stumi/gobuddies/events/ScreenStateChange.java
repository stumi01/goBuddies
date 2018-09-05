package com.stumi.gobuddies.events;

/**
 * Created by sourc on 2017. 02. 23..
 */
public class ScreenStateChange {
    private boolean screenOn;

    public ScreenStateChange(boolean screenOn) {
        this.screenOn = screenOn;
    }

    public boolean isScreenOn() {
        return screenOn;
    }
}
