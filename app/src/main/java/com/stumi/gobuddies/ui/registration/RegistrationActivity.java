package com.stumi.gobuddies.ui.registration;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.stumi.gobuddies.GoBuddiesApplication;
import com.stumi.gobuddies.R;
import com.stumi.gobuddies.model.User;
import com.stumi.gobuddies.system.animation.ButtonAnimationOnTouchListener;
import com.stumi.gobuddies.system.widget.ToastManager;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author stumpfb on 29/07/2016.
 */
public class RegistrationActivity extends MvpActivity<RegistrationView, RegistrationPresenter>
        implements RegistrationView {

    @BindView(R.id.register_nickname)
    MaterialEditText nicknameText;

    @BindView(R.id.register_team_spinner)
    Spinner teamSpinner;

    @BindView(R.id.register_button)
    Button registerButton;

    @BindView(R.id.constraintLayout)
    ViewGroup rootView;

    @Inject
    RegistrationPresenter presenter;

    private ToastManager toastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        injectDependencies();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        initButtonAnimation();
        teamSpinner.setAdapter(new TeamSpinnerAdapter(this, R.id.team_select_name,
                getResources().getStringArray(R.array.team_array), getLayoutInflater()));
        presenter.restoreData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initButtonAnimation() {
        registerButton.setOnTouchListener(new ButtonAnimationOnTouchListener(this));
    }

    @Override
    public void showError(int code) {
        toastManager.errorLoading(
                String.format(Locale.ENGLISH, "Error during request (%d) \n Please try again later", code));
        setFieldsEnabled(true);
    }

    @Override
    public void showLoading() {
        toastManager = new ToastManager(this);
        toastManager.showLoading(rootView);
        setFieldsEnabled(false);
    }

    private void setFieldsEnabled(boolean enabled) {
        teamSpinner.setEnabled(enabled);
        nicknameText.setEnabled(enabled);
        registerButton.setEnabled(enabled);
    }

    @Override
    public void showSuccess() {
        toastManager.successLoading();
        setFieldsEnabled(true);
    }

    @Override
    public void showValidationError() {
        new ToastManager(this).showError("Please fill the fields", rootView);
        setFieldsEnabled(true);
    }

    @Override
    public void setData(User self) {
        nicknameText.setText(self.getName());
        teamSpinner.setSelection(self.getTeam());
    }

    private void injectDependencies() {
        DaggerRegistrationComponent.builder()
                .applicationComponent(GoBuddiesApplication.get(getBaseContext()).getApplicationComponent())
                .build().inject(this);
    }

    @NonNull
    @Override
    public RegistrationPresenter createPresenter() {
        return presenter;
    }

    @OnClick(R.id.register_button)
    public void submit() {
        int teamPos = getTeam();
        String nickname = getName();
        presenter.registerClick(teamPos, nickname);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.attachView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.detachView(true);
    }

    @Override
    public String getName() {
        return nicknameText.getText().toString();
    }

    @Override
    public int getTeam() {
        return teamSpinner.getSelectedItemPosition();
    }
}
