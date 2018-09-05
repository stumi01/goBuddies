package com.stumi.gobuddies.idlingResources;

import android.app.Activity;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.test.espresso.IdlingResource;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.Collection;

/**
 * Created by sourc on 2017. 02. 16..
 */

public class ViewVisibilityIdlingResource implements IdlingResource {

    private static final int IDLE_POLL_DELAY_MILLIS = 100;
    private final Class<? extends Activity> clazz;
    private final int viewId;

    /**
     * Hold weak reference to the View, so we don't leak memory even if the resource isn't unregistered.
     */
    private WeakReference<View> mView;
    private final int mVisibility;
    private final String mName;

    private ResourceCallback mResourceCallback;

    public ViewVisibilityIdlingResource(final Class<? extends Activity> clazz, @IdRes int viewId, int visibility) {
        this.clazz = clazz;
        this.viewId = viewId;
        mView = new WeakReference<View>(null);
        mVisibility = visibility;
        mName = "View Visibility for view " + viewId + "(@" + System.identityHashCode(mView) + ")";
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public boolean isIdleNow() {
        checkAtvities();
        View view = mView.get();
        final boolean isIdle = view != null && view.getVisibility() == mVisibility;
        if (isIdle) {
            if (mResourceCallback != null) {
                mResourceCallback.onTransitionToIdle();
            }
        } else {
            /* Force a re-check of the idle state in a little while.
             * If isIdleNow() returns false, Espresso only polls it every few seconds which can slow down our tests.
             * Ideally we would watch for the visibility state changing, but AFAIK we can't detect when a View's
             * visibility changes to GONE.
             */
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isIdleNow();
                }
            }, IDLE_POLL_DELAY_MILLIS);
        }
        return isIdle;
    }

    private void checkAtvities() {
        Collection<Activity> activitiesInStage = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
        for (Activity a : activitiesInStage) {
            if (clazz.isAssignableFrom(a.getClass())) {
                mView = new WeakReference<View>(a.findViewById(viewId));
            }
        }
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        mResourceCallback = resourceCallback;
    }
}