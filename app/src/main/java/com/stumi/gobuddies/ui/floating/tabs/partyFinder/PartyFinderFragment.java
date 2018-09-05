package com.stumi.gobuddies.ui.floating.tabs.partyFinder;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stumi.gobuddies.GoBuddiesApplication;
import com.stumi.gobuddies.R;
import com.stumi.gobuddies.base.mvp.MvpBaseLceFragment;
import com.stumi.gobuddies.model.User;
import com.stumi.gobuddies.ui.UIUtils;
import com.stumi.gobuddies.ui.floating.tabs.partyFinder.party.PartyAdapter;
import com.stumi.gobuddies.ui.floating.tabs.partyFinder.party.PartyDetails;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Single;

/**
 * @author stumpfb on 30/07/2016.
 */
public class PartyFinderFragment
        extends MvpBaseLceFragment<LinearLayout, List<PartyDetails>, PartyFinderView, PartyFinderPresenter>
        implements PartyFinderView, PartyAdapter.PartyClickListener {

    private PartyFinderComponent partyFinderComponent;

    @BindView(R.id.contentView)
    RecyclerView recyclerView;

    private PartyAdapter adapter;

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        initRecycleView();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_party_finder;
    }

    private void initRecycleView() {
        adapter = new PartyAdapter(getContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setData(List<PartyDetails> data) {
        adapter.setParties(data);
        adapter.notifyDataSetChanged();
        showContent();
    }

    public void loadData(boolean pullToRefresh) {
        presenter.loadParties(pullToRefresh);
    }

    @Override
    public void injectDependencies() {
        partyFinderComponent = GoBuddiesApplication.get(getContext())
                .getFloatingComponent().plus(new PartyFinderModule());
    }

    @NonNull
    @Override
    public PartyFinderPresenter createPresenter() {
        return partyFinderComponent.presenter();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
        presenter.detachView(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachView(this);
        presenter.onResume();
        presenter.loadPartiesPeriodically();
    }

    @Override
    public void onJoinClick(PartyDetails partyDetails) {
        presenter.joinToPartyClick(partyDetails);
    }

    @Override
    public Single<List<User>> onExpandClick(String partyId) {
        return presenter.partyExpandClick(partyId);
    }

    @Override
    public void promptRequests(final List<PartyDetails> parties) {
        if (!parties.isEmpty()) {
            final Dialog dialog = new Dialog(getContext());
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_accept_request);
            dialog.setCancelable(false);

            PartyDetails partyDetails = parties.get(0);

            fillDialogFromPartyProperties(dialog, partyDetails);

            final Button accept = (Button) dialog.findViewById(R.id.accept_dialog_accept);
            final Button cancel = (Button) dialog.findViewById(R.id.accept_dialog_cancel);

            accept.setOnClickListener(click -> {
                presenter.doJoinAccept(partyDetails);
                dialog.dismiss();
            });
            cancel.setOnClickListener(click -> {
                if (parties.size() > 1) {
                    promptRequests(parties.subList(1, parties.size()));
                }
                dialog.dismiss();
            });

            dialog.show();
        }
    }

    private void fillDialogFromPartyProperties(Dialog dialog, PartyDetails partyDetails) {
        TextView name = (TextView) dialog.findViewById(R.id.accept_dialog_party_name);
        name.setText(partyDetails.getName());

        TextView size = (TextView) dialog.findViewById(R.id.accept_dialog_party_size);
        UIUtils.initPartySizeBubbleTextField(size, partyDetails.getSize());

        ImageView icon = (ImageView) dialog.findViewById(R.id.accept_dialog_party_icon);
        icon.setImageResource(UIUtils.getIconResource(partyDetails.getTeam(), partyDetails.getSize()));

        TextView distance = (TextView) dialog.findViewById(R.id.accept_dialog_party_distance);
        distance.setText("(" + UIUtils.getReadableDistance(partyDetails.getDistance()) + ")");
    }

    @Override
    public void showUpdating() {
        //TODO #51
    }

    @Override
    public void hideUpdating() {
        //TODO #51
    }

}
