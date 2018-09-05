package com.stumi.gobuddies.ui.floating.settings;

import com.stumi.gobuddies.model.BaseResponseModel;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author stumpfb on 06/09/2016.
 */
public interface SettingsApiProvider {

    @FormUrlEncoded
    @POST("cs")
    Single<Response<BaseResponseModel>> updateSettings(@Field("name") String name, @Field("team") int team);

}
