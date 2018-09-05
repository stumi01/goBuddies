package com.stumi.gobuddies.ui.registration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author stumpfb on 31/07/2016.
 */
public class RegistrationModel {

    @Expose
    @SerializedName("ok")
    private String ok;

    public String getOk() {
        return ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }

    @Override
    public String toString() {
        return "RegistrationModel{" +
                "ok='" + ok + '\'' +
                '}';
    }
}
