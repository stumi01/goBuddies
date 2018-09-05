package com.stumi.gobuddies.base;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.stumi.gobuddies.base.mvp.BaseMVPLayout;

/**
 * Created by sourc on 2017. 03. 14..
 */

public abstract class CircularRevealView<View extends MvpView, Presenter extends MvpPresenter<View>> extends BaseMVPLayout<View, Presenter> {
    public static final int DURATION = 400;

    //we add listener if it's revered to handle activity finish
    private Animator.AnimatorListener animatorListener =
            new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    //to remove default lollipop animation
                    rootView.setVisibility(android.view.View.INVISIBLE);
                    finish();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            };

    protected abstract void finish();

    public CircularRevealView(@NonNull Context context) {
        super(context);
    }

    protected void revealContent(int x, int y) {
    /*
    We do post cause we need height and width
    Also it will be attached when runnable is executed
    */
        rootView.post(() -> {
            /*
            why am I handling older android versions here?
            I don't know. Maybe because I'm douche.
            */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                /*
                We've passed the x and y coordinate
                from previous activity, to reveal
                layout from exact place where it was tapped
                */
                Animator animator = createRevealAnimator(false, x, y);
                animator.start();
            }
            rootView.setVisibility(android.view.View.VISIBLE);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected Animator createRevealAnimator(boolean reversed, int x, int y) {

        float hypot =
                (float) Math.hypot(rootView.getHeight(), rootView.getWidth());
        float startRadius = reversed ? hypot : 0;
        float endRadius = reversed ? 0 : hypot;
        Animator animator = ViewAnimationUtils.createCircularReveal(
                rootView, x, y, //center position of the animation
                startRadius,
                endRadius);
        animator.setDuration(DURATION);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        if (reversed)
            animator.addListener(animatorListener);
        return animator;
    }
}
