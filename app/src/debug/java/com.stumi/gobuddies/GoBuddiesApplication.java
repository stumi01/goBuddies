package com.stumi.gobuddies;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
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

    private RefWatcher refWatcher;

    private ApplicationComponent applicationComponent;

    private FloatingComponent floatingComponent;

    public static GoBuddiesApplication get(Context context) {
        return (GoBuddiesApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        /*refWatcher = LeakCanary.refWatcher(this).listenerServiceClass(LeakSlackUploadService.class)
                .buildAndInstall();*/

        Fabric.with(this, new Crashlytics());
        applicationComponent =
                DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();

        applicationComponent.trackingListener().startTracking();
    }

    @Override
    public void onTerminate() {
        applicationComponent.trackingListener().stopTracking();
        super.onTerminate();
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
