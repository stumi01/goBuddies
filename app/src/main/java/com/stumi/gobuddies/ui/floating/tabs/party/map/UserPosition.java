package com.stumi.gobuddies.ui.floating.tabs.party.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author stumpfb on 29/08/2016.
 *         lat: latitude
 *         lon: longitude
 *         name: name of the leader
 */
public class UserPosition {

    @Expose
    @SerializedName("lat")
    private double latitude;

    @Expose
    @SerializedName("lon")
    private double longitude;

    @Expose
    @SerializedName("name")
    private String name;

    public UserPosition() {
    }

    public UserPosition(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "UserPosition{" +
                "lat=" + latitude +
                ", lon=" + longitude +
                ", name='" + name + '\'' +
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

        UserPosition userPosition = (UserPosition) o;

        if (Double.compare(userPosition.latitude, latitude) != 0) {
            return false;
        }
        if (Double.compare(userPosition.longitude, longitude) != 0) {
            return false;
        }
        return name != null ? name.equals(userPosition.name) : userPosition.name == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public double getLat() {
        return latitude;
    }

    public void setLat(double lat) {
        this.latitude = lat;
    }

    public double getLon() {
        return longitude;
    }

    public void setLon(double lon) {
        this.longitude = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static UserPosition Create(String name, LatLng userLocation) {
        return new UserPosition(name, userLocation.latitude, userLocation.longitude);
    }
}
