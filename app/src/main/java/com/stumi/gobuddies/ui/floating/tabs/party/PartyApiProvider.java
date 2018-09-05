package com.stumi.gobuddies.ui.floating.tabs.party;

import com.stumi.gobuddies.model.BaseResponseModel;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @author stumpfb on 28/08/2016.
 */
public interface PartyApiProvider {

    @GET("pp")
    Single<Response<Party>> loadAllPreviousMessages();

    @GET("ppu")
    Single<Response<Party>> loadUnreadMessages();

    @FormUrlEncoded
    @POST("msg")
    Single<Response<BaseResponseModel>> sendMessage(@Field("msg") String text);
}
