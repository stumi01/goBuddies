package com.stumi.gobuddies.ui.floating.tabs.partyFinder;

import com.stumi.gobuddies.system.helper.FragmentScope;

import dagger.Subcomponent;

/**
 * @author stumpfb on 31/07/2016.
 */
@FragmentScope
@Subcomponent(modules = PartyFinderModule.class)
public interface PartyFinderComponent {

    PartyFinderPresenter presenter();

    void inject(PartyFinderFragment activity);
}
