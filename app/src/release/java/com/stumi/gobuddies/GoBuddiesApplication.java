package com.stumi.gobuddies;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.stumi.gobuddies.di.ApplicationComponent;
import com.stumi.gobuddies.di.ApplicationModule;
import com.stumi.gobuddies.di.DaggerApplicationComponent;
import com.stumi.gobuddies.ui.floating.FloatingComponent;
import com.stumi.gobuddies.ui.floating.FloatingModule;

import io.fabric.sdk.android.Fabric;

/**
 * @author stumpfb on 29/07/2016.
 */
public class GoBuddiesApplication extends BaseApplication {

    private ApplicationComponent applicationComponent;

    private FloatingComponent floatingComponent;

    public static GoBuddiesApplication get(Context context) {
        return (GoBuddiesApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        applicationComponent =
                DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
    }

    public FloatingComponent createFloatingComponent() {
        floatingComponent = applicationComponent.plus(new FloatingModule());
        return floatingComponent;
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public FloatingComponent getFloatingComponent() {
        return floatingComponent;
    }

    public void releaseFloatingComponent() {
        floatingComponent = null;
    }
}
