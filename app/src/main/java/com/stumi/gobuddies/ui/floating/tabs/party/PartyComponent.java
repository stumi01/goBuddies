package com.stumi.gobuddies.ui.floating.tabs.party;

import com.stumi.gobuddies.system.helper.FragmentScope;

import dagger.Subcomponent;

/**
 * @author stumpfb on 28/08/2016.
 */
@FragmentScope
@Subcomponent(modules = PartyModule.class)
public interface PartyComponent {

    PartyPresenter presenter();

}
