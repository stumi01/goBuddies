package com.stumi.gobuddies.ui.floating.tabs.partyMembers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stumi.gobuddies.GoBuddiesApplication;
import com.stumi.gobuddies.R;
import com.stumi.gobuddies.base.mvp.BaseMVPLCELayout;
import com.stumi.gobuddies.model.User;
import com.stumi.gobuddies.system.animation.ButtonAnimationOnTouchListener;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnEditorAction;

/**
 * Created by sourc on 2017. 03. 08..
 */

public class PartyMembersLayout extends
        BaseMVPLCELayout<LinearLayout, PartyMembers, PartyMembersView, PartyMembersPresenter>
        implements PartyMembersView, PartyMemberAdapter.PassButtonClickListener {

    @BindView(R.id.recycleView)
    RecyclerView recyclerView;

    @BindView(R.id.team_name)
    TextView teamNameView;

    @BindView(R.id.team_name_edit)
    TextView teamNameEditView;

    @BindView(R.id.team_name_change)
    ImageButton teamNameChange;

    @BindView(R.id.team_name_change_buttons)
    View teamNameEditButtons;

    @BindView(R.id.team_name_change_ok)
    ImageButton teamNameChangeOk;

    @BindView(R.id.team_name_change_cancel)
    ImageButton teamNameChangeCancel;

    @BindView(R.id.leave_button)
    Button leaveParty;

    private PartyMemberAdapter adapter;

    //private ToastManager toastManager;

    public PartyMembersLayout(@NonNull Context context) {
        super(context);
        leaveParty.setOnTouchListener(new ButtonAnimationOnTouchListener(getContext()));
        initRecycleView();
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return null;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_party_members;
    }

    @NonNull
    @Override
    public PartyMembersPresenter createPresenter() {
        return GoBuddiesApplication.get(getContext()).getFloatingComponent().plusPartyMembersComponent().presenter();
    }

    private void initRecycleView() {
        adapter = new PartyMemberAdapter(getContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.leave_button)
    public void onLeaveClick() {
        presenter.leaveParty();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        loadData(false);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.onPause();
    }

    @Override
    public void setData(PartyMembers data) {
        changeTeamNameEditButtonVisibility(data.getIsCurrentUserPartyLeader());
        adapter.setData(data);
        adapter.notifyDataSetChanged();
        showContent();
    }

    private void changeTeamNameEditButtonVisibility(boolean isCurrentUserPartyLeader) {
        teamNameChange.setVisibility(isCurrentUserPartyLeader ? View.VISIBLE : View.GONE);
        teamNameView.setClickable(isCurrentUserPartyLeader);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        showLoading(pullToRefresh);
        presenter.loadTeamData();
    }

    @OnClick(R.id.team_name)
    public void onTeamNameEditFromTextView() {
        onTeamNameEdit();
    }

    @OnClick(R.id.team_name_change)
    public void onTeamNameEditFromPencil() {
        onTeamNameEdit();
    }

    private void onTeamNameEdit() {
        teamNameEditView.setText(teamNameView.getText());
        teamNameView.setVisibility(View.GONE);
        teamNameEditView.setVisibility(View.VISIBLE);

        teamNameChange.setVisibility(View.GONE);
        teamNameEditButtons.setVisibility(View.VISIBLE);

        teamNameEditView.setSelectAllOnFocus(true);
        teamNameEditView.requestFocus();
        teamNameEditView.postDelayed(() -> {
            InputMethodManager keyboard =
                    (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.showSoftInput(teamNameEditView, 0);
        }, 200);
    }

    @OnClick(R.id.team_name_change_ok)
    public void onTeamNameChange() {
        presenter.changeTeamName(teamNameEditView.getText().toString());
        teamNameEditView.clearFocus();

        onTeamNameChangeCancel();
    }

    @OnClick(R.id.team_name_change_cancel)
    public void onTeamNameChangeCancel() {
        teamNameEditView.setText("");

        teamNameEditView.setVisibility(View.GONE);
        teamNameView.setVisibility(View.VISIBLE);

        teamNameEditButtons.setVisibility(View.GONE);
        teamNameChange.setVisibility(View.VISIBLE);

        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(teamNameEditView.getWindowToken(), 0);
        }
    }

    @OnEditorAction(R.id.team_name_edit)
    public boolean onTeamNameAction(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            onTeamNameChange();

        }
        return false;
    }

    @Override
    public void onPassClick(User user) {
        presenter.passLeaderTo(user);
    }

    @Override
    public void onKickClick(User user) {
        presenter.kickUser(user);
    }

    @Override
    public void setTeamName(String teamName) {
        teamNameView.setText(teamName);
    }

    @Override
    public void showRequestError(int errorCode) {
/*
        toastManager.errorLoading(
                String.format(Locale.ENGLISH, "Error during request (%d) \n Please try again later",
                        errorCode));
*/
    }

    @Override
    public void setFieldsEnabled(boolean enabled) {
        recyclerView.setEnabled(enabled);
        teamNameView.setEnabled(enabled);
        teamNameChange.setEnabled(enabled);
        leaveParty.setEnabled(enabled);
    }

    @Override
    public boolean isTeamNameEditing() {
        return teamNameView.hasFocus();
    }

    @Override
    public void showSuccess() {
        //toastManager.successLoading();
    }

    @Override
    public void showLoadingRequest() {
        //toastManager.showLoading();
    }
}
