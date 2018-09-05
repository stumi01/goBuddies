package com.stumi.gobuddies.ui.bubble;

import android.graphics.Point;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.stumi.gobuddies.events.ShowBubbleView;
import com.stumi.gobuddies.events.ShowFloatingView;
import com.stumi.gobuddies.system.IntentManager;
import com.stumi.gobuddies.system.LocationManager;
import com.stumi.gobuddies.system.UserDataHolder;
import com.stumi.gobuddies.system.location.LocationUpdateModel;
import com.stumi.gobuddies.system.network.ExecutorDispatcher;
import com.stumi.gobuddies.ui.floating.tabs.partyMembers.PartyMembersApiProvider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author stumpfb on 13/08/2016.
 */
public class BubbleHeadPresenter extends MvpBasePresenter<BubbleHeadView>
        implements Consumer<LocationUpdateModel> {

    private static final String TAG = BubbleHeadPresenter.class.getSimpleName();

    private final IntentManager intentManager;

    private final LocationManager locationManager;

    private final UserDataHolder userDataHolder;

    private final EventBus eventBus;

    private PartyMembersApiProvider partyMembersApiProvider;
    private ExecutorDispatcher executorDispatcher;

    private Disposable subscription;

    @Inject
    public BubbleHeadPresenter(IntentManager intentManager, LocationManager locationManager,
                               UserDataHolder userDataHolder,
                               PartyMembersApiProvider partyMembersApiProvider,
                               ExecutorDispatcher executorDispatcher, EventBus eventBus) {
        this.intentManager = intentManager;
        this.locationManager = locationManager;
        this.userDataHolder = userDataHolder;
        this.partyMembersApiProvider = partyMembersApiProvider;
        this.executorDispatcher = executorDispatcher;
        this.eventBus = eventBus;
    }


    @Subscribe
    public void onEvent(ShowFloatingView showFloatingView) {
        bubbleClick(userDataHolder.getHeadLastPosition().x, userDataHolder.getHeadLastPosition().y, showFloatingView.getX(), showFloatingView.getY());
    }

    @Subscribe
    public void onEvent(ShowBubbleView showFloatingView) {
        if (isViewAttached()) {
            final Point headLastPosition = userDataHolder.getHeadLastPosition();
            getView().addHead(headLastPosition.x, headLastPosition.y);
        }
    }

    public void bubbleClick(int bubbleX, int bubbleY, float x, float y) {
        if (isViewAttached()) {
            getView().disableHeadClick();
            bubbleMoved(bubbleX, bubbleY);
            getView().showFloatingView((int) x, (int) y);
        }
    }

    @Override
    public void accept(LocationUpdateModel locationUpdateModel) throws Exception {
        if (isViewAttached()) {
            getView().setUnreadMessages(locationUpdateModel.getUnseenMessagesCount());
            getView().setNearParties(locationUpdateModel.getNearbyPartiesCount());
            getView().setUnseenRequests(locationUpdateModel.getReceivedRequestsCount());
            getView().setGroupName(locationUpdateModel.getCurrentGroupName());
            getView().setGroupMembersCount(locationUpdateModel.getGroupMembersCount());
            getView().setGroupTeam(locationUpdateModel.getGroupsTeam(),
                    locationUpdateModel.getGroupMembersCount());
            getView().setLead(locationUpdateModel.isLead(), locationUpdateModel.getGroupMembersCount());
        }
    }

    private void startUpdate() {
        subscription = locationManager.subscribe(this);
    }

    private void stopListener() {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }

    public void bubbleRemoved() {
        stopListener();
    }

    public void onCreate() {
        eventBus.register(this);
        final Point headLastPosition = userDataHolder.getHeadLastPosition();
        getView().initView(headLastPosition.x, headLastPosition.y);
        startUpdate();
    }

    public void bubbleTrashed() {
        onDestroy();
        executorDispatcher.executeOnBackground(partyMembersApiProvider.leaveParty());
    }

    public void onDestroy() {
        stopListener();
        intentManager.stopBubbleHeads();
        eventBus.unregister(this);
    }

    public void bubbleMoved(int x, int y) {
        userDataHolder.setHeadLastPosition(x, y);
    }
}
