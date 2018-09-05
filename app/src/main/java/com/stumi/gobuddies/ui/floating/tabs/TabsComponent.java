package com.stumi.gobuddies.ui.floating.tabs;

import com.stumi.gobuddies.system.helper.FragmentScope;

import dagger.Subcomponent;

/**
 * Created by sourc on 2016. 09. 10..
 */
@FragmentScope
@Subcomponent(modules = TabsModule.class)
public interface TabsComponent {
    TabsPresenter presenter();

}
