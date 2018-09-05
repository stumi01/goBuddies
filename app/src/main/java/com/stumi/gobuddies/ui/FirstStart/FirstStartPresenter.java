package com.stumi.gobuddies.ui.firstStart;

import android.graphics.Point;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.stumi.gobuddies.BuildConfig;
import com.stumi.gobuddies.system.IntentManager;
import com.stumi.gobuddies.system.UserDataHolder;
import com.stumi.gobuddies.system.network.ExecutorDispatcher;
import com.stumi.gobuddies.system.network.request.ObserverRequestExecutor;

import javax.inject.Inject;

import static com.stumi.gobuddies.system.network.ErrorCodesConstant.UN_AUTHORIZED;

/**
 * @author stumpfb on 07/10/2016.
 */
public class FirstStartPresenter extends MvpBasePresenter<FirstStartView>
        implements ObserverRequestExecutor.BaseResponseObserverListener<ValidationModel> {

    private static final String TAG = FirstStartPresenter.class.getSimpleName();
    private final UserDataHolder userDataHolder;

    private final IntentManager intentManager;

    private final HandshakeAPIProvider handshakeAPIProvider;

    private ExecutorDispatcher executorDispatcher;

    @Inject
    public FirstStartPresenter(HandshakeAPIProvider handshakeAPIProvider,
                               ExecutorDispatcher executorDispatcher, UserDataHolder userDataHolder,
                               IntentManager intentManager) {
        this.userDataHolder = userDataHolder;
        this.intentManager = intentManager;
        this.handshakeAPIProvider = handshakeAPIProvider;
        this.executorDispatcher = executorDispatcher;
    }

    public void startApp() {
        if (userDataHolder.isTokenExists()) {
            checkTokenValid();
        } else {
            startRegistration();
        }
    }

    @Override
    public void callbackError(int errorCode, String message) {
        if (errorCode == UN_AUTHORIZED) {
            startRegistration();
        } else {
            //TODO: 23/12/2016 other errors?
            if (isViewAttached()) {
                getView().showGrantErrorAndExit();
            }
        }
    }

    @Override
    public void callbackSuccess(ValidationModel body) {
        checkValidationResult(body);
    }

    private void checkTokenValid() {
        executorDispatcher.executeForMainThread(handshakeAPIProvider.validation(), this);
    }

    private void checkValidationResult(ValidationModel model) {
        if (model.getVersion() > BuildConfig.VERSION_CODE) {
            showMandatoryAppUpdateAvailable();
        } else if (model.isValid()) {
            startTeamFinder();
        } else {
            startRegistration();
        }
    }

    private void startTeamFinder() {
        if (isViewAttached()) {
            Point size = getView().getScreenSize();
            getView().showSuccessAndHide(() -> intentManager.startTeamFinder(size.x / 2, size.y / 2));
        }
    }

    private void startRegistration() {
        if (isViewAttached()) {
            getView().showSuccess(intentManager::startRegistrationActivity);
        }
    }

    private void showMandatoryAppUpdateAvailable() {
        if (isViewAttached()) {
            getView().showSuccess(intentManager::startGooglePlayForUpdate);
        }
    }
}
