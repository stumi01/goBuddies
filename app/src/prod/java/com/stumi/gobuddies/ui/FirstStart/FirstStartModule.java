package com.stumi.gobuddies.ui.firstStart;

import com.stumi.gobuddies.system.helper.ActivityScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author stumpfb on 09/10/2016.
 */
@ActivityScope
@Module
public class FirstStartModule {

    @ActivityScope
    @Provides
    public HandshakeAPIProvider providesHandshakeAPIProvider(Retrofit retrofit) {
        return retrofit.create(HandshakeAPIProvider.class);
    }

}
