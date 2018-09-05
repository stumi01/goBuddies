package com.stumi.gobuddies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author stumpfb on 20/08/2016.
 */
public class BaseResponseModel {

    @Expose
    @SerializedName("ok")
    private boolean ok;

    public boolean getOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BaseResponseModel that = (BaseResponseModel) o;

        return ok == that.ok;

    }

    @Override
    public int hashCode() {
        return (ok ? 1 : 0);
    }

    @Override
    public String toString() {
        return "BaseResponseModel{" +
                "ok=" + ok +
                '}';
    }
}
