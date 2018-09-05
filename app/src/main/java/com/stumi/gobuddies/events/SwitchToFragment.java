package com.stumi.gobuddies.events;


import com.stumi.gobuddies.model.GoBuddiesTab;

/**
 * @author stumpfb on 27/08/2016.
 */
public class SwitchToFragment {

    private final GoBuddiesTab fragment;

    public SwitchToFragment(GoBuddiesTab fragment) {
        this.fragment = fragment;
    }

    public int getFragmentItemId() {
        return fragment.getId();
    }
}
