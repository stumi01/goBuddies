package com.stumi.gobuddies.ui.floating.tabs.partyMembers;

import com.stumi.gobuddies.system.helper.FragmentScope;

import dagger.Subcomponent;

/**
 * @author stumpfb on 25/08/2016.
 */
@FragmentScope
@Subcomponent()
public interface PartyMembersComponent {

    PartyMembersPresenter presenter();

}
