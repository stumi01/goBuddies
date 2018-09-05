package com.stumi.gobuddies.ui.registration;

import com.stumi.gobuddies.system.helper.ActivityScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author stumpfb on 31/07/2016.
 */
@Module
public class RegistrationModule {

    @Provides
    @ActivityScope
    RegistrationAPIProvider providesRegistrationApi(Retrofit retrofit) {
        return retrofit.create(RegistrationAPIProvider.class);
    }

}
