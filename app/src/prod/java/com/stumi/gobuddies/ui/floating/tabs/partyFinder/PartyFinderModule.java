package com.stumi.gobuddies.ui.floating.tabs.partyFinder;

import com.stumi.gobuddies.system.helper.FragmentScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author stumpfb on 31/07/2016.
 */
@Module
public class PartyFinderModule {

    @Provides
    @FragmentScope
    public PartyFinderApiProvider providesPartyFinderApiProvider(Retrofit retrofit) {
        return retrofit.create(PartyFinderApiProvider.class);
    }

}
