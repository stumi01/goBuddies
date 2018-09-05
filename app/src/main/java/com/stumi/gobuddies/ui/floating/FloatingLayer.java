package com.stumi.gobuddies.ui.floating;

import android.animation.Animator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.stumi.gobuddies.GoBuddiesApplication;
import com.stumi.gobuddies.R;
import com.stumi.gobuddies.base.CircularRevealView;
import com.stumi.gobuddies.system.animation.ButtonAnimationOnTouchListener;
import com.stumi.gobuddies.ui.floating.settings.SettingsLayout;
import com.stumi.gobuddies.ui.floating.tabs.TabsLayout;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
import static android.view.WindowManager.LayoutParams.TYPE_PHONE;

/**
 * Created by sourc on 2017. 03. 06..
 */

public class FloatingLayer extends CircularRevealView<FloatingView, FloatingPresenter> implements FloatingView {

    private static final String TAG = FloatingView.class.getSimpleName();

    private WindowManager windowManager;
    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;

    private TabsLayout tabsLayout;

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;

    @BindView(R.id.trash_button)
    ImageView trashButton;

    @BindView(R.id.errorsText)
    TextView errorText;

    int contentViewIndex;
    private WeakReference<View> contentView = new WeakReference<View>(null);
    private boolean shown;
    private Set<String> errors = new HashSet<>();

    public FloatingLayer(@NonNull Context context) {
        super(context);
        rootView.setVisibility(GONE);
    }

    @Override
    public FloatingPresenter createPresenter() {
        GoBuddiesApplication goBuddiesApplication = GoBuddiesApplication.get(context);
        FloatingComponent floatingComponent = goBuddiesApplication.createFloatingComponent();
        return floatingComponent.presenter();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_floating;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.onResume();
        errorText.setVisibility(GONE);
    }

    @Override
    protected void onDetachedFromWindow() {
        presenter.onPause();
        super.onDetachedFromWindow();
    }

    @Override
    public boolean isShown() {
        return super.isShown() || shown;
    }

    public void showView(int x, int y) {
        shown = true;
        if (contentView.get() == null) {
            initView();
        } else {
            addThisToWindow();
        }
        Log.d(TAG, "showView: ");
        revealContent(x, y);
        rootView.setVisibility(android.view.View.VISIBLE);

    }

    private void initView() {
        addThisToWindow();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        globalLayoutListener = () -> showLayout(displayMetrics.heightPixels, displayMetrics.density);
        getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
        initContentView();

        initToolbar();
        addTabView();

        trashButton.setOnTouchListener(new ButtonAnimationOnTouchListener(context));
    }

    private void addThisToWindow() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(MATCH_PARENT, MATCH_PARENT, TYPE_PHONE, 32, PixelFormat.TRANSLUCENT);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.softInputMode = SOFT_INPUT_ADJUST_PAN;
        this.windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.windowManager.addView(this, layoutParams);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

    private void initToolbar() {
        toolbar.setTitle(context.getString(R.string.app_name));
        toolbar.inflateMenu(R.menu.menu_toolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_settings && !(contentView.get() instanceof SettingsLayout)) {
                switchToView(new SettingsLayout(context));
                return true;
            }

            return false;
        });
    }

    private void initContentView() {
        View C = findViewById(R.id.content_fragment);
        ViewGroup parent = (ViewGroup) C.getParent();
        contentViewIndex = parent.indexOfChild(C);
        contentView = new WeakReference<>(C);
    }

    private void addTabView() {
        tabsLayout = new TabsLayout(context);
        switchToView(tabsLayout);
    }

    private void switchToView(View newContentView) {
        ViewGroup parent = (ViewGroup) contentView.get().getParent();
        parent.removeView(contentView.get());
        parent.addView(newContentView, contentViewIndex);
        contentView = new WeakReference<View>(newContentView);
    }

    private void showLayout(int heightPixels, float density) {
        int height = heightPixels - getHeight();
        if (((float) height) / density > 100.0f) {
            Log.d("ChatLayerView", "Hiding x -> Keybaord shown, diff: " + (((float) height) / density));
            trashButton.setVisibility(GONE);
            return;
        }

        trashButton.setVisibility(VISIBLE);
    }

    @OnClick(R.id.trash_button)
    public void onTrashButtonClick(View v) {
        int[] outLocation = new int[2];
        v.getLocationOnScreen(outLocation);
        outLocation[0] += v.getWidth() / 3;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animator animator =
                    createRevealAnimator(
                            true,
                            outLocation[0],
                            outLocation[1]);
            animator.start();
        } else {
            finish();
        }
    }

    @Override
    public void onBackButtonPressed() {
        if (contentView.get() instanceof TabsLayout) {
            finish();
        } else if (contentView.get() instanceof SettingsLayout) {
            addTabView();
        }
    }

    protected void finish() {
        this.windowManager.removeView(this);
        shown = false;
    }

    @Override
    public void closeWindow() {
        finish();
    }

    @Override
    public void showGpsError() {
        final String message = context.getString(R.string.message_gps_disabled);
        addErrorMessage(message);
    }

    private void showErrorMessagesIfNecessary() {
        if (!errors.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            errorText.setText("");
            errors.forEach(s -> errorMessage.append(s).append("\n"));
            errorMessage.deleteCharAt(errorMessage.length() - 1);
            errorText.setText(errorMessage);
            errorText.setVisibility(VISIBLE);
        } else {
            errorText.setVisibility(GONE);
        }

    }

    @Override
    public void showNetworkError() {
        final String message = context.getString(R.string.message_network_error);
        addErrorMessage(message);
    }

    @Override
    public void hideNetworkError() {
        final String message = context.getString(R.string.message_network_error);
        removeErrorMessage(message);
    }

    @Override
    public void hideGpsError() {
        final String message = context.getString(R.string.message_gps_disabled);
        removeErrorMessage(message);
    }

    private void addErrorMessage(String message) {
        if (!errors.contains(message)) {
            errors.add(message);
            showErrorMessagesIfNecessary();
        }
    }

    private void removeErrorMessage(String message) {
        if (errors.contains(message)) {
            errors.remove(message);
            showErrorMessagesIfNecessary();
        }
    }


}
