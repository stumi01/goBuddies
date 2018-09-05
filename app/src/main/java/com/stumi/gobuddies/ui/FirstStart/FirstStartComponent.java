package com.stumi.gobuddies.ui.firstStart;

import com.stumi.gobuddies.di.ApplicationComponent;
import com.stumi.gobuddies.system.helper.ActivityScope;

import dagger.Component;

/**
 * @author stumpfb on 14/08/2016.
 */
@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = FirstStartModule.class)
public interface FirstStartComponent {

    FirstStartPresenter presenter();

    void inject(FirstStartActivity activity);
}
