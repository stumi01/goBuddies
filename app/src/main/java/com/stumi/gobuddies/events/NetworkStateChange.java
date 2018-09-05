package com.stumi.gobuddies.events;

/**
 * Created by sourc on 2017. 04. 10..
 */

public class NetworkStateChange {
    private final boolean connected;

    public NetworkStateChange(boolean connected) {
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }
}
