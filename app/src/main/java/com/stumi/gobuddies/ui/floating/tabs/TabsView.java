package com.stumi.gobuddies.ui.floating.tabs;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.stumi.gobuddies.events.SwitchToFragment;

/**
 * Created by sourc on 2016. 09. 10..
 */
public interface TabsView extends MvpView {

    void switchToFragment(SwitchToFragment switchToFragment);

    void enablePartyTabs();

    void disablePartyTabs();

    void showKickedMessage();
}
