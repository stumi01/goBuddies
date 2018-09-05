package com.stumi.gobuddies.ui.floating.tabs;

import com.stumi.gobuddies.system.LocationManager;
import com.stumi.gobuddies.system.location.LocationUpdateModel;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author stumpfb on 30/12/2016.
 */
public class TabsPresenterTest {

    @Mock
    EventBus eventBus;

    @Mock
    LocationManager locationManager;

    private TabsPresenter presenter;

    @Mock
    LocationUpdateModel model;

    @Mock
    TabsView view;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new TabsPresenter(eventBus, locationManager);
    }

    @Test
    public void testUpdate_without_team() throws Exception {
        //Given

        //When
        when(model.getGroupMembersCount()).thenReturn(1);

        presenter.attachView(view);
        presenter.accept(model);
        //Then
        verify(view).disablePartyTabs();
    }

    @Test
    public void testUpdate_with_team() throws Exception {
        //Given

        //When
        when(model.getGroupMembersCount()).thenReturn(2);

        presenter.attachView(view);
        presenter.accept(model);
        //Then
        verify(view).enablePartyTabs();
    }
}