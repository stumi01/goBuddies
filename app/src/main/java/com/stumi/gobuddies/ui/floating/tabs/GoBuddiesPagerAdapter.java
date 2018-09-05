package com.stumi.gobuddies.ui.floating.tabs;

import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stumi.gobuddies.R;
import com.stumi.gobuddies.model.GoBuddiesTab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author stumpfb on 28/08/2016.
 */
public class GoBuddiesPagerAdapter extends PagerAdapter {

    private final TabLayout tabLayout;

    private final List<View> tabs = new ArrayList<>();
    private final List<TextView> titles = new ArrayList<>();

    public GoBuddiesPagerAdapter(TabLayout tabLayout) {
        this.tabLayout = tabLayout;
    }

    private TextView createTab(TabLayout tabLayout, String goBuddiesTab, boolean enabled) {
        TabLayout.Tab tab = tabLayout.newTab().setTag(goBuddiesTab);
        View tabView = LayoutInflater.from(tabLayout.getContext())
                .inflate(R.layout.tab_item_layout, tabLayout, false);
        TextView textView = (TextView) tabView.findViewById(R.id.tab_text);
        textView.setText(goBuddiesTab);
        setColorByState(tabLayout, enabled, textView);
        tab.setCustomView(textView);
        tabLayout.addTab(tab, enabled);
        return textView;
    }

    private void setColorByState(TabLayout tabLayout, boolean enabled, TextView textView) {
        textView.setTextColor(ContextCompat.getColor(tabLayout.getContext(),
                enabled ? R.color.tab_selected_text_color : R.color.tab_disabled_text_color));
    }

    public void addTab(View view, String title, boolean enabled) {
        tabs.add(view);
        titles.add(createTab(tabLayout, title, enabled));
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        return tabs.get(position);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void enablePartyTabs() {
        disableTabClickByRange(Arrays.asList());
        setColorByState(tabLayout, true, titles.get(1));
        setColorByState(tabLayout, true, titles.get(2));
    }

    public void disablePartyTabs() {
        disableTabClickByRange(Arrays.asList(GoBuddiesTab.PARTY.getId(), GoBuddiesTab.PARTY_MEMBERS.getId()));
        setColorByState(tabLayout, false, titles.get(1));
        setColorByState(tabLayout, false, titles.get(2));
    }

    private void disableTabClickByRange(final List<Integer> disabled) {
        LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            final int tabIndex = i;
            tabStrip.getChildAt(i).setOnTouchListener((v, event) -> disabled.contains(tabIndex));
        }
    }


    public void tabSelected(int position) {
        tabs.get(position).setSelected(true);
    }

    public void tabUnSelected(int position) {
        tabs.get(position).setSelected(false);
    }
}