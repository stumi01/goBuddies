package com.stumi.gobuddies.ui.floating.tabs.party;

import com.google.android.gms.maps.model.LatLng;
import com.stumi.gobuddies.model.BaseResponseModel;
import com.stumi.gobuddies.model.User;
import com.stumi.gobuddies.system.LocationManager;
import com.stumi.gobuddies.system.UserDataHolder;
import com.stumi.gobuddies.system.helper.scheduler.Ticker;
import com.stumi.gobuddies.system.network.ExecutorDispatcher;
import com.stumi.gobuddies.system.network.request.ObserverRequestExecutor;
import com.stumi.gobuddies.ui.floating.tabs.party.map.UserPosition;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.reactivex.subscribers.ResourceSubscriber;
import retrofit2.Response;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author stumpfb on 12/01/2017.
 */
public class PartyPresenterTest {

    PartyPresenter partyPresenter;

    @Mock
    PartyApiProvider partyApiProvider;

    @Mock
    ExecutorDispatcher executorDispatcher;

    @Mock
    UserDataHolder userDataHolder;

    @Mock
    LocationManager locationManager;

    @Mock
    PartyView view;

    @Mock
    User self;

    @Mock
    Ticker ticker;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        partyPresenter =
                new PartyPresenter(partyApiProvider, executorDispatcher, userDataHolder, locationManager,
                        ticker);
        when(userDataHolder.getSelf()).thenReturn(self);
        verify(userDataHolder).subscribePartyStatusUpdates(any(Consumer.class));
    }

    private void verifyNoMoreInteractionsAtAll() {
        verifyNoMoreInteractions(partyApiProvider);
        verifyNoMoreInteractions(executorDispatcher);
        verifyNoMoreInteractions(userDataHolder);
        verifyNoMoreInteractions(locationManager);
        verifyNoMoreInteractions(view);
        verifyNoMoreInteractions(self);
        verifyNoMoreInteractions(ticker);
    }

    @Test
    public void testLoadPreviousMessages() throws Exception {
        //Given
        Single<Response<Party>> single = Mockito.mock(Single.class);

        //When
        when(partyApiProvider.loadAllPreviousMessages()).thenReturn(single);
        partyPresenter.loadPreviousMessages();

        //Then
        verify(partyApiProvider).loadAllPreviousMessages();
        verify(executorDispatcher).executeForMainThread(eq(single),
                any(ObserverRequestExecutor.BaseResponseObserverListener.class));
        verifyNoMoreInteractionsAtAll();
    }

    @Test
    public void testLoadPreviousMessages_response_error() throws Exception {
        //Given
        Single<Response<Party>> single = Mockito.mock(Single.class);

        final ObserverRequestExecutor.BaseResponseObserverListener<Party>[] listeners =
                extractListener(single);
        //When
        when(partyApiProvider.loadAllPreviousMessages()).thenReturn(single);
        partyPresenter.attachView(view);
        partyPresenter.loadPreviousMessages();
        listeners[0].callbackError(1, "");

        //Then
        verify(partyApiProvider).loadAllPreviousMessages();
        verify(executorDispatcher).executeForMainThread(eq(single),
                any(ObserverRequestExecutor.BaseResponseObserverListener.class));
        verifyNoMoreInteractionsAtAll();
    }

    @Test
    public void testLoadPreviousMessages_response_success_isLeader_showMap() throws Exception {
        //Given
        Single<Response<Party>> single = Mockito.mock(Single.class);
        Party party = Mockito.mock(Party.class);
        LatLng latlong = new LatLng(0, 0);

        final ObserverRequestExecutor.BaseResponseObserverListener<Party>[] listeners =
                extractListener(single);
        //When
        when(locationManager.getUserLocation()).thenReturn(latlong);
        when(party.getMemberPositions()).thenReturn(Collections.emptyList());
        when(partyApiProvider.loadAllPreviousMessages()).thenReturn(single);

        partyPresenter.attachView(view);
        partyPresenter.loadPreviousMessages();
        listeners[0].callbackSuccess(party);

        //Then
        verify(partyApiProvider).loadAllPreviousMessages();
        verify(executorDispatcher).executeForMainThread(eq(single),
                any(ObserverRequestExecutor.BaseResponseObserverListener.class));
        verify(locationManager).getUserLocation();
        verify(view).showContent();
        verify(view).setMessages(party.getMessages());
        verify(view).showMap();
        verify(view).setMapDataForLeader(eq(Collections.emptyList()), eq(latlong));
        verify(ticker).start(anyInt(), any(ResourceSubscriber.class));
        verifyNoMoreInteractionsAtAll();
    }

    @Test
    public void testLoadPreviousMessages_response_success_isLeader_dontShowMap() throws Exception {
        //Given
        Single<Response<Party>> single = Mockito.mock(Single.class);
        Party party = Mockito.mock(Party.class);
        LatLng latlong = new LatLng(0, 0);

        final ObserverRequestExecutor.BaseResponseObserverListener<Party>[] listeners =
                extractListener(single);
        //When
        when(locationManager.getUserLocation()).thenReturn(latlong);
        when(party.getMemberPositions()).thenReturn(Collections.emptyList());
        when(partyApiProvider.loadAllPreviousMessages()).thenReturn(single);
        partyPresenter.attachView(view);
        partyPresenter.keyboardVisibilityChange(true);
        partyPresenter.loadPreviousMessages();
        listeners[0].callbackSuccess(party);

        //Then
        verify(partyApiProvider).loadAllPreviousMessages();
        verify(executorDispatcher).executeForMainThread(eq(single),
                any(ObserverRequestExecutor.BaseResponseObserverListener.class));
        verify(locationManager).getUserLocation();
        verify(view).hideMap();
        verify(view).showContent();
        verify(view).setMessages(party.getMessages());
        verify(view).setMapDataForLeader(eq(Collections.emptyList()), eq(latlong));
        verify(ticker).start(anyInt(), any(ResourceSubscriber.class));
        verifyNoMoreInteractionsAtAll();
    }

    @Test
    public void testLoadPreviousMessages_response_success_isNotLeader_showMap() throws Exception {
        //Given
        Single<Response<Party>> single = Mockito.mock(Single.class);
        Party party = Mockito.mock(Party.class);
        LatLng latlong = new LatLng(0, 0);
        UserPosition userPos = Mockito.mock(UserPosition.class);

        final ObserverRequestExecutor.BaseResponseObserverListener<Party>[] listeners =
                extractListener(single);
        //When
        when(locationManager.getUserLocation()).thenReturn(latlong);
        when(party.getLeaderPosition()).thenReturn(userPos);
        when(partyApiProvider.loadAllPreviousMessages()).thenReturn(single);
        partyPresenter.attachView(view);
        partyPresenter.loadPreviousMessages();
        listeners[0].callbackSuccess(party);

        //Then
        verify(partyApiProvider).loadAllPreviousMessages();
        verify(executorDispatcher).executeForMainThread(eq(single),
                any(ObserverRequestExecutor.BaseResponseObserverListener.class));
        verify(locationManager).getUserLocation();
        verify(view).showContent();
        verify(view).setMessages(party.getMessages());
        verify(view).showMap();
        verify(view).setMapDataForMembers(eq(userPos), eq(latlong));
        verify(ticker).start(anyInt(), any(ResourceSubscriber.class));
        verifyNoMoreInteractionsAtAll();
    }

    @Test
    public void testKeyboardVisibilityChange_true() throws Exception {
        //Given

        //When
        partyPresenter.attachView(view);
        partyPresenter.keyboardVisibilityChange(true);

        //Then
        verify(view).hideMap();
        verifyNoMoreInteractionsAtAll();
    }

    @Test
    public void testKeyboardVisibilityChange_false() throws Exception {
        //Given

        //When
        partyPresenter.attachView(view);
        partyPresenter.keyboardVisibilityChange(false);

        //Then
        verify(view).showMap();
        verifyNoMoreInteractionsAtAll();
    }

    @Test
    public void testSendMessage() throws Exception {
        //Given
        Single<Response<BaseResponseModel>> single = Mockito.mock(Single.class);
        String message = "";
        //When
        when(partyApiProvider.sendMessage(anyString())).thenReturn(single);
        partyPresenter.sendMessage(message);

        //Then
        verify(partyApiProvider).sendMessage(eq(message));
        verify(executorDispatcher).executeForMainThread(eq(single),
                any(ObserverRequestExecutor.BaseResponseObserverListener.class));
        verifyNoMoreInteractionsAtAll();

    }

    @Test
    public void testSendMessage_callbackError() throws Exception {
        //Given
        Single<Response<BaseResponseModel>> single = Mockito.mock(Single.class);
        String message = "";

        final ObserverRequestExecutor.BaseResponseObserverListener<BaseResponseModel>[] listeners =
                extractListener(single);
        //When
        when(partyApiProvider.sendMessage(anyString())).thenReturn(single);
        partyPresenter.attachView(view);
        partyPresenter.sendMessage(message);
        listeners[0].callbackError(1, "");

        //Then
        verify(partyApiProvider).sendMessage(eq(message));
        verify(executorDispatcher).executeForMainThread(eq(single),
                any(ObserverRequestExecutor.BaseResponseObserverListener.class));
        verify(view).showMessageSendError(eq(1));
        verifyNoMoreInteractionsAtAll();
    }

    @Test
    public void testSendMessage_callbackSuccess() throws Exception {
        //Given
        Single<Response<BaseResponseModel>> single = Mockito.mock(Single.class);
        String message = "";
        BaseResponseModel response = Mockito.mock(BaseResponseModel.class);

        final ObserverRequestExecutor.BaseResponseObserverListener<BaseResponseModel>[] listeners =
                extractListener(single);
        //When
        when(partyApiProvider.sendMessage(anyString())).thenReturn(single);
        partyPresenter.attachView(view);
        partyPresenter.sendMessage(message);
        listeners[0].callbackSuccess(response);

        //Then
        verify(partyApiProvider).sendMessage(eq(message));
        verify(executorDispatcher).executeForMainThread(eq(single),
                any(ObserverRequestExecutor.BaseResponseObserverListener.class));
        verify(view).updateMessages(any());
        verify(view).showMessageSendSuccess();
        verify(userDataHolder).getSelf();
        verify(self).getName();
        verify(self).isLeader();
        verify(self).getTeam();
        verifyNoMoreInteractionsAtAll();
    }

    private <T> ObserverRequestExecutor.BaseResponseObserverListener<T>[] extractListener(
            Single<Response<T>> single) {
        final ObserverRequestExecutor.BaseResponseObserverListener<T>[] listBaseResponseObserverListener =
                new ObserverRequestExecutor.BaseResponseObserverListener[1];

        doAnswer(invocation -> {
            listBaseResponseObserverListener[0] =
                    (ObserverRequestExecutor.BaseResponseObserverListener<T>) invocation.getArguments()[1];
            return null;
        }).when(executorDispatcher).executeForMainThread(eq(single),
                any(ObserverRequestExecutor.BaseResponseObserverListener.class));

        return listBaseResponseObserverListener;
    }

}