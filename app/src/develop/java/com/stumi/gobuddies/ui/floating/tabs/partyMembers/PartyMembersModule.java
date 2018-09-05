package com.stumi.gobuddies.ui.floating.tabs.partyMembers;

import com.stumi.gobuddies.system.helper.ActivityScope;
import com.stumi.gobuddies.system.helper.MockServer;

import dagger.Module;
import dagger.Provides;

/**
 * @author stumpfb on 26/08/2016.
 */
@Module
public class PartyMembersModule {

    @Provides
    @ActivityScope
    public PartyMembersApiProvider teamPartyApiProvider(MockServer mockServer) {
        return new DevelopPartyMembersApiProvider(mockServer);
    }
}
