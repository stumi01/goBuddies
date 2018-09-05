package com.stumi.gobuddies.di;

import android.content.Context;

import com.stumi.gobuddies.system.LocationManager;
import com.stumi.gobuddies.system.UserDataHolder;
import com.stumi.gobuddies.system.location.LocationAPIProvider;
import com.stumi.gobuddies.system.location.LocationProvider;
import com.stumi.gobuddies.system.network.ExecutorDispatcher;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author stumpfb on 30/07/2016.
 */
@Module
public class ApplicationModule extends BaseApplicationModule {

    public ApplicationModule(Context context) {
        super(context);
    }

    @Singleton
    @Provides
    public LocationManager providesLocationManager(LocationProvider locationProvider,
                                                   ExecutorDispatcher executorDispatcher,
                                                   UserDataHolder userDataHolder, Retrofit retrofit,
                                                   Context context) {
        LocationAPIProvider locationAPIProvider = retrofit.create(LocationAPIProvider.class);
        return new LocationManager(locationProvider, locationAPIProvider, executorDispatcher,
                userDataHolder, context);
    }


}
