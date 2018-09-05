package com.stumi.gobuddies.ui.floating.tabs.partyMembers;

import com.stumi.gobuddies.base.BaseDevelopProvider;
import com.stumi.gobuddies.model.BaseResponseModel;
import com.stumi.gobuddies.model.User;
import com.stumi.gobuddies.system.helper.MockServer;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.Query;

/**
 * @author stumpfb on 26/08/2016.
 */
public class DevelopPartyMembersApiProvider extends BaseDevelopProvider implements PartyMembersApiProvider {

    public DevelopPartyMembersApiProvider(MockServer mockServer) {
        super(mockServer);
    }

    @Override
    public Single<Response<PartyMembers>> loadTeamMembers() {
        PartyMembers partyMembers = new PartyMembers();
        partyMembers.setIsCurrentUserPartyLeader(0);
        User leader = new User();
        leader.setId("1");
        leader.setName("Lorenn");
        leader.setTeam(1);
        User member = new User();
        member.setId("2");
        member.setName("Marc");
        member.setTeam(1);
        User member2 = new User();
        member2.setId("3");
        member2.setName("Thomas");
        member2.setTeam(2);
        List<User> members = Arrays.asList(member, member2);
        partyMembers.setLead(leader);
        partyMembers.setMembers(members);
        return createResponse(partyMembers);
    }

    @Override
    public Single<Response<PartyMembers>> loadTeamMembers(@Query("id") String partyId) {
        return loadTeamMembers();
    }

    @Override
    public Single<Response<BaseResponseModel>> passLead(@Field("id") String text) {
        return createResponse(new BaseResponseModel());
    }

    @Override
    public Single<Response<BaseResponseModel>> kickUser(@Field("id") String text) {
        return createResponse(new BaseResponseModel());
    }

    @Override
    public Single<Response<BaseResponseModel>> renameParty(@Field("name") String newName,
                                                           @Field("team") int team) {
        return createResponse(new BaseResponseModel());
    }

    @Override
    public Single<Response<BaseResponseModel>> leaveParty() {
        return createResponse(new BaseResponseModel());
    }
}
