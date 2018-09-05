package com.stumi.gobuddies.ui.firstStart;

import android.app.Activity;

import com.stumi.gobuddies.BuildConfig;
import com.stumi.gobuddies.system.IntentManager;
import com.stumi.gobuddies.system.UserDataHolder;
import com.stumi.gobuddies.system.network.ExecutorDispatcher;
import com.stumi.gobuddies.system.network.request.ObserverRequestExecutor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Single;

import static com.stumi.gobuddies.system.network.ErrorCodesConstant.UN_AUTHORIZED;
import static info.solidsoft.mockito.java8.AssertionMatcher.assertArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author stumpfb on 30/12/2016.
 */
public class FirstStartPresenterTest {

    @Mock
    IntentManager intentManager;

    @Mock
    UserDataHolder userDataHolder;

    @Mock
    ExecutorDispatcher executorDispatcher;

    @Mock
    HandshakeAPIProvider handshakeAPIProvider;

    @Mock
    Single<retrofit2.Response<com.stumi.gobuddies.ui.firstStart.ValidationModel>> mockSingle;

    @Mock
    FirstStartActivity mockView;

    @Mock
    ValidationModel mockBody;

    @Mock
    Activity mockActivity;

    private FirstStartPresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(mockView.getActivity()).thenReturn(mockActivity);
        presenter = new FirstStartPresenter(handshakeAPIProvider, executorDispatcher, userDataHolder,
                intentManager);
    }

    @Test
    public void tesStartApp_noToken() throws Exception {
        //Given

        //When
        presenter.attachView(mockView);
        when(userDataHolder.isTokenExists()).thenReturn(false);
        presenter.startApp();
        //Then
        verify(mockView).showSuccess(assertArg(l -> intentManager.startRegistrationActivity()));
    }

    @Test
    public void tesStartApp_Token() throws Exception {
        //Given

        //When
        when(userDataHolder.isTokenExists()).thenReturn(true);

        when(userDataHolder.getToken()).thenReturn("TOKEN");
        when(handshakeAPIProvider.validation()).thenReturn(mockSingle);

        presenter.startApp();
        //Then
        verify(executorDispatcher).executeForMainThread(any(Single.class),
                any(ObserverRequestExecutor.BaseResponseObserverListener.class));
    }

    @Test
    public void testCallbackError_UN_AUTHORIZED() throws Exception {
        //Given

        //When
        presenter.attachView(mockView);
        presenter.callbackError(UN_AUTHORIZED, "message");

        //Then
        verify(mockView).showSuccess(assertArg(l -> intentManager.startRegistrationActivity()));
    }

    @Test
    public void testCallbackError_unknownError() throws Exception {
        //Given

        //When
        presenter.attachView(mockView);
        presenter.callbackError(-1, "message");

        //Then
        verify(mockView).showGrantErrorAndExit();
        verifyNoMoreInteractions(intentManager);
    }

    @Test
    public void testCallbackSuccess_has_never_version() throws Exception {
        //Given

        //When
        presenter.attachView(mockView);
        when(mockBody.getVersion()).thenReturn(BuildConfig.VERSION_CODE + 1);
        presenter.callbackSuccess(mockBody);

        //Then
        verify(mockView).showSuccess(assertArg(l -> intentManager.startGooglePlayForUpdate()));
    }

    @Test
    public void testCallbackSuccess_valid_token() throws Exception {
        //Given

        //When
        presenter.attachView(mockView);
        when(mockBody.getVersion()).thenReturn(BuildConfig.VERSION_CODE);
        when(mockBody.isValid()).thenReturn(true);
        presenter.callbackSuccess(mockBody);

        //Then
        verify(mockView).showSuccessAndHide(assertArg(l -> intentManager.startTeamFinder(eq(0), eq(0))));
    }

    @Test
    public void testCallbackSuccess_invalid_token() throws Exception {
        //Given

        //When
        presenter.attachView(mockView);
        when(mockBody.getVersion()).thenReturn(BuildConfig.VERSION_CODE);
        when(mockBody.isValid()).thenReturn(false);
        presenter.callbackSuccess(mockBody);

        //Then
        verify(mockView).showSuccess(assertArg(l -> intentManager.startRegistrationActivity()));
    }
}