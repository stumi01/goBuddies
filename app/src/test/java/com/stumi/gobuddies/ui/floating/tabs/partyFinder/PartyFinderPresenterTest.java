package com.stumi.gobuddies.ui.floating.tabs.partyFinder;

import com.stumi.gobuddies.model.BaseResponseModel;
import com.stumi.gobuddies.model.User;
import com.stumi.gobuddies.system.IntentManager;
import com.stumi.gobuddies.system.LocationManager;
import com.stumi.gobuddies.system.helper.scheduler.Ticker;
import com.stumi.gobuddies.system.helper.scheduler.TickerImmediateStartSubscriber;
import com.stumi.gobuddies.system.network.ExecutorDispatcher;
import com.stumi.gobuddies.system.network.request.ObserverRequestExecutor;
import com.stumi.gobuddies.ui.floating.tabs.partyFinder.party.PartyDetails;
import com.stumi.gobuddies.ui.floating.tabs.partyFinder.party.PartyRequestStatus;
import com.stumi.gobuddies.ui.floating.tabs.partyMembers.PartyMembers;
import com.stumi.gobuddies.ui.floating.tabs.partyMembers.PartyMembersApiProvider;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;

import static com.stumi.gobuddies.ui.floating.tabs.partyFinder.PartyFinderPresenter.REFRESH_SECONDS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author stumpfb on 30/12/2016.
 */
public class PartyFinderPresenterTest {

    @Mock
    LocationManager locationManager;

    @Mock
    IntentManager intentManager;

    @Mock
    ExecutorDispatcher executorDispatcher;

    @Mock
    PartyMembersApiProvider partyMembersApiProvider;

    @Mock
    PartyFinderApiProvider partyFinderProvider;

    @Mock
    PartyFinderView view;

    @Mock
    Single<Response<BaseResponseModel>> mockSingle;

    @Mock
    PartyDetails mockPartyDetails;

    @Mock
    Ticker mockTicker;

    private PartyFinderPresenter presenter;

    @Mock
    EventBus mockEventBus;

    private void verifyNoMoreInteractionsAtAll() {
        verifyNoMoreInteractions(locationManager);
        verifyNoMoreInteractions(intentManager);
        verifyNoMoreInteractions(executorDispatcher);
        verifyNoMoreInteractions(partyMembersApiProvider);
        verifyNoMoreInteractions(partyFinderProvider);
        verifyNoMoreInteractions(view);
        verifyNoMoreInteractions(mockSingle);
        verifyNoMoreInteractions(mockPartyDetails);
        verifyNoMoreInteractions(mockTicker);
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new PartyFinderPresenter(partyFinderProvider, partyMembersApiProvider, executorDispatcher,
                intentManager, locationManager, mockTicker, mockEventBus);
    }

    @Test
    public void testJoinToPartyClick_NotAvailable() throws Exception {
        //Given

        //When
        when(mockPartyDetails.getTeamRequestStatus()).thenReturn(PartyRequestStatus.NotAvailable);
        presenter.attachView(view);
        presenter.joinToPartyClick(mockPartyDetails);

        //Then
        verify(mockPartyDetails).getTeamRequestStatus();

        verifyNoMoreInteractionsAtAll();
    }

    @Test
    public void testJoinToPartyClick_RequestAvailable() throws Exception {
        //Given

        //When
        when(mockPartyDetails.getTeamRequestStatus()).thenReturn(PartyRequestStatus.RequestAvailable);

        when(mockPartyDetails.getId()).thenReturn("ID");
        when(partyFinderProvider.doJoinRequest(anyString())).thenReturn(mockSingle);

        presenter.attachView(view);
        presenter.joinToPartyClick(mockPartyDetails);

        //Then
        verify(mockPartyDetails).getTeamRequestStatus();
        verify(mockPartyDetails).getId();
        verify(executorDispatcher).executeForMainThread(any(Single.class),
                any(ObserverRequestExecutor.BaseResponseObserverListener.class));
        verify(partyFinderProvider).doJoinRequest(anyString());
        verifyNoMoreInteractionsAtAll();
    }

    @Test
    public void testJoinToPartyClick_RequestAcceptable() throws Exception {
        //Given

        //When
        when(mockPartyDetails.getTeamRequestStatus()).thenReturn(PartyRequestStatus.RequestAcceptable);

        when(mockPartyDetails.getId()).thenReturn("ID");
        Single<Response<AcceptJoinResponseModel>> single = Mockito.mock(Single.class);
        when(partyFinderProvider.acceptJoinRequest(anyString())).thenReturn(single);

        presenter.attachView(view);
        presenter.joinToPartyClick(mockPartyDetails);

        //Then
        verify(mockPartyDetails).getTeamRequestStatus();
        verify(mockPartyDetails).getId();

        verify(executorDispatcher).executeForMainThread(any(Single.class),
                any(ObserverRequestExecutor.BaseResponseObserverListener.class));
        verify(partyFinderProvider).acceptJoinRequest(anyString());
        verifyNoMoreInteractionsAtAll();
    }

