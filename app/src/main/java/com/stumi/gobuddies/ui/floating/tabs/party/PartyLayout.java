package com.stumi.gobuddies.ui.floating.tabs.party;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;
import com.stumi.gobuddies.GoBuddiesApplication;
import com.stumi.gobuddies.R;
import com.stumi.gobuddies.base.mvp.BaseMVPLCELayout;
import com.stumi.gobuddies.system.helper.KeyboardVisibilityEvent;
import com.stumi.gobuddies.system.helper.NotImplementedException;
import com.stumi.gobuddies.ui.floating.tabs.party.map.PartyOsmMapView;
import com.stumi.gobuddies.ui.floating.tabs.party.map.UserPosition;
import com.stumi.gobuddies.ui.floating.tabs.party.message.PartyMessage;
import com.stumi.gobuddies.ui.floating.tabs.party.message.PartyMessagesAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by sourc on 2017. 03. 08..
 */

public class PartyLayout extends
        BaseMVPLCELayout<LinearLayout, Party, PartyView, PartyPresenter>
        implements PartyView {

    @BindView(R.id.messagesView)
    RecyclerView messagesView;

    @BindView(R.id.sendMessageButton)
    ImageButton messageSendButton;

    @BindView(R.id.messageEditText)
    TextView messageContentsView;

    @BindView(R.id.mapView)
    PartyOsmMapView mapView;

    @BindView(R.id.message_more_button)
    View moreButton;

    private PartyMessagesAdapter adapter;

    public PartyLayout(@NonNull Context context) {
        super(context);
        initRecycleView();
        showLoading(true);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_party;
    }

    @Override
    public PartyPresenter createPresenter() {
        return GoBuddiesApplication.get(getContext()).getFloatingComponent().plus(new PartyModule()).presenter();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        MapsInitializer.initialize(getContext());
        loadData(false);
        KeyboardVisibilityEvent.setEventListener(context, rootView, isOpen -> presenter.keyboardVisibilityChange(isOpen));
    }


    @Override
    protected void onDetachedFromWindow() {
        presenter.stop();
        super.onDetachedFromWindow();
    }

    private void initRecycleView() {
        adapter = new PartyMessagesAdapter(getContext());
        messagesView.setLayoutManager(new LinearLayoutManager(getContext()));
        messagesView.setAdapter(adapter);
        initHideNewMessageIndicatorListener();
    }

    private void initHideNewMessageIndicatorListener() {
        RxRecyclerView.scrollEvents(messagesView).filter(event -> isLastItemDisplaying(event.view()))
                .subscribe(e -> moreButton.setVisibility(GONE));
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return null;
    }

    @Override
    public void setData(Party data) {
        throw new NotImplementedException();
    }

    @Override
    public void setMessages(List<PartyMessage> msgs) {
        showContent();
        adapter.setMessages(msgs);
        adapter.notifyDataSetChanged();
        scrollToBottom();
    }

    @Override
    public void showMessageSendError(int errorCode) {
        //TODO show error notification?
        messageContentsView.setEnabled(true);
        final Animation animShake = AnimationUtils.loadAnimation(this.getContext(), R.anim.shake_animation);
        messageContentsView.startAnimation(animShake);
    }

    @Override
    public void showMessageSendSuccess() {
        messageContentsView.setText("");
        messageContentsView.setEnabled(true);
        scrollToBottom();
    }

    @Override
    public void updateMessages(List<PartyMessage> msgs) {
        boolean scroll = isLastItemDisplaying(messagesView);
        adapter.appendMessages(msgs);
        if (scroll) {
            scrollToBottom();
        } else {
            moreButton.setVisibility(View.VISIBLE);
        }
    }

    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView
                    .getAdapter().getItemCount() - 1) {
                return true;
            }
        }
        return false;
    }

    @OnClick(R.id.message_more_button)
    public void scrollToBottom() {
        messagesView.scrollToPosition(adapter.getItemCount() - 1);
        moreButton.setVisibility(GONE);
    }

    @Override
    public void setMapDataForLeader(List<UserPosition> mems, LatLng userLocation) {
        mapView.setMapDataForLeader(mems, userLocation);
    }

    @Override
    public void setMapDataForMembers(UserPosition leader, LatLng userLocation) {
        mapView.setMapDataForMembers(leader, userLocation);
    }

    @Override
    public void setInputBackground(Integer backgroundResource) {
        messageContentsView.setBackgroundResource(backgroundResource);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        showLoading(pullToRefresh);
        presenter.loadData();
    }

    @OnClick(R.id.sendMessageButton)
    public void onMessageSendClick() {
        messageContentsView.setEnabled(false);
        String message = messageContentsView.getText().toString();
        if (message.length() > 0) {
            presenter.sendMessage(message);
        }
    }

    @Override
    public void hideMap() {
        mapView.setVisibility(GONE);
    }

    @Override
    public void showMap() {
        mapView.setVisibility(View.VISIBLE);
        scrollToBottom();
    }
}
