package com.stumi.gobuddies.system.helper.scheduler;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * @author stumpfb on 26/08/2016.
 */
public class Ticker {

    private ResourceSubscriber<Long> subscriber;

    public void stop() {
        if (subscriber != null && !subscriber.isDisposed()) {
            subscriber.dispose();
        }
    }

    public void start(int seconds, ResourceSubscriber<Long> subscriber) {
        stop();
        Flowable<Long> ticker =
                Flowable.interval(seconds, TimeUnit.SECONDS).subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread());
        this.subscriber = subscriber;
        ticker.subscribe(subscriber);
    }

}
