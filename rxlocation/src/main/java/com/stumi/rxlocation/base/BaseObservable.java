package com.stumi.rxlocation.base;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.stumi.rxlocation.exceptions.GoogleAPIConnectionException;
import com.stumi.rxlocation.exceptions.GoogleAPIConnectionSuspendedException;

import java.util.Arrays;
import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

/**
 * @author stumpfb on 20/01/2017.
 */

public abstract class BaseObservable<T> implements ObservableOnSubscribe<T> {

    private final Context ctx;

    private final List<Api<? extends Api.ApiOptions.NotRequiredOptions>> services;

    @SafeVarargs
    protected BaseObservable(Context ctx, Api<? extends Api.ApiOptions.NotRequiredOptions>... services) {
        this.ctx = ctx;
        this.services = Arrays.asList(services);
    }

    @Override
    public void subscribe(ObservableEmitter<T> observer) {
        final GoogleApiClient apiClient = createApiClient(observer);
        try {
            apiClient.connect();
        } catch (Throwable ex) {
            observer.onError(ex);
        }

        observer.setDisposable(new Disposable() {
            @Override
            public void dispose() {
                if (apiClient.isConnected() || apiClient.isConnecting()) {
                    onUnsubscribed(apiClient);
                    apiClient.disconnect();
                }
            }

            @Override
            public boolean isDisposed() {
                return !apiClient.isConnected();
            }
        });
    }

    protected void onUnsubscribed(GoogleApiClient locationClient) {
    }

    protected GoogleApiClient createApiClient(ObservableEmitter<? super T> subscriber) {

        ApiClientConnectionCallbacks apiClientConnectionCallbacks =
                new ApiClientConnectionCallbacks(subscriber);

        GoogleApiClient.Builder apiClientBuilder = new GoogleApiClient.Builder(ctx);


        for (Api<? extends Api.ApiOptions.NotRequiredOptions> service : services) {
            apiClientBuilder.addApi(service);
        }

        apiClientBuilder.addConnectionCallbacks(apiClientConnectionCallbacks);
        apiClientBuilder.addOnConnectionFailedListener(apiClientConnectionCallbacks);

        GoogleApiClient apiClient = apiClientBuilder.build();

        apiClientConnectionCallbacks.setClient(apiClient);

        return apiClient;

    }

    protected abstract void onGoogleApiClientReady(GoogleApiClient apiClient,
                                                   ObservableEmitter<? super T> observer);

    private class ApiClientConnectionCallbacks
            implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

        final private ObservableEmitter<? super T> observer;

        private GoogleApiClient apiClient;

        private ApiClientConnectionCallbacks(ObservableEmitter<? super T> observer) {
            this.observer = observer;
        }

        @Override
        public void onConnected(Bundle bundle) {
            try {
                onGoogleApiClientReady(apiClient, observer);
            } catch (Throwable ex) {
                observer.onError(ex);
            }
        }

        @Override
        public void onConnectionSuspended(int cause) {
            observer.onError(new GoogleAPIConnectionSuspendedException(cause));
        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            observer.onError(new GoogleAPIConnectionException("Error connecting to GoogleApiClient.",
                    connectionResult));
        }

        public void setClient(GoogleApiClient client) {
            this.apiClient = client;
        }
    }
}
