package com.stumi.gobuddies.ui.floating.tabs.partyFinder;

import com.stumi.gobuddies.model.BaseResponseModel;
import com.stumi.gobuddies.ui.floating.tabs.partyFinder.party.PartyDetails;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @author stumpfb on 31/07/2016.
 */
public interface PartyFinderApiProvider {

    @GET("np")
    Single<Response<List<PartyDetails>>> loadCloseParties();

    @GET("ur")
    Single<Response<List<PartyDetails>>> loadUnseenRequests();

    @FormUrlEncoded
    @POST("req")
    Single<Response<BaseResponseModel>> doJoinRequest(@Field("id") String partyID);

    @FormUrlEncoded
    @POST("acc")
    Single<Response<AcceptJoinResponseModel>> acceptJoinRequest(@Field("id") String partyID);
}
