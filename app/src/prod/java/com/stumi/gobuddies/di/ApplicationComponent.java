package com.stumi.gobuddies.di;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author stumpfb on 24/01/2017.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent extends BaseApplicationComponent {

}
