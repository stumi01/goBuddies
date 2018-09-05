package com.stumi.gobuddies.ui.floating.tabs.party;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stumi.gobuddies.ui.floating.tabs.party.map.UserPosition;
import com.stumi.gobuddies.ui.floating.tabs.party.message.PartyMessage;
import com.stumi.gobuddies.ui.floating.tabs.partyFinder.party.PartyDetails;

import java.util.Collections;
import java.util.List;

/**
 * @author stumpfb on 28/08/2016.
 *         map: location of the group leader
 *         lat: latitude
 *         lon: longitude
 *         name: name of the leader
 *         group:
 *         name: name of the current group (leader can change it)
 *         count: number of people in this group
 *         msgs: List of chat messages (Same as above)
 */
public class Party {

    @Expose
    @SerializedName("lead")
    private UserPosition leaderPosition;

    @Expose
    @SerializedName("mems")
    private List<UserPosition> memberPositions;

    @Expose
    @SerializedName("group")
    private PartyDetails partyDetails;

    @Expose
    @SerializedName("msgs")
    private List<PartyMessage> messages;

    @Override
    public String toString() {
        return "PartyDetails{" +
                "leaderPosition=" + leaderPosition +
                ", memberPositions=" + memberPositions +
                ", partyDetails=" + partyDetails +
                ", messages=" + messages +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Party party = (Party) o;

        if (leaderPosition != null ? !leaderPosition
                .equals(party.leaderPosition) : party.leaderPosition != null) {
            return false;
        }
        if (memberPositions != null ? !memberPositions
                .equals(party.memberPositions) : party.memberPositions != null) {
            return false;
        }
        if (this.partyDetails != null ? !this.partyDetails
                .equals(party.partyDetails) : party.partyDetails != null) {
            return false;
        }
        return messages != null ? messages.equals(party.messages) : party.messages == null;

    }

    @Override
    public int hashCode() {
        int result = leaderPosition != null ? leaderPosition.hashCode() : 0;
        result = 31 * result + (memberPositions != null ? memberPositions.hashCode() : 0);
        result = 31 * result + (partyDetails != null ? partyDetails.hashCode() : 0);
        result = 31 * result + (messages != null ? messages.hashCode() : 0);
        return result;
    }

    public UserPosition getLeaderPosition() {
        return leaderPosition;
    }

    public void setLeaderPosition(UserPosition leaderPosition) {
        this.leaderPosition = leaderPosition;
    }

    public List<UserPosition> getMemberPositions() {
        if (memberPositions == null) {
            return Collections.EMPTY_LIST;
        }
        return memberPositions;
    }

    public void setMemberPositions(List<UserPosition> memberPositions) {
        this.memberPositions = memberPositions;
    }

    public PartyDetails getPartyDetails() {
        return partyDetails;
    }

    public void setPartyDetails(PartyDetails partyDetails) {
        this.partyDetails = partyDetails;
    }

    public List<PartyMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<PartyMessage> messages) {
        this.messages = messages;
    }
}
