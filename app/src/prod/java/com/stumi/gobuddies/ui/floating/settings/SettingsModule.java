package com.stumi.gobuddies.ui.floating.settings;

import com.stumi.gobuddies.system.helper.FragmentScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author stumpfb on 06/09/2016.
 */
@Module
public class SettingsModule {

    @FragmentScope
    @Provides
    public SettingsApiProvider providesSettingsApiProvider(Retrofit retrofit) {
        return retrofit.create(SettingsApiProvider.class);
    }
}