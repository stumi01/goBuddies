package com.stumi.gobuddies.ui.firstStart;

import android.app.Activity;
import android.graphics.Point;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.stumi.gobuddies.system.widget.MorphingToastLayout;

/**
 * @author stumpfb on 06/02/2017.
 */
public interface FirstStartView extends MvpView {

    void showGrantErrorAndExit();

    void showSuccess(MorphingToastLayout.TransitionEndListener listener);

    Activity getActivity();

    Point getScreenSize();

    void showSuccessAndHide(MorphingToastLayout.TransitionEndListener listener);
}
