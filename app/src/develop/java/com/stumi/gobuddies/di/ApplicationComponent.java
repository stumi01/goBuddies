package com.stumi.gobuddies.di;

import com.stumi.gobuddies.system.helper.MockServer;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author stumpfb on 27/08/2016.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent extends BaseApplicationComponent {

    MockServer providesMockServer();
}
