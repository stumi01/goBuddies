package com.stumi.gobuddies.model;

import android.support.annotation.NonNull;

/**
 * @author stumpfb on 28/08/2016.
 */
public enum GoBuddiesTab {
    BUDDY_FINDER(0), PARTY(1), PARTY_MEMBERS(2);

    private final int id;

    GoBuddiesTab(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public static GoBuddiesTab valueOf(int position) {
        for (GoBuddiesTab fragment : GoBuddiesTab.values()) {
            if (fragment.getId() == position) {
                return fragment;
            }
        }

        return BUDDY_FINDER;
    }

    @Override
    public String toString() {
        return super.toString().replace('_', ' ');
    }
}
