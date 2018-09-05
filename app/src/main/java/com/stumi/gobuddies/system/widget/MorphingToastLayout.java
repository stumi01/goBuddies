package com.stumi.gobuddies.system.widget;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.stumi.gobuddies.R;
import com.transitionseverywhere.AutoTransition;
import com.transitionseverywhere.ChangeText;
import com.transitionseverywhere.Recolor;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author stumpfb on 17/09/2016.
 */
public class MorphingToastLayout {

    private View doneView;

    private View errorView;

    private View progressBar;

    //private TextView textView;

    private View containerView;

    private final ViewGroup rootView;

    private final int DURATION = 300;
    private final int DURATION_FOR_TEXT = 1000;

    public MorphingToastLayout(ViewGroup view) {
        rootView = view;
        initView();
    }

    public MorphingToastLayout(ViewGroup view, String message) {
        this(view);
        //textView.setText(message);
    }

    public void doNotMorph(TransitionEndListener listener) {
        createTransition(listener, () -> {
        });
    }

    public void changeToColorError() {
        changeContainerColor(R.drawable.loading_background_error, containerView);
    }

    private void initView() {
        doneView = rootView.findViewById(R.id.loading_done);
        errorView = rootView.findViewById(R.id.loading_fail);
        //textView = (TextView) rootView.findViewById(R.id.loading_text);
        progressBar = rootView.findViewById(R.id.loading_bar);
        containerView = rootView.findViewById(R.id.loading_container);
    }

    public void morphSuccess(TransitionEndListener listener) {
        createTransition(listener, () -> {

            changeContainerColor(R.drawable.loading_background_success, containerView);

            doneView.setVisibility(View.VISIBLE);
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                progressBar.setProgress(100, true);
            } else {
                progressBar.setProgress(100);
            }*/
            progressBar.setVisibility(View.GONE);
            //textView.setVisibility(View.GONE);
            errorView.setVisibility(View.GONE);
        });

    }

    private void changeContainerColor(@DrawableRes int drawable, View view) {
        Drawable currentBG = view.getBackground();
        Drawable newBG = ContextCompat.getDrawable(rootView.getContext(), drawable);
        TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{currentBG, newBG});
        transitionDrawable.setCrossFadeEnabled(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(transitionDrawable);
        } else {
            view.setBackground(transitionDrawable);
        }
        transitionDrawable.startTransition(DURATION);
    }

    public void morphError(TransitionEndListener listener) {
        createTransition(listener, () -> {

            containerView.setBackgroundResource(R.drawable.loading_background_error);
            progressBar.setVisibility(View.GONE);
            errorView.setVisibility(View.VISIBLE);
            //textView.setVisibility(View.GONE);
            doneView.setVisibility(View.GONE);
        });

    }

    public void morphErrorWithMessage(String message, TransitionEndListener listener) {
        createTransition(DURATION_FOR_TEXT, listener, () -> {
            containerView.setBackgroundResource(R.drawable.loading_background_error);
            Log.d("STUMI", message);
            // textView.setText(message);
            progressBar.setVisibility(View.GONE);
            errorView.setVisibility(View.VISIBLE);
            //textView.setVisibility(View.VISIBLE);
            doneView.setVisibility(View.GONE);

        });

    }

    private void createTransition(int duration, TransitionEndListener listener, TransitionAction action) {
        Observable.timer(100, TimeUnit.MILLISECONDS).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
            TransitionSet transitionSet = new TransitionSet();
            transitionSet.setDuration(duration);
            transitionSet.setOrdering(TransitionSet.ORDERING_TOGETHER);

            ChangeText changeText = new ChangeText();
            //changeText.addTarget(textView);
            transitionSet.addTransition(changeText);

            AutoTransition autoTransition = new AutoTransition();
            /*autoTransition.addTarget(textView);
            autoTransition.addTarget(containerView);*/
            transitionSet.addTransition(autoTransition);

            Recolor recolor = new Recolor();
            recolor.setDuration(duration);
            transitionSet.addTransition(recolor);

            TransitionManager
                    .beginDelayedTransition(rootView, attachListenerToTransition(transitionSet, listener));
            action.action();
        });
    }

    private void createTransition(TransitionEndListener listener, TransitionAction action) {
        createTransition(DURATION, listener, action);
    }

    private Transition attachListenerToTransition(Transition transition,
                                                  final TransitionEndListener listener) {
        return transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                listener.onEndTransition();
            }

            @Override
            public void onTransitionCancel(Transition transition) {
            }

            @Override
            public void onTransitionPause(Transition transition) {
            }

            @Override
            public void onTransitionResume(Transition transition) {
            }
        });
    }

    public interface TransitionEndListener {

        void onEndTransition();
    }

    private interface TransitionAction {

        void action();
    }
}