    @Test
    public void testLoadParties() throws Exception {
        //Given

        //When

        Single<Response<List<PartyDetails>>> single = Mockito.mock(Single.class);
        when(partyFinderProvider.loadCloseParties()).thenReturn(single);

        presenter.attachView(view);
        presenter.loadParties(true);

        //Then
        verify(view).showUpdating();
        verify(executorDispatcher).executeForMainThread(any(Single.class),
                any(ObserverRequestExecutor.BaseResponseObserverListener.class));
        verify(partyFinderProvider).loadCloseParties();

        verifyNoMoreInteractionsAtAll();
    }

    @Test
    public void testLoadParties_callback_error() throws Exception {
        //Given
        Single<Response<List<PartyDetails>>> single = Mockito.mock(Single.class);

        final ObserverRequestExecutor.BaseResponseObserverListener<List<PartyDetails>>[]
                listBaseResponseObserverListener = extractListener(single);

        //When
        when(partyFinderProvider.loadCloseParties()).thenReturn(single);

        presenter.attachView(view);
        presenter.loadParties(true);
        listBaseResponseObserverListener[0].callbackError(1, "errooooor");

        //Then
        verify(view).showUpdating();
        verify(executorDispatcher).executeForMainThread(any(Single.class),
                any(ObserverRequestExecutor.BaseResponseObserverListener.class));
        verify(partyFinderProvider).loadCloseParties();
        verify(view).hideUpdating();

        verifyNoMoreInteractionsAtAll();
    }

    @Test
    public void testLoadParties_callback_success_empty() throws Exception {
        //Given
        Single<Response<List<PartyDetails>>> single = Mockito.mock(Single.class);

        final ObserverRequestExecutor.BaseResponseObserverListener<List<PartyDetails>>[]
                listBaseResponseObserverListener = extractListener(single);

        //When
        when(partyFinderProvider.loadCloseParties()).thenReturn(single);

        presenter.attachView(view);
        presenter.loadParties(true);
        listBaseResponseObserverListener[0].callbackSuccess(Collections.EMPTY_LIST);

        //Then
        verify(view).showUpdating();
        verify(executorDispatcher).executeForMainThread(any(Single.class),
                any(ObserverRequestExecutor.BaseResponseObserverListener.class));
        verify(partyFinderProvider).loadCloseParties();
        verify(view).showLoading(false);

        verifyNoMoreInteractionsAtAll();
    }

