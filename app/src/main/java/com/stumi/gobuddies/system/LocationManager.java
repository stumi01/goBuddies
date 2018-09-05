package com.stumi.gobuddies.system;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.jakewharton.rxrelay2.BehaviorRelay;
import com.stumi.gobuddies.events.LocationUpdate;
import com.stumi.gobuddies.events.ScreenStateChange;
import com.stumi.gobuddies.system.helper.scheduler.Ticker;
import com.stumi.gobuddies.system.helper.scheduler.TickerImmediateStartSubscriber;
import com.stumi.gobuddies.system.location.LocationAPIProvider;
import com.stumi.gobuddies.system.location.LocationProvider;
import com.stumi.gobuddies.system.location.LocationUpdateListener;
import com.stumi.gobuddies.system.location.LocationUpdateModel;
import com.stumi.gobuddies.system.network.ExecutorDispatcher;
import com.stumi.gobuddies.system.network.request.ObserverRequestExecutor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static android.content.Context.ALARM_SERVICE;

/**
 * @author stumpfb on 26/08/2016.
 */
public class LocationManager implements LocationUpdateListener,
        ObserverRequestExecutor.BaseResponseObserverListener<LocationUpdateModel> {

    public static final String LOCATION_UPDATE_ACTION = "com.stumi.gobuddies.system.action.LOCATION_UPDATE";

    private static final String TAG = LocationManager.class.getSimpleName();
    private static final int UPDATE_IN_SECONDS = 8;
    private static final int BACKGROUND_UPDATE_IN_SECONDS = 20;

    private final Ticker ticker;

    private final LocationProvider locationProvider;

    private final LocationAPIProvider locationAPIProvider;

    private ExecutorDispatcher executorDispatcher;

    private final UserDataHolder userDataHolder;
    private Context context;

    private LatLng userLocation;

    final BehaviorRelay<LocationUpdateModel> objectBehaviorRelay;

    private AtomicInteger subscriptions;
    private PendingIntent currentAlarm;

    @Inject
    public LocationManager(LocationProvider locationProvider, LocationAPIProvider locationAPIProvider,
                           ExecutorDispatcher executorDispatcher, UserDataHolder userDataHolder,
                           Context context) {
        this.locationProvider = locationProvider;
        this.locationAPIProvider = locationAPIProvider;
        this.executorDispatcher = executorDispatcher;
        this.userDataHolder = userDataHolder;
        this.context = context;
        ticker = new Ticker();
        objectBehaviorRelay = BehaviorRelay.create();
        subscriptions = new AtomicInteger(0);
    }

    public Disposable subscribe(Consumer<? super LocationUpdateModel> onNext) {
        subscriptions.incrementAndGet();
        startIfNecessary();
        final Disposable disposable = objectBehaviorRelay.subscribe(onNext);
        return new Disposable() {
            @Override
            public void dispose() {
                subscriptions.decrementAndGet();
                stopIfNecessary();
                disposable.dispose();
            }

            @Override
            public boolean isDisposed() {
                return disposable.isDisposed();
            }
        };
    }

    private void startIfNecessary() {
        if (subscriptions.get() == 1) {
            startUpdate();
        }
    }

    private void stopIfNecessary() {
        if (subscriptions.get() == 0) {
            stopUpdate();
        }
    }

    private void startUpdate() {
        EventBus.getDefault().register(this);
        locationProvider.start(this);
        restartTicker(UPDATE_IN_SECONDS);
    }

    private void stopUpdate() {
        locationProvider.stop();
        ticker.stop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(ScreenStateChange event) {
        if (event.isScreenOn()) {
            locationProvider.start(this);
            restartTicker(UPDATE_IN_SECONDS);
            stopAlarmIfNecceseary();
        } else {
            locationProvider.stop();
            ticker.stop();
            startAlarm();
        }
        notifyServerForScreenState(event.isScreenOn());
    }

    private void notifyServerForScreenState(boolean screenOn) {
        executorDispatcher.executeOnBackground(locationAPIProvider.notifySleepState(screenOn ? 0 : 1));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(LocationUpdate locationUpdate) {
        updateWithLastLocation();
    }

    private void startAlarm() {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, GoBuddiesReceiver.class);
        intent.setAction(LOCATION_UPDATE_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                1000 * 40,
                1000 * 60,
                pendingIntent);
        currentAlarm = pendingIntent;
    }

    private void stopAlarmIfNecceseary() {
        if (currentAlarm != null) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager.cancel(currentAlarm);
            currentAlarm = null;
        }
    }



    private void restartTicker(int updateInSeconds) {
        ticker.stop();
        ticker.start(updateInSeconds, new TickerImmediateStartSubscriber(this::updateWithLastLocation));
    }

    public LatLng getUserLocation() {
        return userLocation;
    }

    @Override
    public void update(double longitude, double latitude) {
        userLocation = new LatLng(latitude, longitude);
        Log.d(TAG, "location update arrived " + userLocation.toString());
    }

    private void updateWithLastLocation() {
        if (userLocation != null) {
            executorDispatcher.executeForMainThread(locationAPIProvider.
                    locationUpdate(userLocation.longitude, userLocation.latitude), this);
        }
    }


    @Override
    public void callbackSuccess(LocationUpdateModel locationUpdateModel) {
        Log.d(TAG, "location callback");
        userDataHolder
                .updateSelf(locationUpdateModel.isLead(), locationUpdateModel.getGroupMembersCount(), locationUpdateModel.getCurrentGroupName());
        objectBehaviorRelay.accept(locationUpdateModel);
    }

    @Override
    public void callbackError(int errorCode, String message) {
        Log.d(TAG, String.format("location callback error %d %s", errorCode, message));
    }
}
