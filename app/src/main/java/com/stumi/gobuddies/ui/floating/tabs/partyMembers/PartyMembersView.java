package com.stumi.gobuddies.ui.floating.tabs.partyMembers;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

/**
 * @author stumpfb on 21/08/2016.
 */
public interface PartyMembersView extends MvpLceView<PartyMembers> {

    void setTeamName(String teamName);

    void showRequestError(int errorCode);

    void setFieldsEnabled(boolean enabled);

    boolean isTeamNameEditing();

    void showSuccess();

    void showLoadingRequest();
}
