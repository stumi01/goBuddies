package com.stumi.gobuddies.ui.floating.settings;

import com.stumi.gobuddies.system.helper.FragmentScope;
import com.stumi.gobuddies.system.helper.MockServer;

import dagger.Module;
import dagger.Provides;

/**
 * @author stumpfb on 06/09/2016.
 */

@Module
public class SettingsModule {

    @FragmentScope
    @Provides
    public SettingsApiProvider providesSettingsApiProvider(MockServer mockServer) {
        return new DevelopSettingsApiProvider(mockServer);
    }
}
