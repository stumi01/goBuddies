package com.stumi.gobuddies.ui.floating.settings;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.stumi.gobuddies.events.BackButtonPressedEvent;
import com.stumi.gobuddies.model.BaseResponseModel;
import com.stumi.gobuddies.system.IntentManager;
import com.stumi.gobuddies.system.UserDataHolder;
import com.stumi.gobuddies.system.network.ExecutorDispatcher;
import com.stumi.gobuddies.system.network.request.ObserverRequestExecutor;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * @author stumpfb on 04/09/2016.
 */
public class SettingsPresenter extends MvpBasePresenter<SettingsView>
        implements ObserverRequestExecutor.BaseResponseObserverListener<BaseResponseModel> {

    private static final String TAG = SettingsPresenter.class.getSimpleName();

    private ExecutorDispatcher executorDispatcher;

    private final UserDataHolder userDataHolder;

    private final SettingsApiProvider apiProvider;

    private final EventBus eventBus;

    private final IntentManager intentManager;

    @Inject
    public SettingsPresenter(SettingsApiProvider apiProvider, ExecutorDispatcher executorDispatcher,
                             UserDataHolder userDataHolder, EventBus eventBus,
                             IntentManager intentManager) {
        this.executorDispatcher = executorDispatcher;
        this.userDataHolder = userDataHolder;
        this.apiProvider = apiProvider;
        this.eventBus = eventBus;
        this.intentManager = intentManager;
    }

    @Override
    public void attachView(SettingsView view) {
        super.attachView(view);

        getView().setName(userDataHolder.getSelf().getName());
        getView().setTeam(userDataHolder.getSelf().getTeam());
        getView().disableSave();
    }

    public void onSaveClick() {
        if (isViewAttached()) {
            getView().showLoading();
            String name = getView().getName();
            int team = getView().getTeam();
            executorDispatcher.executeForMainThread(apiProvider.updateSettings(name, team), this);
        }
    }

    @Override
    public void callbackError(int errorCode, String message) {
        Log.e(TAG, "error:" + message);
        getView().showError(errorCode);
    }

    @Override
    public void callbackSuccess(BaseResponseModel body) {
        if (isViewAttached()) {
            userDataHolder.updateSelf(getView().getName(), getView().getTeam());
            getView().showSuccess();
        }
    }

    public void feedbackClick() {
        intentManager.sendFeedbackEmail();
    }

    public void onBackClick() {
        eventBus.post(new BackButtonPressedEvent());
    }

    public void teamChange() {
        if (isViewAttached() && getView().getTeam() != userDataHolder.getSelf().getTeam()) {
            getView().enableSave();
        }

    }
}
