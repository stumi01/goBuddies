package com.stumi.gobuddies.ui.registration;

import com.stumi.gobuddies.system.helper.ActivityScope;
import com.stumi.gobuddies.system.helper.MockServer;

import dagger.Module;
import dagger.Provides;

/**
 * @author stumpfb on 26/08/2016.
 */
@Module
public class RegistrationModule {

    @Provides
    @ActivityScope
    public RegistrationAPIProvider registrationAPIProvider(MockServer mockServer) {
        return new DevelopRegistrationAPIProvider(mockServer);
    }
}
