package com.stumi.gobuddies.ui.registration;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.stumi.gobuddies.model.User;
import com.stumi.gobuddies.system.IntentManager;
import com.stumi.gobuddies.system.UserDataHolder;
import com.stumi.gobuddies.system.network.ExecutorDispatcher;
import com.stumi.gobuddies.system.network.request.ObserverRequestExecutor;

import javax.inject.Inject;

/**
 * @author stumpfb on 29/07/2016.
 */

public class RegistrationPresenter extends MvpBasePresenter<RegistrationView>
        implements ObserverRequestExecutor.BaseResponseObserverListener<RegistrationModel> {

    private static final String TAG = RegistrationPresenter.class.getSimpleName();

    private final RegistrationAPIProvider registrationAPIProvider;

    private ExecutorDispatcher executorDispatcher;

    private final UserDataHolder userDataHolder;

    private final IntentManager intentManager;

    @Inject
    public RegistrationPresenter(RegistrationAPIProvider registrationAPIProvider,
                                 ExecutorDispatcher executorDispatcher,
                                 UserDataHolder userDataHolder, IntentManager intentManager) {
        this.registrationAPIProvider = registrationAPIProvider;
        this.executorDispatcher = executorDispatcher;
        this.userDataHolder = userDataHolder;
        this.intentManager = intentManager;
    }

    public void registerClick(int team, String nickname) {
        if (isViewAttached()) {
            if (nickname != null && !nickname.isEmpty()) {
                getView().showLoading();
                executorDispatcher
                        .executeForMainThread(registrationAPIProvider.registerUser(nickname, team), this);
            } else {
                getView().showValidationError();
            }
        }
    }

    @Override
    public void callbackError(int errorCode, String message) {
        Log.e(TAG, message);
        if (isViewAttached()) {
            getView().showError(errorCode);
        }
    }

    @Override
    public void callbackSuccess(RegistrationModel body) {
        Log.d(TAG, body.toString());
        if (isViewAttached()) {
            getView().showSuccess();
            saveUserToken(body.getOk());
        }
        intentManager.startTeamFinderFromRegistration();
        if (isViewAttached()) {
            getView().finish();

        }
    }

    public void restoreData() {
        if (userDataHolder.isTokenExists() && isViewAttached()) {
            getView().setData(userDataHolder.getSelf());
        }
    }

    private void saveUserToken(String token) {
        userDataHolder.saveSelf(new User(token, getView().getName(), getView().getTeam()));
    }


}
