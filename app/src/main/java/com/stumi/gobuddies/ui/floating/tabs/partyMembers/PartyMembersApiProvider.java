package com.stumi.gobuddies.ui.floating.tabs.partyMembers;

import com.stumi.gobuddies.model.BaseResponseModel;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @author stumpfb on 25/08/2016.
 */
public interface PartyMembersApiProvider {

    @GET("pm")
    Single<Response<PartyMembers>> loadTeamMembers();

    @FormUrlEncoded
    @GET("pm")
    Single<Response<PartyMembers>> loadTeamMembers(@Field("id") String partyId);

    @FormUrlEncoded
    @POST("pass")
    Single<Response<BaseResponseModel>> passLead(@Field("id") String userId);

    @FormUrlEncoded
    @POST("kick")
    Single<Response<BaseResponseModel>> kickUser(@Field("id") String userId);

    @FormUrlEncoded
    @POST("rp")
    Single<Response<BaseResponseModel>> renameParty(@Field("name") String name, @Field("team") int team);

    @POST("lp")
    Single<Response<BaseResponseModel>> leaveParty();
}
