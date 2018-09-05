package com.stumi.gobuddies.ui.floating.tabs.party;

import com.stumi.gobuddies.system.helper.FragmentScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author stumpfb on 30/08/2016.
 */
@FragmentScope
@Module
public class PartyModule {

    @Provides
    @FragmentScope
    public PartyApiProvider providesPartyApiProvider(Retrofit retrofit) {
        return retrofit.create(PartyApiProvider.class);
    }
}
