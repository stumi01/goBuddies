package com.stumi.gobuddies.ui.registration;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.stumi.gobuddies.model.User;

/**
 * @author stumpfb on 29/07/2016.
 */
public interface RegistrationView extends MvpView {

    String getName();

    int getTeam();

    void showError(int code);

    void showLoading();

    void showSuccess();

    void showValidationError();

    void setData(User self);

    void finish();
}
