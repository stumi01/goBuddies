package com.stumi.gobuddies.events;

/**
 * Created by sourc on 2017. 04. 05..
 */

public class ShowFloatingView {
    private float x;
    private float y;

    public ShowFloatingView(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
