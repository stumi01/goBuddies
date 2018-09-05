package com.stumi.gobuddies.system.location;

import com.stumi.gobuddies.model.BaseResponseModel;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author stumpfb on 14/08/2016.
 */
public interface LocationAPIProvider {

    @FormUrlEncoded
    @POST("lu")
    Single<Response<LocationUpdateModel>> locationUpdate(@Field("lon") Double longitude,
                                                         @Field("lat") Double latitude);

    @FormUrlEncoded
    @POST("sl")
    Single<Response<BaseResponseModel>> notifySleepState(@Field("v") int isSleep);
}
