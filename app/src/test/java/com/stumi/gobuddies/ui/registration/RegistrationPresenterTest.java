package com.stumi.gobuddies.ui.registration;

import com.stumi.gobuddies.model.User;
import com.stumi.gobuddies.system.IntentManager;
import com.stumi.gobuddies.system.UserDataHolder;
import com.stumi.gobuddies.system.network.ExecutorDispatcher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author stumpfb on 27/12/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class RegistrationPresenterTest {

    @Mock
    IntentManager intentManager;

    @Mock
    UserDataHolder userDataHolder;

    @Mock
    RegistrationAPIProvider registrationApiProvider;

    @Mock
    RegistrationView registrationView;

    @Mock
    ExecutorDispatcher executorDispatcher;

    private RegistrationPresenter registrationPresenter;

    private String id = "id";

    private String name = "name";

    private String name_empty = "";

    private int team = 1;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        registrationPresenter =
                new RegistrationPresenter(registrationApiProvider, executorDispatcher, userDataHolder,
                        intentManager);
    }

    @Test
    public void testRegisterClickWithAllData() throws Exception {
        //Given
        registrationPresenter.attachView(registrationView);
        //When
        registrationPresenter.registerClick(team, name);
        //Then
        verify(registrationView).showLoading();
        verify(registrationApiProvider).registerUser(name, team);
    }

    @Test
    public void testRegisterClickEmptyName() throws Exception {
        //Given
        registrationPresenter.attachView(registrationView);
        //When
        registrationPresenter.registerClick(team, name_empty);
        //Then
        verify(registrationView).showValidationError();
    }

    @Test
    public void testRegisterClickWithoutName() throws Exception {
        //Given
        String nickname = null;
        registrationPresenter.attachView(registrationView);
        //When
        registrationPresenter.registerClick(team, nickname);
        //Then
        verify(registrationView).showValidationError();
    }

    @Test
    public void testRegisterClickWithoutView() throws Exception {
        //Given
        //When
        registrationPresenter.registerClick(team, name_empty);
        //Then
        verifyNoMoreInteractions(registrationView);
        verifyNoMoreInteractions(registrationApiProvider);
    }

    @Test
    public void testCallbackSuccess() throws Exception {
        //Given
        RegistrationModel registrationModel = new RegistrationModel();
        registrationModel.setOk(id);

        //When
        registrationPresenter.attachView(registrationView);
        when(registrationView.getName()).thenReturn(name);
        when(registrationView.getTeam()).thenReturn(team);
        registrationPresenter.callbackSuccess(registrationModel);

        //Then
        verify(registrationView).showSuccess();
        verify(userDataHolder).saveSelf(eq(new User(id, name, team)));
        verify(intentManager).startTeamFinderFromRegistration();
    }

    @Test
    public void testCallbackSuccessWithoutView() throws Exception {
        //Given

        RegistrationModel registrationModel = new RegistrationModel();
        registrationModel.setOk(id);

        //When
        registrationPresenter.callbackSuccess(registrationModel);

        //Then
        verifyNoMoreInteractions(registrationView);
        verifyNoMoreInteractions(userDataHolder);
        verify(intentManager).startTeamFinderFromRegistration();
    }
}