    @Test
    public void testLoadParties_callback_success() throws Exception {
        //Given
        Single<Response<List<PartyDetails>>> single = Mockito.mock(Single.class);

        final ObserverRequestExecutor.BaseResponseObserverListener<List<PartyDetails>>[]
                listBaseResponseObserverListener = extractListener(single);

        //When
        when(partyFinderProvider.loadCloseParties()).thenReturn(single);

        presenter.attachView(view);
        presenter.loadParties(true);
        listBaseResponseObserverListener[0].callbackSuccess(Arrays.asList(mockPartyDetails));

        //Then
        verify(view).showUpdating();
        verify(executorDispatcher).executeForMainThread(any(Single.class),
                any(ObserverRequestExecutor.BaseResponseObserverListener.class));
        verify(partyFinderProvider).loadCloseParties();
        verify(view).hideUpdating();
        verify(view).setData(eq(Arrays.asList(mockPartyDetails)));

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

    @Test
    public void testLoadPartiesPeriodically() throws Exception {
        //Given

        //When
        presenter.attachView(view);
        presenter.loadPartiesPeriodically();

        //Then
        verify(view).showLoading(false);
        verify(mockTicker).start(eq(REFRESH_SECONDS), any(TickerImmediateStartSubscriber.class));
        verifyNoMoreInteractionsAtAll();
    }

    @Test
    public void testPartyExpandClick() throws Exception {
        //Given
        String partyId = "123";
        Single<Response<PartyMembers>> single = Mockito.mock(Single.class);

        //When
        presenter.attachView(view);

        when(partyMembersApiProvider.loadTeamMembers(eq(partyId))).thenReturn(single);
        final Single<List<User>> listSingle = presenter.partyExpandClick(partyId);
        listSingle.subscribe();

        //Then
        verify(executorDispatcher).executeForMainThread(any(Single.class),
                any(ObserverRequestExecutor.BaseResponseObserverListener.class));
        verify(partyMembersApiProvider).loadTeamMembers(eq(partyId));

        verifyNoMoreInteractionsAtAll();
    }

    @Test
    public void testDoJoinAccept() throws Exception {
        //Given
        String partyId = "123";
        Single<Response<AcceptJoinResponseModel>> single = Mockito.mock(Single.class);

        //When
        when(mockPartyDetails.getId()).thenReturn(partyId);
        when(partyFinderProvider.acceptJoinRequest(partyId)).thenReturn(single);
        presenter.attachView(view);
        presenter.doJoinAccept(mockPartyDetails);

        //Then
        verify(mockPartyDetails).getId();
        verify(executorDispatcher).executeForMainThread(any(Single.class),
                any(ObserverRequestExecutor.BaseResponseObserverListener.class));
        verify(partyFinderProvider).acceptJoinRequest(eq(partyId));

        verifyNoMoreInteractionsAtAll();
    }

    @Test
    public void testDoJoinAccept_error() throws Exception {
        //Given
        String partyId = "123";
        Single<Response<AcceptJoinResponseModel>> single = Mockito.mock(Single.class);
        Single<Response<List<PartyDetails>>> singleForLoadParties = Mockito.mock(Single.class);


        final ObserverRequestExecutor.BaseResponseObserverListener<AcceptJoinResponseModel>[]
                listBaseResponseObserverListener = extractListener(single);

        //When
        when(mockPartyDetails.getId()).thenReturn(partyId);
        when(partyFinderProvider.acceptJoinRequest(partyId)).thenReturn(single);
        when(partyFinderProvider.loadCloseParties()).thenReturn(singleForLoadParties);

        presenter.attachView(view);
        presenter.doJoinAccept(mockPartyDetails);
        listBaseResponseObserverListener[0].callbackError(1, "errooor");


        //Then
        verify(mockPartyDetails).getId();
        verify(partyFinderProvider).acceptJoinRequest(eq(partyId));
        verify(partyFinderProvider).loadCloseParties();
        verify(executorDispatcher, times(2)).executeForMainThread(any(Single.class),
                any(ObserverRequestExecutor.BaseResponseObserverListener.class));
        verify(view).showUpdating();

        verifyNoMoreInteractionsAtAll();
    }

    @Test
    public void testDoJoinAccept_success_withLead() throws Exception {
        //Given
        String partyId = "123";
        Single<Response<AcceptJoinResponseModel>> single = Mockito.mock(Single.class);
        Single<Response<List<PartyDetails>>> singleForLoadParties = Mockito.mock(Single.class);


        final ObserverRequestExecutor.BaseResponseObserverListener<AcceptJoinResponseModel>[]
                listBaseResponseObserverListener = extractListener(single);

        AcceptJoinResponseModel acceptJoinResponseModel = Mockito.mock(AcceptJoinResponseModel.class);

        //When
        when(mockPartyDetails.getId()).thenReturn(partyId);
        when(partyFinderProvider.acceptJoinRequest(partyId)).thenReturn(single);
        when(partyFinderProvider.loadCloseParties()).thenReturn(singleForLoadParties);
        when(acceptJoinResponseModel.isLead()).thenReturn(true);

        presenter.attachView(view);
        presenter.doJoinAccept(mockPartyDetails);
        listBaseResponseObserverListener[0].callbackSuccess(acceptJoinResponseModel);

        //Then
        verify(mockPartyDetails).getId();
        verify(partyFinderProvider).acceptJoinRequest(eq(partyId));
        verify(partyFinderProvider).loadCloseParties();
        verify(executorDispatcher, times(2)).executeForMainThread(any(Single.class),
                any(ObserverRequestExecutor.BaseResponseObserverListener.class));
        verify(view).showUpdating();

        verifyNoMoreInteractionsAtAll();
    }

    @Test
    public void testDoJoinAccept_success_withoutLead() throws Exception {
        //Given
        String partyId = "123";
        Single<Response<AcceptJoinResponseModel>> single = Mockito.mock(Single.class);


        final ObserverRequestExecutor.BaseResponseObserverListener<AcceptJoinResponseModel>[]
                listBaseResponseObserverListener = extractListener(single);

        AcceptJoinResponseModel acceptJoinResponseModel = Mockito.mock(AcceptJoinResponseModel.class);

        //When
        when(mockPartyDetails.getId()).thenReturn(partyId);
        when(partyFinderProvider.acceptJoinRequest(partyId)).thenReturn(single);
        when(acceptJoinResponseModel.isLead()).thenReturn(false);

        presenter.attachView(view);
        presenter.doJoinAccept(mockPartyDetails);
        listBaseResponseObserverListener[0].callbackSuccess(acceptJoinResponseModel);

        //Then
        verify(mockPartyDetails).getId();
        verify(partyFinderProvider).acceptJoinRequest(eq(partyId));
        verify(executorDispatcher, times(1)).executeForMainThread(any(Single.class),
                any(ObserverRequestExecutor.BaseResponseObserverListener.class));
        verify(intentManager).switchToPartyFragment();

        verifyNoMoreInteractionsAtAll();
    }

}