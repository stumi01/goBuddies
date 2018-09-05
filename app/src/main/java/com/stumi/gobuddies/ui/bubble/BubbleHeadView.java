package com.stumi.gobuddies.ui.bubble;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * @author stumpfb on 13/08/2016.
 */
public interface BubbleHeadView extends MvpView {
    void setUnreadMessages(int mess);

    void setNearParties(int near);

    void setUnseenRequests(int reqs);

    void setGroupName(String gn);

    void setGroupMembersCount(int gmc);

    void setGroupTeam(int gt, int gmc);

    void setLead(boolean isLead, int gmc);

    void initView(int x, int y);

    void showFloatingView(int x, int y);

    void disableHeadClick();

    void addHead(int x, int y);
}
