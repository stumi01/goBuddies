package com.stumi.gobuddies.system.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

/**
 * Created by sourc on 2017. 04. 06..
 */

public class KeyboardVisibilityEvent {
    public static final String TAG = "KeyboardVisibilityEvent";

    private final static int KEYBOARD_VISIBLE_THRESHOLD_DP = 100;

    /**
     * Set keyboard visiblity change event listener.
     *
     * @param context  Context
     * @param rootView baseView where the visibility can happpen
     * @param listener KeyboardVisibilityEventListener
     */
    public static void setEventListener(
            @NonNull final Context context,
            @NonNull final View rootView,
            @NonNull final KeyboardVisibilityEventListener listener) {


        rootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    private final Rect r = new Rect();

                    private final int visibleThreshold = Math.round(
                            UIUtil.convertDpToPx(context, KEYBOARD_VISIBLE_THRESHOLD_DP));

                    private boolean wasOpened = false;

                    @Override
                    public void onGlobalLayout() {
                        rootView.getWindowVisibleDisplayFrame(r);

                        int heightDiff = Math.abs(rootView.getRootView().getHeight() - r.height());

                        boolean isOpen = heightDiff > visibleThreshold;
                        Log.d(TAG, "onGlobalLayout: " + heightDiff);
                        if (isOpen == wasOpened) {
                            // keyboard state has not changed
                            return;
                        }

                        wasOpened = isOpen;

                        listener.onVisibilityChanged(isOpen);
                    }
                });
    }

    /**
     * Determine if keyboard is visible
     *
     * @param activity Activity
     * @return Whether keyboard is visible or not
     */
    public static boolean isKeyboardVisible(Activity activity) {
        Rect r = new Rect();

        View activityRoot = null;//getActivityRoot(activity);
        int visibleThreshold = Math
                .round(UIUtil.convertDpToPx(activity, KEYBOARD_VISIBLE_THRESHOLD_DP));

        activityRoot.getWindowVisibleDisplayFrame(r);

        int heightDiff = activityRoot.getRootView().getHeight() - r.height();

        return heightDiff > visibleThreshold;
    }

    private static View getActivityRoot(Activity activity) {
        return ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
    }
}
