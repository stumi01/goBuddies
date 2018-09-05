package com.stumi.gobuddies.ui.floating.tabs.partyMembers;

import com.stumi.gobuddies.system.helper.ActivityScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author stumpfb on 30/08/2016.
 */
@ActivityScope
@Module
public class PartyMembersModule {
    @Provides
    @ActivityScope
    public PartyMembersApiProvider providesPartyMembersApiProvider(Retrofit retrofit) {
        return retrofit.create(PartyMembersApiProvider.class);
    }
}
