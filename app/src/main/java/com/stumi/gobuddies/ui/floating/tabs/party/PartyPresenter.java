package com.stumi.gobuddies.ui.floating.tabs.party;

import android.util.Log;

import com.stumi.gobuddies.base.RequestAwarePresenter;
import com.stumi.gobuddies.model.BaseResponseModel;
import com.stumi.gobuddies.model.User;
import com.stumi.gobuddies.system.LocationManager;
import com.stumi.gobuddies.system.UserDataHolder;
import com.stumi.gobuddies.system.helper.scheduler.Ticker;
import com.stumi.gobuddies.system.helper.scheduler.TickerSubscriber;
import com.stumi.gobuddies.system.network.ExecutorDispatcher;
import com.stumi.gobuddies.system.network.request.ObserverRequestExecutor;
import com.stumi.gobuddies.ui.floating.tabs.party.message.PartyMessage;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * @author stumpfb on 28/08/2016.
 */
public class PartyPresenter extends RequestAwarePresenter<PartyView> {

    private static final String TAG = PartyPresenter.class.getSimpleName();

    private ExecutorDispatcher executorDispatcher;

    private final UserDataHolder userDataHolder;

    private final PartyApiProvider provider;

    private final LocationManager locationManager;

    private final Ticker ticker;

    private boolean keyBoardVisible;

    private final Disposable isInPartySubscription;

    @Inject
    public PartyPresenter(PartyApiProvider provider, ExecutorDispatcher executorDispatcher,
                          UserDataHolder userDataHolder, LocationManager locationManager, Ticker ticker) {
        this.executorDispatcher = executorDispatcher;
        this.userDataHolder = userDataHolder;
        this.provider = provider;
        this.locationManager = locationManager;
        this.ticker = ticker;
        isInPartySubscription = userDataHolder.subscribePartyStatusUpdates(isInParty -> {
            if (isInParty && isViewAttached()) {
                loadData();
            }
        });
    }

    public void loadPreviousMessages() {
        //TODO avoid calling this multiple times #50
        watchForCancel(executorDispatcher.executeForMainThread(provider.loadAllPreviousMessages(),
                new ObserverRequestExecutor.BaseResponseObserverListener<Party>() {
                    @Override
                    public void callbackError(int errorCode, String message) {
                        Log.e(TAG, message.concat(" / " + errorCode));
                    }

                    @Override
                    public void callbackSuccess(Party party) {
                        setData(party);
                        startBackgroundTask();
                    }
                }));
    }

    public void keyboardVisibilityChange(boolean isOpen) {
        keyBoardVisible = isOpen;
        if (isViewAttached()) {
            if (isOpen) {
                getView().hideMap();
            } else {
                getView().showMap();
            }
        }
    }

    private void setData(Party party) {
        if (isViewAttached()) {
            getView().showContent();
            getView().setMessages(party.getMessages());
            setMap(party);
            Log.d(TAG, party.toString());
        }
    }

    private void updateData(Party party) {
        if (isViewAttached()) {
            getView().updateMessages(party.getMessages());
            setMap(party);
            Log.d(TAG, party.toString());
        }
    }

    private void setMap(Party party) {
        if (!keyBoardVisible) {
            getView().showMap();
        }
        if (isCurrentUserLeader(party)) {
            getView().setMapDataForLeader(party.getMemberPositions(), locationManager.getUserLocation());
        } else {
            getView().setMapDataForMembers(party.getLeaderPosition(), locationManager.getUserLocation());
        }
    }

    private boolean isCurrentUserLeader(Party party) {
        return party.getLeaderPosition() == null;
    }

    private void loadUnreadMessages() {
        watchForCancel(executorDispatcher.executeForMainThread(provider.loadUnreadMessages(),
                new ObserverRequestExecutor.BaseResponseObserverListener<Party>() {
                    @Override
                    public void callbackError(int errorCode, String message) {

                    }

                    @Override
                    public void callbackSuccess(Party party) {
                        updateData(party);
                    }
                }));
    }

    public void sendMessage(String text) {
        executorDispatcher.executeForMainThread(provider.sendMessage(text),
                new ObserverRequestExecutor.BaseResponseObserverListener<BaseResponseModel>() {
                    @Override
                    public void callbackError(int errorCode, String message) {
                        sendMessageError(errorCode);
                    }

                    @Override
                    public void callbackSuccess(BaseResponseModel body) {
                        sendMessageSuccess(text);
                    }
                });
    }

    private void sendMessageSuccess(String text) {
        if (isViewAttached()) {
            getView().updateMessages(createSelfMessage(text));
            getView().showMessageSendSuccess();
        }
    }

    private List<PartyMessage> createSelfMessage(String text) {
        final User self = userDataHolder.getSelf();
        PartyMessage message = new PartyMessage();
        message.setMessage(text);
        message.setName(self.getName());
        message.setLead(self.isLeader());
        message.setMe(true);
        message.setTeam(self.getTeam());

        return Arrays.asList(message);
    }

    private void sendMessageError(int errorCode) {
        if (isViewAttached()) {
            getView().showMessageSendError(errorCode);
        }
    }

    public void startBackgroundTask() {
        ticker.start(5, new TickerSubscriber(this::loadUnreadMessages));
    }

    public void stop() {
        ticker.stop();
        cancelAll();
    }

    @Override
    public void detachView(boolean retainInstance) {
        isInPartySubscription.dispose();
        super.detachView(retainInstance);
    }

    public void loadData() {
        if (userDataHolder.isInParty()) {
            loadPreviousMessages();
        } else {
            stop();
        }
    }
}
