package com.stumi.gobuddies.ui.floating.settings;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * @author stumpfb on 04/09/2016.
 */
public interface SettingsView extends MvpView {
    void setName(String name);

    void setTeam(int team);

    String getName();

    int getTeam();

    void showError(int code);

    void showSuccess();

    void showLoading();

    void disableSave();

    void enableSave();
}
