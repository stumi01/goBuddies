package com.stumi.gobuddies.di;

import android.content.Context;

import com.stumi.gobuddies.system.IntentManager;
import com.stumi.gobuddies.system.LocationManager;
import com.stumi.gobuddies.system.UserDataHolder;
import com.stumi.gobuddies.system.helper.scheduler.Ticker;
import com.stumi.gobuddies.system.location.LocationProvider;
import com.stumi.gobuddies.system.tracking.TrackingListener;
import com.stumi.gobuddies.ui.floating.FloatingComponent;
import com.stumi.gobuddies.ui.floating.FloatingModule;
import com.stumi.gobuddies.ui.floating.settings.SettingsComponent;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Retrofit;

/**
 * @author stumpfb on 24/01/2017.
 */

public interface BaseApplicationComponent {

    LocationProvider locationProvider();

    UserDataHolder userTokenHolder();

    IntentManager intentManager();

    LocationManager locationManager();

    Context context();

    Retrofit retrofit();

    Ticker ticker();

    EventBus eventBus();

    TrackingListener trackingListener();

    FloatingComponent plus(FloatingModule floatingModule);

    SettingsComponent plus();
}
