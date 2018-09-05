package com.stumi.gobuddies.system.animation;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.stumi.gobuddies.R;

/**
 * @author stumpfb on 09/09/2016.
 */
public class ButtonAnimationOnTouchListener implements View.OnTouchListener {

    private final Context context;

    public ButtonAnimationOnTouchListener(Context context) {
        this.context = context;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            AnimatorSet animator = (AnimatorSet) AnimatorInflater
                    .loadAnimator(context, R.animator.bubble_down_click_animator);
            animator.setTarget(v);
            animator.start();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            AnimatorSet animator =
                    (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.bubble_up_click_animator);
            animator.setTarget(v);
            animator.start();
        }
        return false;
    }
}
