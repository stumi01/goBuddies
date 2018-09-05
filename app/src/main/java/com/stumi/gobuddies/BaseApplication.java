package com.stumi.gobuddies;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import com.stumi.gobuddies.system.GoBuddiesReceiver;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

/**
 * Created by sourc on 2017. 02. 23..
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        filter.addAction(CONNECTIVITY_ACTION);
        BroadcastReceiver mReceiver = new GoBuddiesReceiver();
        registerReceiver(mReceiver, filter);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
