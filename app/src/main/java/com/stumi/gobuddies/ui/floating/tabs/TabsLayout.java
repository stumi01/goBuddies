package com.stumi.gobuddies.ui.floating.tabs;

import android.content.Context;
import android.support.design.widget.TabLayout;

import com.stumi.gobuddies.GoBuddiesApplication;
import com.stumi.gobuddies.R;
import com.stumi.gobuddies.base.mvp.BaseMVPLayout;
import com.stumi.gobuddies.events.SwitchToFragment;
import com.stumi.gobuddies.model.GoBuddiesTab;

import butterknife.BindView;

/**
 * Created by sourc on 2017. 03. 08..
 */

public class TabsLayout extends BaseMVPLayout<TabsView, TabsPresenter> implements TabsView {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.pager)
    SwypeControlViewPager viewPager;

    public TabsLayout(Context context) {
        super(context);
        viewPager.initTabs(tabLayout);
    }

    @Override
    public TabsPresenter createPresenter() {
        return GoBuddiesApplication.get(getContext()).getFloatingComponent().plus(new TabsModule()).presenter();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_tabs;
    }

    @Override
    public void switchToFragment(SwitchToFragment fragment) {
        if (fragment.getFragmentItemId() == GoBuddiesTab.PARTY.getId()) {
            enablePartyTabs();
        }
        viewPager.setCurrentItem(fragment.getFragmentItemId());
    }

    @Override
    public void enablePartyTabs() {
        viewPager.setSwypeEnabled(true);
    }

    @Override
    public void disablePartyTabs() {
        switchToFragment(new SwitchToFragment(GoBuddiesTab.BUDDY_FINDER));
        viewPager.setSwypeEnabled(false);
    }

    @Override
    public void showKickedMessage() {
        //TODO
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.onResume();
    }

    @Override
    protected void onDetachedFromWindow() {
        presenter.onPause();
        tabLayout.clearOnTabSelectedListeners();
        super.onDetachedFromWindow();
    }
}
