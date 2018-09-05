package com.stumi.gobuddies.ui.bubble;

import com.stumi.gobuddies.di.ApplicationComponent;
import com.stumi.gobuddies.system.helper.ActivityScope;
import com.stumi.gobuddies.ui.floating.tabs.partyMembers.PartyMembersModule;

import dagger.Component;

/**
 * @author stumpfb on 13/08/2016.
 */
@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = PartyMembersModule.class)
public interface BubbleHeadComponent {

    BubbleHeadPresenter presenter();

    void inject(BubbleHeadService view);
}
