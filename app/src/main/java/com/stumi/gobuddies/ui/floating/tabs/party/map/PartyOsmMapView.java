package com.stumi.gobuddies.ui.floating.tabs.party.map;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;

import com.google.android.gms.maps.model.LatLng;
import com.stumi.gobuddies.R;

import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.res.ResourcesCompat.getColor;

/**
 * Created by sourc on 2017. 03. 30..
 */

public class PartyOsmMapView extends MapView {
    private static final String YOU = "You";
    private Drawable memberIcon;
    private Drawable leaderIcon;
    private Drawable userIcon;

    public PartyOsmMapView(Context context, MapTileProviderBase tileProvider, Handler tileRequestCompleteHandler, AttributeSet attrs) {
        super(context, tileProvider, tileRequestCompleteHandler, attrs);
        init();

    }

    public PartyOsmMapView(Context context, MapTileProviderBase tileProvider, Handler tileRequestCompleteHandler, AttributeSet attrs, boolean hardwareAccelerated) {
        super(context, tileProvider, tileRequestCompleteHandler, attrs, hardwareAccelerated);
        init();

    }

    public PartyOsmMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public PartyOsmMapView(Context context) {
        super(context);
        init();

    }

    public PartyOsmMapView(Context context, MapTileProviderBase aTileProvider) {
        super(context, aTileProvider);
        init();

    }

    public PartyOsmMapView(Context context, MapTileProviderBase aTileProvider, Handler tileRequestCompleteHandler) {
        super(context, aTileProvider, tileRequestCompleteHandler);
        init();
    }

    private void init() {
        memberIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_person_pin_circle_black_24dp);
        leaderIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_person_pin_circle_black_24dp_crown);
        userIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_my_location_black_24dp);
        getController().setZoom(10);
        setBuiltInZoomControls(true);
        setMultiTouchControls(true);
        setTilesScaledToDpi(true);
    }


    public void setMapDataForLeader(List<UserPosition> mems, LatLng userLocation) {
        getOverlays().clear();

        ArrayList<OverlayItem> items = new ArrayList<>();
        for (UserPosition up : mems) {
            items.add(createOverLayItem(up, memberIcon));
        }
        items.add(createOverLayItem(UserPosition.Create(YOU, userLocation), userIcon));

        getOverlays().add(createMarkersOverlay(items));
        zoomToBoundingBox(calculateBoundingBox(items), true);

    }

    public void setMapDataForMembers(UserPosition leader, LatLng userLocation) {
        getOverlays().clear();

        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        items.add(createOverLayItem(leader, leaderIcon));
        items.add(createOverLayItem(UserPosition.Create(YOU, userLocation), userIcon));

        getOverlays().add(createMarkersOverlay(items));
        zoomToBoundingBox(calculateBoundingBox(items), true);
    }

    @NonNull
    private ItemizedOverlayWithFocus<OverlayItem> createMarkersOverlay(ArrayList<OverlayItem> items) {
        ItemizedOverlayWithFocus<OverlayItem> overlay = new ItemizedOverlayWithFocus<>(getContext(),
                items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(int index, OverlayItem item) {
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(int index, OverlayItem item) {
                        return false;
                    }
                });
        overlay.setFocusItemsOnTap(true);
        changeOverlayColor(overlay);
        return overlay;
    }

    private void changeOverlayColor(ItemizedOverlayWithFocus<OverlayItem> overlay) {
        overlay.setMarkerTitleForegroundColor(
                getColor(getResources(), R.color.tab_selected_text_color, null));
        overlay.setMarkerBackgroundColor(
                getColor(getResources(), R.color.floating_activity_gradient_end, null));
    }

    @NonNull
    private OverlayItem createOverLayItem(UserPosition leader, Drawable leaderIcon) {
        OverlayItem item = new OverlayItem(leader.getName(), "", new GeoPoint(leader.getLat(), leader.getLon()));
        item.setMarker(leaderIcon);
        return item;
    }

    public BoundingBox calculateBoundingBox(ArrayList<OverlayItem> points) {

        double nord = 0, sud = 0, ovest = 0, est = 0;

        for (int i = 0; i < points.size(); i++) {
            if (points.get(i) == null) continue;

            double lat = points.get(i).getPoint().getLatitude();
            double lon = points.get(i).getPoint().getLongitude();

            if ((i == 0) || (lat > nord)) nord = lat;
            if ((i == 0) || (lat < sud)) sud = lat;
            if ((i == 0) || (lon < ovest)) ovest = lon;
            if ((i == 0) || (lon > est)) est = lon;

        }

        return new BoundingBox(nord + 0.001, est - 0.001, sud - 0.001, ovest + 0.001);

    }
}
