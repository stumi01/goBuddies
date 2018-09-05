package com.stumi.gobuddies.ui.firstStart;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;

/**
 * @author stumpfb on 07/10/2016.
 */
public interface HandshakeAPIProvider {

    @GET("vc")
    Single<Response<ValidationModel>> validation();

}
