package com.stumi.gobuddies.ui.firstStart;

import com.stumi.gobuddies.system.helper.ActivityScope;
import com.stumi.gobuddies.system.helper.MockServer;

import dagger.Module;
import dagger.Provides;

/**
 * @author stumpfb on 09/10/2016.
 */
@ActivityScope
@Module
public class FirstStartModule {

    @ActivityScope
    @Provides
    public HandshakeAPIProvider providesHandshakeAPIProvider(MockServer mockServer) {
        return new DevelopHandshakeAPIProvider(mockServer);
    }
}
