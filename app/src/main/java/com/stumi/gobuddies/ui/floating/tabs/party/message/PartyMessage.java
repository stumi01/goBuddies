package com.stumi.gobuddies.ui.floating.tabs.party.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author stumpfb on 29/08/2016.
 *         <p>
 *         sys: This is a system message. (If present, value always 1)
 *         own: This message sent by the current user. (If present, value always 1)
 *         lead: Sender of the message is leader. (If present, value always 1) (not in system message)
 *         name: name of the sender (not in system message)
 *         team: team of the sender (not in system message)
 *         time: elapsed time since the message sent in seconds.
 *         txt: message content (max 120 characters)
 *         </p>
 */
public class PartyMessage {

    @Expose
    @SerializedName("lead")
    private boolean lead;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("team")
    private int team;

    @Expose
    @SerializedName("time")
    private long time;

    @Expose
    @SerializedName("txt")
    private String message;

    @Expose
    @SerializedName("own")
    private boolean me;

    @Expose
    @SerializedName("sys")
    private boolean system;

    @Override
    public String toString() {
        return "PartyMessage{" +
                "lead=" + lead +
                ", name='" + name + '\'' +
                ", team=" + team +
                ", time=" + time +
                ", message='" + message + '\'' +
                ", me=" + me +
                ", system=" + system +
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

        PartyMessage message1 = (PartyMessage) o;

        if (lead != message1.lead) {
            return false;
        }
        if (team != message1.team) {
            return false;
        }
        if (time != message1.time) {
            return false;
        }
        if (me != message1.me) {
            return false;
        }
        if (system != message1.system) {
            return false;
        }
        if (name != null ? !name.equals(message1.name) : message1.name != null) {
            return false;
        }
        return message != null ? message.equals(message1.message) : message1.message == null;

    }

    @Override
    public int hashCode() {
        int result = (lead ? 1 : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + team;
        result = 31 * result + (int) (time ^ (time >>> 32));
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (me ? 1 : 0);
        result = 31 * result + (system ? 1 : 0);
        return result;
    }

    public boolean isLead() {
        return lead;
    }

    public void setLead(boolean lead) {
        this.lead = lead;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isMe() {
        return me;
    }

    public void setMe(boolean me) {
        this.me = me;
    }

    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }
}
