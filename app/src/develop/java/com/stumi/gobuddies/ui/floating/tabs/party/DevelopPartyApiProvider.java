package com.stumi.gobuddies.ui.floating.tabs.party;

import com.stumi.gobuddies.base.BaseDevelopProvider;
import com.stumi.gobuddies.model.BaseResponseModel;
import com.stumi.gobuddies.system.helper.MockServer;
import com.stumi.gobuddies.ui.floating.tabs.party.map.UserPosition;
import com.stumi.gobuddies.ui.floating.tabs.party.message.PartyMessage;
import com.stumi.gobuddies.ui.floating.tabs.partyFinder.party.PartyDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Field;

/**
 * @author stumpfb on 28/08/2016.
 */
public class DevelopPartyApiProvider extends BaseDevelopProvider implements PartyApiProvider {

    public DevelopPartyApiProvider(MockServer mockServer) {
        super(mockServer);
    }

    @Override
    public Single<Response<Party>> loadAllPreviousMessages() {
        Party party = new Party();

        PartyDetails partyDetails = new PartyDetails();
        partyDetails.setDistance("1");
        partyDetails.setId("1");
        partyDetails.setName("Egg Hatchers");
        partyDetails.setRequestStatus(1);
        partyDetails.setSize(3);
        partyDetails.setTeam(1);
        party.setPartyDetails(partyDetails);

 /*       UserPosition map = new UserPosition();
        map.setName("Marc");
        map.setLat(47.5011151657);
        map.setLon(19.0531965145);
        party.setLeaderPosition(map);*/

        UserPosition map2 = new UserPosition();
        map2.setName("Lorenn");
        map2.setLat(47.47189142);
        map2.setLon(19.05376653);
        party.setLeaderPosition(map2);

        List<PartyMessage> messages = new ArrayList<>();
        PartyMessage message0 = new PartyMessage();
        message0.setSystem(true);
        message0.setTime(8);
        message0.setMessage("Server has crashed!");
        messages.add(message0);


/*        PartyMessage message11 = new PartyMessage();
        message11.setTeam(1);
        message11.setTime(8);
        message11.setMe(true);
        message11.setMessage("own shit");
        messages.add(message11);*/

        PartyMessage message1 = new PartyMessage();
        message1.setName("Marc");
        message1.setTeam(2);
        message1.setTime(8);
        message1.setMessage("Welcome here!");
        messages.add(message1);

        PartyMessage message2 = new PartyMessage();
        message2.setName("Lorenn");
        message2.setTeam(2);
        message2.setLead(true);
        message2.setTime(4);
        message2.setMessage("Hi :) Can we meet at the square?");
        messages.add(message2);


        PartyMessage message3 = new PartyMessage();
        //message3.setName("Lorenni");
        message3.setTeam(3);
        message3.setLead(false);
        message3.setTime(0);
        message3.setMe(true);
        message3.setMessage("Hi! Sure, Im on my way");
        messages.add(message3);

        party.setMessages(messages);

        party.setMessages(messages);
        return createResponse(party);
    }

    @Override
    public Single<Response<Party>> loadUnreadMessages() {
        Party party = new Party();

        PartyDetails partyDetails = new PartyDetails();
        partyDetails.setDistance("1");
        partyDetails.setId("1");
        partyDetails.setName("Egg Hatchers");
        partyDetails.setRequestStatus(1);
        partyDetails.setSize(4);
        partyDetails.setTeam(1);
        party.setPartyDetails(partyDetails);

        UserPosition map = new UserPosition();
        map.setName("Lorenn");
        map.setLat(47.47189142);
        map.setLon(19.05376653);
        party.setLeaderPosition(map);

      /*  UserPosition map2 = new UserPosition();
        map.setName("Lorenn");
        map.setLat(47.5021151657);
        map.setLon(19.0531965145);
        party.setMemberPositions(Arrays.asList(map, map2));*/

        List<PartyMessage> messages = new ArrayList<>();
   /*     PartyMessage message1 = new PartyMessage();
        message1.setName("Buzi Gyuri");
        message1.setTeam(1);
        message1.setLead(true);
        message1.setTime(1);
        message1.setMessage(getRandomMessage());
        messages.add(message1);*/

        party.setMessages(messages);
        return createResponse(party);
    }

    private String getRandomMessage() {
        if (new Random().nextBoolean()) {
            return "ping";
        } else {
            return "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                    "sed do eiusmod tempor incididunt ut labore et.";
        }
    }

    @Override
    public Single<Response<BaseResponseModel>> sendMessage(@Field("msg") String text) {
        return createResponse(new BaseResponseModel());
    }
}
