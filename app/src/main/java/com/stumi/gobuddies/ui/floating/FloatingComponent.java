package com.stumi.gobuddies.ui.floating;

import com.stumi.gobuddies.system.helper.ActivityScope;
import com.stumi.gobuddies.ui.floating.tabs.TabsComponent;
import com.stumi.gobuddies.ui.floating.tabs.TabsModule;
import com.stumi.gobuddies.ui.floating.tabs.party.PartyComponent;
import com.stumi.gobuddies.ui.floating.tabs.party.PartyModule;
import com.stumi.gobuddies.ui.floating.tabs.partyFinder.PartyFinderComponent;
import com.stumi.gobuddies.ui.floating.tabs.partyFinder.PartyFinderModule;
import com.stumi.gobuddies.ui.floating.tabs.partyMembers.PartyMembersComponent;
import com.stumi.gobuddies.ui.floating.tabs.partyMembers.PartyMembersModule;

import dagger.Subcomponent;

/**
 * @author stumpfb on 03/09/2016.
 */
@ActivityScope
@Subcomponent(modules = {FloatingModule.class, PartyMembersModule.class})
public interface FloatingComponent {

    FloatingPresenter presenter();

    TabsComponent plus(TabsModule tabsModule);

    PartyFinderComponent plus(PartyFinderModule partyModule);

    PartyComponent plus(PartyModule partyModule);

    PartyMembersComponent plusPartyMembersComponent();

    void inject(FloatingLayer floatingLayer);
}
