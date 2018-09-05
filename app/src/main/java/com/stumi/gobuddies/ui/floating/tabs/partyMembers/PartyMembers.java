package com.stumi.gobuddies.ui.floating.tabs.partyMembers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stumi.gobuddies.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author stumpfb on 21/08/2016.
 *         cpl: Current user is the party leader. 0: False, 1: True. If this is True then show "KICK" and "PASS LEADER" buttons next to every member.
 *         lead: Leader of the current party
 *         mems: Array of members
 */
public class PartyMembers {

    @Expose
    @SerializedName("cpl")
    private int isCurrentUserPartyLeader;

    @Expose
    @SerializedName("lead")
    private User lead;

    @Expose
    @SerializedName("mems")
    private List<User> members;

    @Override
    public String toString() {
        return "PartyMembers{" +
                "isCurrentUserPartyLeader=" + isCurrentUserPartyLeader +
                ", lead=" + lead +
                ", members=" + members +
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

        PartyMembers partyMembers = (PartyMembers) o;

        if (isCurrentUserPartyLeader != partyMembers.isCurrentUserPartyLeader) {
            return false;
        }
        if (lead != null ? !lead.equals(partyMembers.lead) : partyMembers.lead != null) {
            return false;
        }
        return members != null ? members.equals(partyMembers.members) : partyMembers.members == null;

    }

    @Override
    public int hashCode() {
        int result = isCurrentUserPartyLeader;
        result = 31 * result + (lead != null ? lead.hashCode() : 0);
        result = 31 * result + (members != null ? members.hashCode() : 0);
        return result;
    }

    public boolean getIsCurrentUserPartyLeader() {
        return isCurrentUserPartyLeader != 0;
    }

    public void setIsCurrentUserPartyLeader(int isCurrentUserPartyLeader) {
        this.isCurrentUserPartyLeader = isCurrentUserPartyLeader;
    }

    public User getLead() {
        return lead;
    }

    public void setLead(User lead) {
        this.lead = lead;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public List<User> getFullPartyInitialized() {
        lead.setLeader(true);
        ArrayList<User> users = new ArrayList<>();
        users.add(lead);
        users.addAll(members);
        return users;
    }
}
