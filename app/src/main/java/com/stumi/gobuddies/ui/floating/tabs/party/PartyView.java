package com.stumi.gobuddies.ui.floating.tabs.party;

import com.google.android.gms.maps.model.LatLng;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.stumi.gobuddies.ui.floating.tabs.party.map.UserPosition;
import com.stumi.gobuddies.ui.floating.tabs.party.message.PartyMessage;

import java.util.List;

/**
 * @author stumpfb on 28/08/2016.
 */
public interface PartyView extends MvpLceView<Party> {

    void updateMessages(List<PartyMessage> msgs);

    void hideMap();

    void showMap();

    void setMessages(List<PartyMessage> msgs);

    void showMessageSendError(int errorCode);

    void showMessageSendSuccess();

    void setMapDataForLeader(List<UserPosition> mems, LatLng userLocation);

    void setMapDataForMembers(UserPosition lead, LatLng userLocation);

    void setInputBackground(Integer backgroundResource);

}
