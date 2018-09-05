package com.stumi.gobuddies.ui.registration;

import com.stumi.gobuddies.di.ApplicationComponent;
import com.stumi.gobuddies.system.helper.ActivityScope;

import dagger.Component;

/**
 * @author stumpfb on 30/07/2016.
 */
@ActivityScope
@Component(modules = { RegistrationModule.class},dependencies = ApplicationComponent.class)
public interface RegistrationComponent {

    RegistrationPresenter presenter();

    void inject(RegistrationActivity activity);
}
