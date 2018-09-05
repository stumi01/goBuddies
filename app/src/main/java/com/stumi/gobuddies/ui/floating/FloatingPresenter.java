package com.stumi.gobuddies.ui.floating;

import com.stumi.gobuddies.base.RequestAwarePresenter;
import com.stumi.gobuddies.events.BackButtonPressedEvent;
import com.stumi.gobuddies.events.GpsStatusEvent;
import com.stumi.gobuddies.events.HideFloatingView;
import com.stumi.gobuddies.events.HomeButtonEvent;
import com.stumi.gobuddies.events.NetworkError;
import com.stumi.gobuddies.events.NetworkStateChange;
import com.stumi.gobuddies.events.UnauthorizedEvent;
import com.stumi.gobuddies.system.IntentManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

/**
 * Created by sourc on 2017. 02. 27..
 */
public class FloatingPresenter extends RequestAwarePresenter<FloatingView> {

    private IntentManager intentManager;

    private EventBus eventBus;

    @Inject
    public FloatingPresenter(IntentManager intentManager, EventBus eventBus) {
        this.intentManager = intentManager;
        this.eventBus = eventBus;
    }

    @Subscribe
    public void onEvent(UnauthorizedEvent event) {
        intentManager.startRegistrationActivity();
    }

    @Subscribe
    public void onEvent(BackButtonPressedEvent event) {
        getView().onBackButtonPressed();
    }

    @Subscribe
    public void onEvent(HomeButtonEvent event) {
        if (isViewAttached()) {
            getView().closeWindow();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GpsStatusEvent event) {
        if (isViewAttached()) {
            if (!event.isEnabled()) {
                getView().showGpsError();
            } else {
                getView().hideGpsError();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NetworkStateChange event) {
        if (isViewAttached()) {
            if (event.isConnected()) {
                getView().hideNetworkError();
            } else {
                getView().showNetworkError();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NetworkError event) {
        if (isViewAttached()) {
            getView().showNetworkError();
        }
    }


    @Subscribe
    public void onEvent(HideFloatingView hideFloatingView) {
        if (isViewAttached()) {
            getView().closeWindow();
        }
    }

    public void onResume() {
        eventBus.register(this);
        //intentManager.stopBubbleHeads();
    }

    public void onPause() {
        intentManager.startBubbleHeads(false);
        eventBus.unregister(this);
    }

}
