package com.stumi.gobuddies.ui.floating.tabs.partyFinder;

import com.crashlytics.android.answers.CustomEvent;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.stumi.gobuddies.model.BaseResponseModel;
import com.stumi.gobuddies.model.User;
import com.stumi.gobuddies.system.IntentManager;
import com.stumi.gobuddies.system.LocationManager;
import com.stumi.gobuddies.system.helper.scheduler.Ticker;
import com.stumi.gobuddies.system.helper.scheduler.TickerImmediateStartSubscriber;
import com.stumi.gobuddies.system.location.LocationUpdateModel;
import com.stumi.gobuddies.system.network.ExecutorDispatcher;
import com.stumi.gobuddies.system.network.request.ObserverRequestExecutor.BaseResponseObserverListener;
import com.stumi.gobuddies.system.tracking.KPIEvent;
import com.stumi.gobuddies.ui.floating.tabs.partyFinder.party.PartyDetails;
import com.stumi.gobuddies.ui.floating.tabs.partyMembers.PartyMembers;
import com.stumi.gobuddies.ui.floating.tabs.partyMembers.PartyMembersApiProvider;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author stumpfb on 30/07/2016.
 */
public class PartyFinderPresenter extends MvpBasePresenter<PartyFinderView>
        implements Consumer<LocationUpdateModel> {

    private static final String TAG = PartyFinderPresenter.class.getSimpleName();

    protected static final int REFRESH_SECONDS = 60;

    private final Ticker ticker;

    private final EventBus eventBus;

    private final PartyFinderApiProvider provider;

    private final PartyMembersApiProvider partyMembersApiProvider;

    private ExecutorDispatcher executorDispatcher;

    private final IntentManager intentManager;

    private final LocationManager locationManager;

    private Disposable subscription;

    @Inject
    PartyFinderPresenter(PartyFinderApiProvider provider, PartyMembersApiProvider partyMembersApiProvider,
                         ExecutorDispatcher executorDispatcher, IntentManager intentManager,
                         LocationManager locationManager, Ticker ticker, EventBus eventBus) {
        this.provider = provider;
        this.partyMembersApiProvider = partyMembersApiProvider;
        this.executorDispatcher = executorDispatcher;
        this.intentManager = intentManager;
        this.locationManager = locationManager;
        this.ticker = ticker;
        this.eventBus = eventBus;
    }

    public void joinToPartyClick(PartyDetails partyDetails) {
        switch (partyDetails.getTeamRequestStatus()) {
            case RequestAvailable:
                doJoinRequest(partyDetails);
                break;
            case RequestAcceptable:
                doJoinAccept(partyDetails);
                break;
            case RequestAlreadySent:
            case NotAvailable:
            default:
                break;
        }
    }

    public void loadParties(boolean pullToRefresh) {
        if (isViewAttached()) {
            getView().showUpdating();
        }
        executorDispatcher.executeForMainThread(provider.loadCloseParties(),
                new BaseResponseObserverListener<List<PartyDetails>>() {
                    @Override
                    public void callbackError(int errorCode, String message) {
                        if (isViewAttached()) {
                            getView().hideUpdating();
                        }
                    }

                    @Override
                    public void callbackSuccess(List<PartyDetails> body) {
                        if (isViewAttached()) {
                            if (body.isEmpty()) {
                                getView().showLoading(false);
                            } else {
                                getView().hideUpdating();
                                getView().setData(body);
                            }
                        }
                    }

                });
    }

    public void loadPartiesPeriodically() {
        getView().showLoading(false);
        ticker.start(REFRESH_SECONDS, new TickerImmediateStartSubscriber(() -> {
            loadParties(false);
        }));
    }

    public Single<List<User>> partyExpandClick(String partyId) {
        return Single.create(singleSubscriber -> {
            executorDispatcher.executeForMainThread(partyMembersApiProvider.loadTeamMembers(partyId),
                    new BaseResponseObserverListener<PartyMembers>() {
                        @Override
                        public void callbackError(int errorCode, String message) {
                            singleSubscriber.onError(new InternalError(message));
                        }

                        @Override
                        public void callbackSuccess(PartyMembers body) {
                            singleSubscriber.onSuccess(body.getFullPartyInitialized());
                        }
                    });
        });
    }

    @Override
    public void accept(LocationUpdateModel locationUpdateModel) throws Exception {
        if (locationUpdateModel.getReceivedRequestsCount() > 0) {
            loadUnseenRequests();
        }

        if (locationUpdateModel.getNearbyPartiesCount() > 0) {
            loadParties(false);
        }
    }

    private void loadUnseenRequests() {
        executorDispatcher.executeForMainThread(provider.loadUnseenRequests(),
                new BaseResponseObserverListener<List<PartyDetails>>() {
                    @Override
                    public void callbackError(int errorCode, String message) {

                    }

                    @Override
                    public void callbackSuccess(List<PartyDetails> body) {
                        if (isViewAttached()) {
                            getView().promptRequests(body);
                        }
                    }

                });
    }

    public void doJoinAccept(PartyDetails partyDetails) {
        executorDispatcher.executeForMainThread(provider.acceptJoinRequest(partyDetails.getId()),
                new BaseResponseObserverListener<AcceptJoinResponseModel>() {
                    @Override
                    public void callbackError(int errorCode, String message) {
                        loadParties(false);
                    }

                    @Override
                    public void callbackSuccess(AcceptJoinResponseModel body) {
                        requestAccepted(body);
                    }

                });
    }

    private void requestAccepted(AcceptJoinResponseModel body) {
        eventBus.post(new KPIEvent(new CustomEvent("Request Accepted")));
        if (body.isLead()) {
            //stay in this screen & refresh
            loadParties(false);
        } else {
            joinedToTeam();
        }
    }

    private void joinedToTeam() {
        intentManager.switchToPartyFragment();
    }

    private void doJoinRequest(PartyDetails partyDetails) {
        eventBus.post(new KPIEvent(new CustomEvent("Do Join Request")));
        executorDispatcher.executeForMainThread(provider.doJoinRequest(partyDetails.getId()),
                new BaseResponseObserverListener<BaseResponseModel>() {
                    @Override
                    public void callbackError(int errorCode, String message) {
                        //TODO Notify error
                        loadParties(false);
                    }

                    @Override
                    public void callbackSuccess(BaseResponseModel body) {
                        loadParties(false);
                    }

                });
    }

    public void onPause() {
        ticker.stop();
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }

    public void onResume() {
        subscription = locationManager.subscribe(this);
    }

}
