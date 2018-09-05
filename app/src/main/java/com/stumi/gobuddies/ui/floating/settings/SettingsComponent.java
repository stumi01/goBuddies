package com.stumi.gobuddies.ui.floating.settings;

import com.stumi.gobuddies.system.helper.FragmentScope;

import dagger.Subcomponent;

/**
 * @author stumpfb on 04/09/2016.
 */
@FragmentScope
@Subcomponent(modules = SettingsModule.class)
public interface SettingsComponent {

    SettingsPresenter presenter();

}
