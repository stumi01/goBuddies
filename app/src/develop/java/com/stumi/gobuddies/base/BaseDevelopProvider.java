package com.stumi.gobuddies.base;

import com.stumi.gobuddies.system.helper.MockServer;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * @author stumpfb on 18/09/2016.
 */
public class BaseDevelopProvider {

    protected MockServer mockServer;

    public BaseDevelopProvider(MockServer mockServer) {
        this.mockServer = mockServer;
    }

    protected static <T> Single<Response<T>> createResponse(T model) {
        return Single.just(Response.success(model)).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
