package com.stumi.gobuddies.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.stumi.gobuddies.events.HomeButtonEvent;
import com.stumi.gobuddies.events.LocationUpdate;
import com.stumi.gobuddies.events.NetworkStateChange;
import com.stumi.gobuddies.events.ScreenStateChange;

import org.greenrobot.eventbus.EventBus;

import static android.content.Intent.ACTION_CLOSE_SYSTEM_DIALOGS;
import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;
import static android.net.ConnectivityManager.EXTRA_NO_CONNECTIVITY;
import static com.stumi.gobuddies.system.LocationManager.LOCATION_UPDATE_ACTION;

/**
 * Created by sourc on 2017. 02. 23..
 */
public class GoBuddiesReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("GoBuddiesReceiver", "onReceive " + intent.getAction());
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            EventBus.getDefault().post(new ScreenStateChange(false));
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            EventBus.getDefault().post(new ScreenStateChange(true));
        } else if (intent.getAction().equals(ACTION_CLOSE_SYSTEM_DIALOGS)) {
            EventBus.getDefault().post(new HomeButtonEvent());
        } else if (intent.getAction().equals(CONNECTIVITY_ACTION)) {
            if (intent.getBooleanExtra(EXTRA_NO_CONNECTIVITY, false)) {
                EventBus.getDefault().post(new NetworkStateChange(false));
            } else {
                EventBus.getDefault().post(new NetworkStateChange(true));
            }
        } else if (intent.getAction().equals(LOCATION_UPDATE_ACTION)) {
            EventBus.getDefault().post(new LocationUpdate());
        }
    }

}