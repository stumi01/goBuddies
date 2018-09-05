package com.stumi.gobuddies.ui.firstStart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author stumpfb on 07/10/2016.
 */
public class ValidationModel {

    @Expose
    @SerializedName("valid")
    private boolean valid;

    @Expose
    @SerializedName("ver")
    private int version;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
