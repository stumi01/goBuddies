package com.stumi.gobuddies.system.helper;

import com.annimon.stream.Stream;
import com.stumi.gobuddies.model.BaseResponseModel;
import com.stumi.gobuddies.model.User;
import com.stumi.gobuddies.system.location.LocationUpdateModel;
import com.stumi.gobuddies.ui.firstStart.ValidationModel;
import com.stumi.gobuddies.ui.floating.tabs.partyFinder.party.PartyDetails;
import com.stumi.gobuddies.ui.registration.RegistrationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author stumpfb on 24/01/2017.
 */

public class MockServer {

    private User user = new User();

    private List<PartyDetails> closeParties = new ArrayList<>();

    private LocationUpdateModel locationUpdate = new LocationUpdateModel();

    private RegistrationModel registrationModel = new RegistrationModel();

    private ValidationModel validationModel = new ValidationModel();

    private String currentGroupName;

    private List<PartyDetails> unseenRequests = new ArrayList<>();

    public MockServer() {
        user.setTeam(3);


        validationModel.setValid(true);
        validationModel.setVersion(1);

        registrationModel.setOk("1");

        PartyDetails team = new PartyDetails();
        team.setName("Gym battle");
        team.setDistance("82");
        team.setId("Buzik1");
        team.setRequestStatus(1);
        team.setSize(3);
        team.setTeam(3);


        PartyDetails team2 = new PartyDetails();
        team2.setName("Egg hatchers");
        team2.setDistance("145");
        team2.setId("Buzik2");
        team2.setRequestStatus(1);
        team2.setSize(2);
        team2.setTeam(2);

        PartyDetails team3 = new PartyDetails();
        team3.setName("Lucas");
        team3.setDistance("478");
        team3.setId("Buzik2");
        team3.setRequestStatus(1);
        team3.setSize(1);
        team3.setTeam(1);

        closeParties.add(team);
        closeParties.add(team2);
        closeParties.add(team3);

        locationUpdate.setCurrentGroupName("");
        locationUpdate.setGroupMembersCount(2);
        locationUpdate.setGroupsTeam(3);
        locationUpdate.setCurrentGroupName("Egg hatchers");
        locationUpdate.setLead(false);

        unseenRequests.add(team);

    }

    public ValidationModel getValidation() {
        return validationModel;
    }

    public LocationUpdateModel getLocationUpdate() {
        locationUpdate.setNearbyPartiesCount(closeParties.size());
        locationUpdate.setReceivedRequestsCount(1);
        locationUpdate.setUnseenMessagesCount(1);
        return locationUpdate;
    }

    public RegistrationModel registerUser(String name, int team) {
        user.setName(name);
        user.setTeam(team);
        user.setId("1");

        registrationModel.setOk(user.getId());
        return registrationModel;
    }

    public BaseResponseModel updateSettings(String name, int team) {
        user.setName(name);
        user.setTeam(team);
        return new BaseResponseModel();
    }

    public List<PartyDetails> getCloseParties() {
        return closeParties;
    }

    public List<PartyDetails> getUnseenRequest() {
        return unseenRequests;
    }

    public BaseResponseModel joinRequest(String teamID) {
        Stream.of(closeParties).filter(party -> party.getId().equals(teamID)).forEach(partyDetails -> {
            locationUpdate.setGroupMembersCount(partyDetails.getSize());
            locationUpdate.setGroupsTeam(partyDetails.getTeam());
            locationUpdate.setCurrentGroupName(partyDetails.getName());
            locationUpdate.setLead(true);
        });
        return new BaseResponseModel();
    }
}
