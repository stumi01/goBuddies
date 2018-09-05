package com.stumi.gobuddies.di;

import android.content.Context;

import com.stumi.gobuddies.system.LocationManager;
import com.stumi.gobuddies.system.UserDataHolder;
import com.stumi.gobuddies.system.helper.MockServer;
import com.stumi.gobuddies.system.location.DevelopLocationAPIProvider;
import com.stumi.gobuddies.system.location.LocationAPIProvider;
import com.stumi.gobuddies.system.location.LocationProvider;
import com.stumi.gobuddies.system.network.ExecutorDispatcher;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author stumpfb on 26/08/2016.
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
                                                   UserDataHolder userTokenHolder, MockServer mockServer, Context context) {
        LocationAPIProvider locationAPIProvider = new DevelopLocationAPIProvider(mockServer);
        return new LocationManager(locationProvider, locationAPIProvider, executorDispatcher,
                userTokenHolder, context);
    }

    @Singleton
    @Provides
    public MockServer providesMockServer() {
        return new MockServer();
    }
}
