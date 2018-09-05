package com.stumi.gobuddies.ui.floating.tabs.party.map;

import android.content.Context;
import android.support.v4.util.Pair;
import android.util.AttributeSet;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.stumi.gobuddies.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author stumpfb on 03/09/2016.
 */
public class PartyMapView extends MapView
        implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener {

    private static final Pair<Float, Float> MEMBER_ICON_ANCHOR = new Pair<>(.5f, 1f);

    private static final Pair<Float, Float> LEADER_ICON_ANCHOR = new Pair<>(.5f, 1f);

    private static final Pair<Float, Float> USER_ICON_ANCHOR = new Pair<>(.5f, .5f);

    private GoogleMap mMap;

    private BitmapDescriptor memberIcon;

    private BitmapDescriptor leaderIcon;

    private BitmapDescriptor userIcon;

    private final List<Marker> markers = new ArrayList<>();

    private boolean isCameraReady = false;

    private Integer markersPadding;

    public PartyMapView(Context context) {
        super(context);
    }

    public PartyMapView(Context var1, AttributeSet var2) {
        super(var1, var2);
    }

    public PartyMapView(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);

    }

    public PartyMapView(Context var1, GoogleMapOptions var2) {
        super(var1, var2);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.onCreate(null);
        if (checkIfGoogleMapsAvailable()) {
            getMapAsync(this);
        } else {
            showNoGooglePlayError();
        }
        onResume();
    }

    @Override
    protected void onDetachedFromWindow() {
        onPause();
        super.onDetachedFromWindow();
    }

    @Override
    public void onCameraIdle() {
        isCameraReady = true;
    }

    private void showNoGooglePlayError() {
        //TODO
    }

    private boolean checkIfGoogleMapsAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        int status = googleApiAvailability.isGooglePlayServicesAvailable(getContext());

        if (status != ConnectionResult.SUCCESS) {
            int requestCode = 10;
           /* Dialog dialog = googleApiAvailability.getErrorDialog(getContext(), status, requestCode);
            dialog.show();*/
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        memberIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_person_pin_circle_black_24dp);
        leaderIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_person_pin_circle_black_24dp_crown);
        userIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_my_location_black_24dp);
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        MapsInitializer.initialize(getContext());
        mMap.setOnCameraIdleListener(this);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    public void setMapDataForMembers(UserPosition leader, LatLng userLocation) {
        markers.clear();
        mMap.clear();

        setUserPosition(leader.getLat(), leader.getLon(), leader.getName(), leaderIcon, LEADER_ICON_ANCHOR);
        setSelfPosition(userLocation);
        zoomCameraToShowEveryone();
    }

    public void setMapDataForLeader(List<UserPosition> members, LatLng selfLocation) {
        markers.clear();
        mMap.clear();
        for (UserPosition up : members) {
            setUserPosition(up.getLat(), up.getLon(), up.getName(), memberIcon, MEMBER_ICON_ANCHOR);
        }
        setSelfPosition(selfLocation);
        zoomCameraToShowEveryone();
    }

    private void setUserPosition(double lat, double lon, String name, BitmapDescriptor icon,
                                 Pair<Float, Float> anchor) {
        LatLng position = new LatLng(lat, lon);
        markers.add(createMarker(name, position, icon, anchor));
    }

    private Marker createMarker(String name, LatLng position, BitmapDescriptor icon,
                                Pair<Float, Float> anchor) {
        MarkerOptions maker = new MarkerOptions().position(position).title(name).icon(icon);
        maker.anchor(anchor.first, anchor.second);
        return mMap.addMarker(maker);
    }

    private void setSelfPosition(LatLng userLocation) {
        markers.add(createMarker("you", userLocation, userIcon, USER_ICON_ANCHOR));
    }

    private void zoomCameraToShowEveryone() {
        if (isCameraReady) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : markers) {
                builder.include(marker.getPosition());
            }
            LatLngBounds bounds = builder.build();
            int padding = getPadding(); // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mMap.animateCamera(cu);
        }
    }

    private int getPadding() {
        if (markersPadding == null) {
            markersPadding = (int) (this.getHeight() / 3.0);
        }
        return markersPadding;
    }
}
