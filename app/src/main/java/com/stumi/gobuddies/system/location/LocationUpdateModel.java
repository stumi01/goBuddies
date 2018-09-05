package com.stumi.gobuddies.system.location;

import com.google.gson.annotations.SerializedName;

/**
 * @author stumpfb on 14/08/2016.
 *         near: number of nearby parties [0, 99]
 *         reqs: number of request received [0, 99]
 *         mess: number of unseen messages [0, 99]
 *         lead: is this user the leader (0: False, 1: True)
 *         gn: current group name
 *         gmc: current group member count
 *         gt: current group's team (0: No PartyDetails/Mixed, 1: Instinct (Yellow) 2: Mystic (Blue) 3: Valor (Red))
 */
public class LocationUpdateModel {

    @SerializedName("near")
    private int nearbyPartiesCount;

    @SerializedName("reqs")
    private int receivedRequestsCount;

    @SerializedName("mess")
    private int unseenMessagesCount;

    @SerializedName("lead")
    private boolean lead;

    @SerializedName("gn")
    private String currentGroupName;

    @SerializedName("gmc")
    private int groupMembersCount;

    @SerializedName("gt")
    private int groupsTeam;

    @Override
    public String toString() {
        return "LocationUpdateModel{" +
                "nearbyPartiesCount=" + nearbyPartiesCount +
                ", receivedRequestsCount=" + receivedRequestsCount +
                ", unseenMessagesCount=" + unseenMessagesCount +
                ", lead=" + lead +
                ", currentGroupName='" + currentGroupName + '\'' +
                ", groupMembersCount=" + groupMembersCount +
                ", groupsTeam=" + groupsTeam +
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

        LocationUpdateModel that = (LocationUpdateModel) o;

        if (nearbyPartiesCount != that.nearbyPartiesCount) {
            return false;
        }
        if (receivedRequestsCount != that.receivedRequestsCount) {
            return false;
        }
        if (unseenMessagesCount != that.unseenMessagesCount) {
            return false;
        }
        if (lead != that.lead) {
            return false;
        }
        if (groupMembersCount != that.groupMembersCount) {
            return false;
        }
        if (groupsTeam != that.groupsTeam) {
            return false;
        }
        return currentGroupName != null ? currentGroupName
                .equals(that.currentGroupName) : that.currentGroupName == null;

    }

    @Override
    public int hashCode() {
        int result = nearbyPartiesCount;
        result = 31 * result + receivedRequestsCount;
        result = 31 * result + unseenMessagesCount;
        result = 31 * result + (lead ? 1 : 0);
        result = 31 * result + (currentGroupName != null ? currentGroupName.hashCode() : 0);
        result = 31 * result + groupMembersCount;
        result = 31 * result + groupsTeam;
        return result;
    }

    public int getNearbyPartiesCount() {
        return nearbyPartiesCount;
    }

    public void setNearbyPartiesCount(int nearbyPartiesCount) {
        this.nearbyPartiesCount = nearbyPartiesCount;
    }

    public int getReceivedRequestsCount() {
        return receivedRequestsCount;
    }

    public void setReceivedRequestsCount(int receivedRequestsCount) {
        this.receivedRequestsCount = receivedRequestsCount;
    }

    public int getUnseenMessagesCount() {
        return unseenMessagesCount;
    }

    public void setUnseenMessagesCount(int unseenMessagesCount) {
        this.unseenMessagesCount = unseenMessagesCount;
    }

    public String getCurrentGroupName() {
        return currentGroupName;
    }

    public void setCurrentGroupName(String currentGroupName) {
        this.currentGroupName = currentGroupName;
    }

    public int getGroupMembersCount() {
        return groupMembersCount;
    }

    public void setGroupMembersCount(int gnc) {
        this.groupMembersCount = gnc;
    }

    public void setLead(boolean lead) {
        this.lead = lead;
    }

    public boolean isLead() {
        return lead;
    }

    public int getGroupsTeam() {
        return groupsTeam;
    }

    public void setGroupsTeam(int groupsTeam) {
        this.groupsTeam = groupsTeam;
    }
}
