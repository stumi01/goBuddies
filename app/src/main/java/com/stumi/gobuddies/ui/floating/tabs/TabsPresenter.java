package com.stumi.gobuddies.ui.floating.tabs;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.stumi.gobuddies.events.SwitchToFragment;
import com.stumi.gobuddies.events.UserKickedFromPartyEvent;
import com.stumi.gobuddies.system.LocationManager;
import com.stumi.gobuddies.system.location.LocationUpdateModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by sourc on 2016. 09. 10..
 */
public class TabsPresenter extends MvpBasePresenter<TabsView> implements Consumer<LocationUpdateModel> {

    private final EventBus eventBus;

    private final LocationManager locationManager;

    private LocationUpdateModel lastLocationUpdateModel;

    private Disposable subscription;

    @Inject
    public TabsPresenter(EventBus eventBus, LocationManager locationManager) {
        this.eventBus = eventBus;
        this.locationManager = locationManager;
    }

    public void onResume() {
        eventBus.register(this);
        subscription = locationManager.subscribe(this);
    }

    public void onPause() {
        eventBus.unregister(this);
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }

    @Subscribe
    public void onEvent(SwitchToFragment switchToFragment) {
        getView().switchToFragment(switchToFragment);
    }

    @Subscribe
    public void onEvent(UserKickedFromPartyEvent event) {
        if (isViewAttached()) {
            getView().disablePartyTabs();
            getView().showKickedMessage();
        }
    }

    @Override
    public void accept(LocationUpdateModel locationUpdateModel) throws Exception {
        if (!locationUpdateModel.equals(this.lastLocationUpdateModel)) {
            this.lastLocationUpdateModel = locationUpdateModel;
            if (isViewAttached()) {
                if (locationUpdateModel.getGroupMembersCount() > 1) {
                    getView().enablePartyTabs();
                } else {
                    getView().disablePartyTabs();
                }
            }
        }
    }
}
