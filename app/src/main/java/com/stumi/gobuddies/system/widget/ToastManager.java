package com.stumi.gobuddies.system.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;


/**
 * @author stumpfb on 17/09/2016.
 */
public class ToastManager {

    private MorphingToastLayout toastLayout;

    private OnHideListener onHideListener;
    private Context context;
    private LoadToast loadToast;


    public ToastManager(Context context) {
        this.context = context;
    }

    public void showLoading(ViewGroup anchorView) {
        loadToast = new LoadToast(context, anchorView);
        loadToast.setTranslationY(50);
        loadToast.show();

    }

    public void successLoading() {
        loadToast.success();
    }

    public void errorLoading() {
        loadToast.error();
    }

    public void errorLoading(String message) {
        //toastLayout.morphErrorWithMessage(message, this::hideCrouton);
        loadToast.error();
    }

    public void showError(String message, View anchorView) {
/*        View view = View.inflate(context, R.layout.toast_loading, null);
        toastLayout = new MorphingToastLayout((ViewGroup) view, message);
        toastLayout.changeToColorError();
        createCrouton(view);
        toastLayout.doNotMorph(() -> {
        });*/
        Toasty.error(context, message, Toast.LENGTH_SHORT, true).show();
    }


    public void addOnHideListener(OnHideListener listener) {
        onHideListener = listener;
    }

    public interface OnHideListener {

        void onHide();
    }
}
