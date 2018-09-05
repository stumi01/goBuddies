package com.stumi.gobuddies.ui.floating.tabs;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.stumi.gobuddies.model.GoBuddiesTab;
import com.stumi.gobuddies.ui.floating.tabs.party.PartyLayout;
import com.stumi.gobuddies.ui.floating.tabs.partyFinder.PartyFinderLayout;
import com.stumi.gobuddies.ui.floating.tabs.partyMembers.PartyMembersLayout;

/**
 * @author stumpfb on 15/09/2016.
 */
public class SwypeControlViewPager extends android.support.v4.view.ViewPager {

    private static final String TAG = "SwypeControlViewPager";
    private final PartyLayout partyView;

    private final PartyMembersLayout partyMembersView;

    private final PartyFinderLayout partyFinderView;

    private boolean enabled;

    private GoBuddiesPagerAdapter goBuddiesPagerAdapter;

    public SwypeControlViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
        partyView = new PartyLayout(context);
        partyMembersView = new PartyMembersLayout(context);
        partyFinderView = new PartyFinderLayout(context);
        addView(partyView);
        addView(partyMembersView);
        addView(partyFinderView);
        setOffscreenPageLimit(2);
    }

    public void initTabs(TabLayout tabLayout) {
        goBuddiesPagerAdapter = new GoBuddiesPagerAdapter(tabLayout);
        goBuddiesPagerAdapter.addTab(partyFinderView, GoBuddiesTab.BUDDY_FINDER.toString(), true);
        goBuddiesPagerAdapter.addTab(partyView, GoBuddiesTab.PARTY.toString(), false);
        goBuddiesPagerAdapter.addTab(partyMembersView, GoBuddiesTab.PARTY_MEMBERS.toString(), false);
        setAdapter(goBuddiesPagerAdapter);
        setCurrentItem(0);

        addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabSelected" + tab.toString());
                setCurrentItem(tab.getPosition());
                goBuddiesPagerAdapter.tabSelected(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabUnselected" + tab.toString());
                goBuddiesPagerAdapter.tabUnSelected(tab.getPosition());

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return enabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return enabled && super.onInterceptTouchEvent(event);
    }

    public void setSwypeEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            goBuddiesPagerAdapter.enablePartyTabs();
        } else {
            goBuddiesPagerAdapter.disablePartyTabs();
        }

    }

    public boolean isSwypeEnabled() {
        return enabled;
    }

}
