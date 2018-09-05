package com.stumi.gobuddies.base;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.stumi.gobuddies.R;

/**
 * Created by sourc on 2017. 02. 13..
 */
public abstract class CircularRevealActivity extends AppCompatActivity {
    public static final int DURATION = 400;
    public static final String EXTRA_X = "x";
    public static final String EXTRA_Y = "y";
    private View content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        content = findViewById(R.id.reveal_content);
        revealContent();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void revealContent() {
    /*
    We do post cause we need height and width
    Also it will be attached when runnable is executed
    */
        content.post(() -> {
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
                int x = getIntent().getIntExtra(EXTRA_X, 0);
                int y = getIntent().getIntExtra(EXTRA_Y, 0);
                Animator animator = createRevealAnimator(false, x, y);
                animator.start();
            }
            content.setVisibility(View.VISIBLE);
        });
    }

    protected abstract int getContentView();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected Animator createRevealAnimator(boolean reversed, int x, int y) {

        float hypot =
                (float) Math.hypot(content.getHeight(), content.getWidth());
        float startRadius = reversed ? hypot : 0;
        float endRadius = reversed ? 0 : hypot;
        Animator animator = ViewAnimationUtils.createCircularReveal(
                content, x, y, //center position of the animation
                startRadius,
                endRadius);
        animator.setDuration(DURATION);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        if (reversed)
            animator.addListener(animatorListener);
        return animator;
    }

    //we add listener if it's revered to handle activity finish
    private Animator.AnimatorListener animatorListener =
            new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    //to remove default lollipop animation
                    content.setVisibility(View.INVISIBLE);
                    finish();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            };
}
