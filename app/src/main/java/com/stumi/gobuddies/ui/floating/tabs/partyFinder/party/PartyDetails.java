package com.stumi.gobuddies.ui.floating.tabs.partyFinder.party;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * id: id for sending/accepting requests (base64 encoded mongo objectid)
 * dist: distance in meters
 * name: name of the team (or individual)
 * size: number of member is the party
 * team: 0: Mixed/No team 1: Instinct (Yellow) 2: Mystic (Blue) 3: Valor (Red)
 * r_st: Request Status: 0: Not available (if user is not the leader) 1: Request available 2: Request already sent 3: Request acceptable
 *
 * @author stumpfb on 31/07/2016.
 */
public class PartyDetails {

    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("dist")
    private String distance;

    @Expose
    @SerializedName("size")
    private int size;

    @Expose
    @SerializedName("team")
    private int team;

    @Expose
    @SerializedName("r_st")
    private int requestStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public int getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(int requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PartyDetails partyDetails1 = (PartyDetails) o;

        if (size != partyDetails1.size) {
            return false;
        }
        if (team != partyDetails1.team) {
            return false;
        }
        if (requestStatus != partyDetails1.requestStatus) {
            return false;
        }
        if (id != null ? !id.equals(partyDetails1.id) : partyDetails1.id != null) {
            return false;
        }
        if (name != null ? !name.equals(partyDetails1.name) : partyDetails1.name != null) {
            return false;
        }
        return distance != null ? distance.equals(partyDetails1.distance) : partyDetails1.distance == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (distance != null ? distance.hashCode() : 0);
        result = 31 * result + size;
        result = 31 * result + team;
        result = 31 * result + requestStatus;
        return result;
    }

    @Override
    public String toString() {
        return "PartyDetails{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", distance='" + distance + '\'' +
                ", size=" + size +
                ", team=" + team +
                ", requestStatus=" + requestStatus +
                '}';
    }

    public PartyRequestStatus getTeamRequestStatus() {
        switch (requestStatus) {
            case 0:
                return PartyRequestStatus.NotAvailable;
            case 1:
                return PartyRequestStatus.RequestAvailable;
            case 2:
                return PartyRequestStatus.RequestAlreadySent;
            case 3:
                return PartyRequestStatus.RequestAcceptable;
            default:
                return PartyRequestStatus.NotAvailable;
        }
    }
}
