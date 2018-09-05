package com.stumi.gobuddies.ui.floating.tabs.partyMembers;

import com.crashlytics.android.answers.CustomEvent;
import com.stumi.gobuddies.base.RequestAwarePresenter;
import com.stumi.gobuddies.events.UserKickedFromPartyEvent;
import com.stumi.gobuddies.model.BaseResponseModel;
import com.stumi.gobuddies.model.User;
import com.stumi.gobuddies.system.LocationManager;
import com.stumi.gobuddies.system.UserDataHolder;
import com.stumi.gobuddies.system.location.LocationUpdateModel;
import com.stumi.gobuddies.system.network.ExecutorDispatcher;
import com.stumi.gobuddies.system.network.request.ObserverRequestExecutor;
import com.stumi.gobuddies.system.tracking.KPIEvent;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author stumpfb on 21/08/2016.
 */
public class PartyMembersPresenter extends RequestAwarePresenter<PartyMembersView>
        implements ObserverRequestExecutor.BaseResponseObserverListener<PartyMembers>,
        Consumer<LocationUpdateModel> {

    private final PartyMembersApiProvider partyMembersApiProvider;

    private final Disposable isInPartySubscription;

    private ExecutorDispatcher executorDispatcher;

    private final UserDataHolder userDataHolder;

    private final LocationManager locationManager;

    private final EventBus eventBus;

    private Disposable locationSubscription;

    @Inject
    public PartyMembersPresenter(PartyMembersApiProvider partyMembersApiProvider,
                                 ExecutorDispatcher executorDispatcher, UserDataHolder userDataHolder,
                                 LocationManager locationManager, EventBus eventBus) {
        this.partyMembersApiProvider = partyMembersApiProvider;
        this.executorDispatcher = executorDispatcher;
        this.userDataHolder = userDataHolder;
        this.locationManager = locationManager;
        this.eventBus = eventBus;
        isInPartySubscription = userDataHolder.subscribePartyStatusUpdates(isInParty -> {
            if (isInParty && isViewAttached()) {
                loadTeamData();
            }
        });
    }

    public void loadTeamData() {
        if (userDataHolder.isInParty()) {
            locationSubscription = locationManager.subscribe(this);
            watchForCancel(
                    executorDispatcher.executeForMainThread(partyMembersApiProvider.loadTeamMembers(), this));
        } else {
            onPause();
        }
    }

    @Override
    public void callbackError(int errorCode, String message) {
        showError(errorCode);
    }

    @Override
    public void callbackSuccess(PartyMembers partyMembers) {
        if (isViewAttached()) {
            getView().setData(partyMembers);
        }
    }

    @Override
    public void accept(LocationUpdateModel locationUpdateModel) throws Exception {
        if (isViewAttached() && !getView().isTeamNameEditing()) {
            getView().setTeamName(locationUpdateModel.getCurrentGroupName());
        }
    }

    public void passLeaderTo(User user) {
        getView().setFieldsEnabled(false);
        getView().showLoadingRequest();

        watchForCancel(executorDispatcher.executeForMainThread(partyMembersApiProvider.passLead(user.getId()),
                new ObserverRequestExecutor.BaseResponseObserverListener<BaseResponseModel>() {
                    @Override
                    public void callbackError(int errorCode, String message) {
                        showError(errorCode);
                    }

                    @Override
                    public void callbackSuccess(BaseResponseModel body) {
                        getView().showSuccess();
                        getView().setFieldsEnabled(true);
                        loadTeamData();
                    }
                }));
    }

    public void kickUser(User user) {
        eventBus.post(new KPIEvent(new CustomEvent("Kick User")));

        getView().setFieldsEnabled(false);
        getView().showLoadingRequest();
        watchForCancel(executorDispatcher.executeForMainThread(partyMembersApiProvider.kickUser(user.getId()),
                new ObserverRequestExecutor.BaseResponseObserverListener<BaseResponseModel>() {
                    @Override
                    public void callbackError(int errorCode, String message) {
                        showError(errorCode);
                    }

                    @Override
                    public void callbackSuccess(BaseResponseModel body) {
                        getView().showSuccess();
                        getView().setFieldsEnabled(true);
                        loadTeamData();
                    }
                }));
    }

    public void changeTeamName(String newName) {
        getView().setFieldsEnabled(false);
        getView().showLoadingRequest();
        watchForCancel(executorDispatcher.executeForMainThread(
                partyMembersApiProvider.renameParty(newName, userDataHolder.getSelf().getTeam()),
                new ObserverRequestExecutor.BaseResponseObserverListener<BaseResponseModel>() {
                    @Override
                    public void callbackError(int errorCode, String message) {
                        showError(errorCode);
                    }

                    @Override
                    public void callbackSuccess(BaseResponseModel body) {
                        getView().showSuccess();
                        getView().setFieldsEnabled(true);
                    }
                }));
    }

    public void leaveParty() {
        eventBus.post(new KPIEvent(new CustomEvent("Leave Party")));
        watchForCancel(executorDispatcher.executeForMainThread(partyMembersApiProvider.leaveParty(),
                new ObserverRequestExecutor.BaseResponseObserverListener<BaseResponseModel>() {
                    @Override
                    public void callbackError(int errorCode, String message) {
                        showError(errorCode);
                    }

                    @Override
                    public void callbackSuccess(BaseResponseModel body) {
                        eventBus.post(new UserKickedFromPartyEvent());
                    }
                }));
    }

    private void showError(int errorCode) {
        if (isViewAttached()) {
            getView().setFieldsEnabled(true);
            getView().showRequestError(errorCode);
        }
    }

    public void onPause() {
        cancelAll();
        if (locationSubscription != null && !locationSubscription.isDisposed()) {
            locationSubscription.dispose();
        }
    }

    @Override
    public void detachView(boolean retainInstance) {
        isInPartySubscription.dispose();
        super.detachView(retainInstance);
    }

}
