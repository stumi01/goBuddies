package com.stumi.gobuddies.system.network.request;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

/**
 * @author stumpfb on 07/09/2016.
 */
public class ObserverRequestExecutor<T> implements SingleObserver<Response<T>>, Cancellable {

    private static final String TAG = ObserverRequestExecutor.class.getSimpleName();

    private static final int SUCCESS = 200;

    private Disposable subscription;

    private final BaseResponseObserverListener<T> listener;

    public ObserverRequestExecutor(Single<Response<T>> requestSingle,
                                   BaseResponseObserverListener<T> listener, Scheduler subscriberThread,
                                   Scheduler observerThread) {
        this.listener = listener;
        requestSingle.subscribeOn(subscriberThread).observeOn(observerThread).subscribe(this);
    }

    @Override
    public void onSubscribe(Disposable d) {
        subscription = d;
    }

    @Override
    public void onSuccess(Response<T> response) {
        if (response.code() == SUCCESS) {
            String message = response.body().toString();
            Crashlytics.log(message);
            Log.d(TAG, message);
            listener.callbackSuccess(response.body());
        } else {
            String message = response.message().concat(" / " + response.code());
            Crashlytics.log(message);
            Log.d(TAG, message);
            listener.callbackError(response.code(), response.message());
        }
    }

    @Override
    public void onError(Throwable e) {
        String message = e.toString();
        Crashlytics.log(message);
        Log.d(TAG, message);
        listener.callbackError(-1, message);
    }

    @Override
    public void cancel() {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }

    public interface BaseResponseObserverListener<T> {

        void callbackError(int errorCode, String message);

        void callbackSuccess(T body);
    }
}
