package com.stumi.gobuddies.system.helper.scheduler;

import android.util.Log;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * @author stumpfb on 07/09/2016.
 */
public class TickerSubscriber extends ResourceSubscriber<Long> {

    private static final String TAG = TickerSubscriber.class.getSimpleName();

    private final TickerListener tickerListener;

    public TickerSubscriber(TickerListener tickerListener) {
        this.tickerListener = tickerListener;
    }

    @Override
    public void onNext(Long aLong) {
        tickerListener.onNext();
    }

    @Override
    public void onError(Throwable e) {
        Log.d(TAG, "error during scheduled task " + e.getMessage());
    }

    @Override
    public void onComplete() {

    }

    public interface TickerListener {

        void onNext();
    }
}
