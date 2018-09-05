package com.stumi.gobuddies.ui.floating.tabs.partyFinder;

import com.stumi.gobuddies.system.helper.FragmentScope;
import com.stumi.gobuddies.system.helper.MockServer;

import dagger.Module;
import dagger.Provides;

/**
 * @author stumpfb on 26/08/2016.
 */
@Module
public class PartyFinderModule {

    @Provides
    @FragmentScope
    public PartyFinderApiProvider teamFinderApiProvider(MockServer mockServer) {
        return new DevelopPartyFinderApiProvider(mockServer);
    }
}
