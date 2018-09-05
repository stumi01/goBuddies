package com.stumi.gobuddies.system.tracking;

import com.crashlytics.android.answers.Answers;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sourc on 2017. 01. 31..
 */
@Singleton
public class TrackingListener {

    private final EventBus eventBus;

    @Inject
    public TrackingListener(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void startTracking() {
        eventBus.register(this);
    }

    public void stopTracking() {
        eventBus.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onKPIEvent(KPIEvent event) {
        Answers.getInstance().logCustom(event.getCustomEvent());
    }

}
