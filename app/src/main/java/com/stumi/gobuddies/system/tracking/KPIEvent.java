package com.stumi.gobuddies.system.tracking;

import com.crashlytics.android.answers.CustomEvent;

/**
 * Created by sourc on 2017. 01. 31..
 */
public class KPIEvent {
    private final CustomEvent customEvent;

    public KPIEvent(CustomEvent customEvent) {
        this.customEvent = customEvent;
    }

    public CustomEvent getCustomEvent() {
        return customEvent;
    }
}
