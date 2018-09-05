package com.stumi.gobuddies.ui.registration;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author stumpfb on 31/07/2016.
 */
public interface RegistrationAPIProvider {

    @FormUrlEncoded
    @POST("reg")
    Single<Response<RegistrationModel>> registerUser(@Field("name") String name, @Field("team") int team);
}
