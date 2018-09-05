package com.stumi.gobuddies.ui.floating;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by sourc on 2017. 02. 27..
 */
public interface FloatingView extends MvpView {
    void onBackButtonPressed();

    void showGpsError();

    void hideGpsError();

    void showNetworkError();

    void hideNetworkError();

    void closeWindow();
}
