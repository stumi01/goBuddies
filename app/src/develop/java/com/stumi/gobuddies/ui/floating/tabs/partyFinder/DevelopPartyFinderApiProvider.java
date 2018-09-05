package com.stumi.gobuddies.ui.floating.tabs.partyFinder;

import com.stumi.gobuddies.base.BaseDevelopProvider;
import com.stumi.gobuddies.model.BaseResponseModel;
import com.stumi.gobuddies.system.helper.MockServer;
import com.stumi.gobuddies.ui.floating.tabs.partyFinder.party.PartyDetails;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Field;

/**
 * @author stumpfb on 26/08/2016.
 */
public class DevelopPartyFinderApiProvider extends BaseDevelopProvider implements PartyFinderApiProvider {

    public DevelopPartyFinderApiProvider(MockServer mockServer) {
        super(mockServer);
    }

    @Override
    public Single<Response<List<PartyDetails>>> loadCloseParties() {
        return createResponse(mockServer.getCloseParties());
    }

    @Override
    public Single<Response<List<PartyDetails>>> loadUnseenRequests() {
        return createResponse(mockServer.getUnseenRequest());
    }

    @Override
    public Single<Response<BaseResponseModel>> doJoinRequest(@Field("id") String teamID) {
        return createResponse(mockServer.joinRequest(teamID));
    }

    @Override
    public Single<Response<AcceptJoinResponseModel>> acceptJoinRequest(@Field("id") String teamID) {
        AcceptJoinResponseModel response = new AcceptJoinResponseModel();
        response.setLead(false);
        return createResponse(response);
    }
}
