package com.stumi.gobuddies.ui.floating.tabs.party;

import com.stumi.gobuddies.system.helper.FragmentScope;
import com.stumi.gobuddies.system.helper.MockServer;

import dagger.Module;
import dagger.Provides;

/**
 * @author stumpfb on 28/08/2016.
 */
@Module
public class PartyModule {

    @Provides
    @FragmentScope
    public PartyApiProvider providesPartyApi(MockServer mockServer) {
        return new DevelopPartyApiProvider(mockServer);
    }
}
