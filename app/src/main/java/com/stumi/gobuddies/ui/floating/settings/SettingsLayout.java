package com.stumi.gobuddies.ui.floating.settings;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.stumi.gobuddies.BuildConfig;
import com.stumi.gobuddies.GoBuddiesApplication;
import com.stumi.gobuddies.R;
import com.stumi.gobuddies.base.mvp.BaseMVPLayout;
import com.stumi.gobuddies.system.animation.ButtonAnimationOnTouchListener;
import com.stumi.gobuddies.system.widget.ToastManager;
import com.stumi.gobuddies.ui.registration.TeamSpinnerAdapter;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;

/**
 * Created by sourc on 2017. 03. 10..
 */

public class SettingsLayout extends BaseMVPLayout<SettingsView, SettingsPresenter> implements SettingsView {

    private static final String TAG = "SettingsLayout";

    @BindView(R.id.register_nickname)
    MaterialEditText nickname;

    @BindView(R.id.register_team_spinner)
    Spinner team;

    @BindView(R.id.settings_backButton)
    Button back;

    @BindView(R.id.settings_saveButton)
    Button save;

    @BindView(R.id.settings_feedbackButton)
    Button feedback;

    @BindView(R.id.versionNumber)
    TextView versionNumberView;

    private ToastManager toastManager;

    public SettingsLayout(Context context) {
        super(context);
        team.setAdapter(new TeamSpinnerAdapter(getContext(), R.id.team_select_name,
                getResources().getStringArray(R.array.team_array), LayoutInflater.from(context)));
        back.setOnTouchListener(new ButtonAnimationOnTouchListener(getContext()));
        save.setOnTouchListener(new ButtonAnimationOnTouchListener(getContext()));
        feedback.setOnTouchListener(new ButtonAnimationOnTouchListener(getContext()));

        versionNumberView.setText(context.getString(R.string.app_name) + " " + BuildConfig.VERSION_NAME);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        Log.d(TAG, "onViewAdded: ");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d(TAG, "onAttachedToWindow: ");
    }

    @Override
    public SettingsPresenter createPresenter() {
        return GoBuddiesApplication.get(getContext()).getApplicationComponent().plus().presenter();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_settings;
    }

    @OnClick(R.id.settings_backButton)
    public void onBackClick() {
        presenter.onBackClick();
    }

    @OnClick(R.id.settings_saveButton)
    public void onSaveClick() {
        presenter.onSaveClick();
    }

    @OnClick(R.id.settings_feedbackButton)
    public void onFeedbackClick() {
        presenter.feedbackClick();
    }

    @OnTextChanged(R.id.register_nickname)
    public void onNameChange() {
        save.setEnabled(true);
    }

    @OnItemSelected(R.id.register_team_spinner)
    public void onTeamSpinnerChange() {
        presenter.teamChange();
    }

    @Override
    public void setName(String name) {
        this.nickname.setText(name);
    }

    @Override
    public void setTeam(int team) {
        this.team.setSelection(team);
    }

    @Override
    public String getName() {
        return nickname.getText().toString();
    }

    @Override
    public int getTeam() {
        return team.getSelectedItemPosition();
    }

    @Override
    public void showError(int code) {
        setFieldsEnabled(true);
        toastManager.errorLoading();
    }

    @Override
    public void showSuccess() {
        setFieldsEnabled(true);
        save.setEnabled(false);
        toastManager.successLoading();
    }

    @Override
    public void showLoading() {
        toastManager = new ToastManager(context);
        toastManager.showLoading(this);
        setFieldsEnabled(false);
    }

    @Override
    public void disableSave() {
        save.setEnabled(false);
    }

    @Override
    public void enableSave() {
        save.setEnabled(true);
    }

    private void setFieldsEnabled(boolean enabled) {
        nickname.setEnabled(enabled);
        team.setEnabled(enabled);
        back.setEnabled(enabled);
        save.setEnabled(enabled);
    }
}
