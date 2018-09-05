package com.stumi.gobuddies.ui.floating.tabs.partyFinder;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.stumi.gobuddies.ui.floating.tabs.partyFinder.party.PartyDetails;

import java.util.List;

/**
 * @author stumpfb on 30/07/2016.
 */
public interface PartyFinderView extends MvpLceView<List<PartyDetails>> {

    void promptRequests(List<PartyDetails> parties);

    void showUpdating();

    void hideUpdating();

}
