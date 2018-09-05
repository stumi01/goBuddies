package com.stumi.gobuddies.ui.firstStart;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.stumi.gobuddies.GoBuddiesApplication;
import com.stumi.gobuddies.R;
import com.stumi.gobuddies.system.IntentManager;
import com.stumi.gobuddies.system.UserDataHolder;
import com.stumi.gobuddies.system.widget.MorphingToastLayout;
import com.tbruyelle.rxpermissions2.RxPermissions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

/**
 * @author stumpfb on 14/08/2016.
 */
public class FirstStartActivity extends MvpActivity<FirstStartView, FirstStartPresenter>
        implements FirstStartView {

    private static final String TAG = FirstStartActivity.class.getSimpleName();

    private static final int PERMISSION_REQUEST_CODE = 1;
    private final static int OVERLAY_REQUEST_CODE = 2;

    @BindView(R.id.loading_view)
    ViewGroup loadingView;

    @Inject
    IntentManager intentManager;

    @Inject
    UserDataHolder userDataHolder;

    @Inject
    FirstStartPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            final Intent intent = getIntent();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(intent.getAction())) {
                Log.w(TAG, "Main Activity is not the root.  Finishing Main Activity instead of launching.");
                finish();
                return;
            }
        }
        setContentView(R.layout.activity_firststart);
        ButterKnife.bind(this);
    }

    private void injectDependencies() {
        DaggerFirstStartComponent.builder()
                .applicationComponent(GoBuddiesApplication.get(this).getApplicationComponent()).build()
                .inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.attachView(this);
        showLoading();
        checkPermissions();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.detachView(false);
        Log.d(TAG, "onPause: ");
    }

    @NonNull
    @Override
    public FirstStartPresenter createPresenter() {
        injectDependencies();
        return presenter;
    }

    private void checkPermissions() {
        new RxPermissions(this).request(Manifest.permission.ACCESS_FINE_LOCATION).subscribe(granted -> {
            if (granted) { // Always true pre-M
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(this)) {
                        checkDrawOverlayPermission();
                    } else {
                        startApp();
                    }
                } else {
                    startApp();
                }
            } else {
                showGrantErrorAndExit();
            }
        });
    }

    private void showLoading() {
        //this is happening
    }

    public void showGrantErrorAndExit() {
        Toasty.error(this, getString(R.string.error_server), Toast.LENGTH_LONG, true).show();
        loadingView.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1200);
    }

    @Override
    public void showSuccessAndHide(MorphingToastLayout.TransitionEndListener listener) {
        loadingView.postDelayed(new Runnable() {
            @Override
            public void run() {
                showSuccess(listener);
                moveTaskToBack(true);
            }
        }, 500);
    }

    @Override
    public void showSuccess(MorphingToastLayout.TransitionEndListener listener) {
        listener.onEndTransition();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Point getScreenSize() {
        Point outsize = new Point();
        getWindowManager().getDefaultDisplay().getSize(outsize);
        return outsize;
    }

    private void startApp() {
        presenter.startApp();
    }

    public void checkDrawOverlayPermission() {
        /** check if we already  have permission to draw over other apps */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                /** if not construct intent to request permission */
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                /** request permission via start activity for result */
                startActivityForResult(intent, OVERLAY_REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /** check if received result code
         is equal our requested code for draw permission  */
        if (requestCode == OVERLAY_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    startApp();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PERMISSION_REQUEST_CODE == requestCode) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    showGrantErrorAndExit();
                    return;
                }
            }
        }
        checkDrawOverlayPermission();
        startApp();
    }
}
