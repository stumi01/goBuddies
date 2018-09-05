package com.stumi.gobuddies.system.network;

import com.stumi.gobuddies.model.BaseResponseModel;
import com.stumi.gobuddies.system.network.request.Cancellable;
import com.stumi.gobuddies.system.network.request.ObserverRequestExecutor;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * @author stumpfb on 27/12/2016.
 */

public class ExecutorDispatcher {

    @Inject
    public ExecutorDispatcher() {
    }

    public <T> Cancellable executeForMainThread(Single<Response<T>> responseSingle,
                                                ObserverRequestExecutor.BaseResponseObserverListener<T> registrationPresenter) {
        return new ObserverRequestExecutor<>(responseSingle, registrationPresenter, Schedulers.newThread(),
                AndroidSchedulers.mainThread());
    }

    public void executeOnBackground(Single<Response<BaseResponseModel>> responseSingle) {
        responseSingle.subscribeOn(Schedulers.computation()).observeOn(Schedulers.computation()).subscribe();
    }
}
