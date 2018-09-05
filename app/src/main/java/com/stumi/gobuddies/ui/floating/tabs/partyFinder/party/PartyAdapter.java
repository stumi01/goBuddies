package com.stumi.gobuddies.ui.floating.tabs.partyFinder.party;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;
import com.stumi.gobuddies.R;
import com.stumi.gobuddies.model.User;
import com.stumi.gobuddies.system.animation.ButtonAnimationOnTouchListener;
import com.stumi.gobuddies.ui.UIUtils;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author stumpfb on 31/07/2016.
 */
public class PartyAdapter extends SupportAnnotatedAdapter implements PartyAdapterBinder {

    @ViewType(
            layout = R.layout.item_team,
            views = {@ViewField(
                    id = R.id.team_icon,
                    name = "icon",
                    type = ImageView.class), @ViewField(
                    id = R.id.team_name,
                    name = "name",
                    type = TextView.class), @ViewField(
                    id = R.id.team_size,
                    name = "size",
                    type = TextView.class), @ViewField(
                    id = R.id.team_distance,
                    name = "distance",
                    type = TextView.class), @ViewField(
                    id = R.id.joinButton,
                    name = "joinButton",
                    type = Button.class), @ViewField(
                    id = R.id.party_members,
                    name = "partyMembersRecycle",
                    type = RecyclerView.class)})
    public final int VIEWTYPE_PARTY = 0;

    private List<PartyDetails> parties;

    private final Context context;

    private final PartyClickListener listener;

    public PartyAdapter(Context context, PartyClickListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return parties == null ? 0 : parties.size();
    }

    @Override
    public void bindViewHolder(PartyAdapterHolders.VIEWTYPE_PARTYViewHolder vh, int position) {
        final PartyDetails partyDetails = parties.get(position);
        initIcon(vh.icon, partyDetails);
        vh.name.setText(partyDetails.getName());
        UIUtils.initPartySizeBubbleTextField(vh.size, partyDetails.getSize());
        vh.distance.setText(UIUtils.getReadableDistance(partyDetails.getDistance()));
        initButton(vh.joinButton, partyDetails.getTeamRequestStatus());
        vh.joinButton.setOnClickListener(v -> {
            initButton(vh.joinButton, PartyRequestStatus.RequestAlreadySent);
            listener.onJoinClick(partyDetails);
        });
        vh.joinButton.setOnTouchListener(new ButtonAnimationOnTouchListener(context));

        vh.icon.setOnClickListener(v -> showMembers(vh.partyMembersRecycle, partyDetails.getId()));
        vh.icon.setOnTouchListener(new ButtonAnimationOnTouchListener(context));

    }

    private void showMembers(RecyclerView partyMembersRecycle, String partyId) {
        if (partyMembersRecycle.getVisibility() == View.GONE) {

            listener.onExpandClick(partyId).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(users -> initRecyclerWithData(partyMembersRecycle, users), hrowable -> {
                        //TODO
                    });
        } else {
            partyMembersRecycle.setVisibility(View.GONE);
        }
    }

    private void initRecyclerWithData(RecyclerView partyMembersRecycle, List<User> users) {
        PartyFinderMemberAdapter adapter = new PartyFinderMemberAdapter(context, users);
        partyMembersRecycle
                .setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        partyMembersRecycle.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        partyMembersRecycle.setVisibility(View.VISIBLE);
    }

    private void initIcon(ImageView icon, PartyDetails partyDetails) {
        icon.setImageResource(UIUtils.getIconResource(partyDetails.getTeam(), partyDetails.getSize()));
    }

    private void initButton(Button button, PartyRequestStatus status) {
        switch (status) {
            case NotAvailable:
                button.setText(context.getText(R.string.partyfinder_not_available));
                button.setEnabled(false);
                button.setVisibility(View.GONE);
                break;
            case RequestAvailable:
                button.setText(context.getText(R.string.partyfinder_available));
                button.setEnabled(true);
                button.setVisibility(View.VISIBLE);
                break;
            case RequestAlreadySent:
                button.setText(context.getText(R.string.partyfinder_request_sent));
                button.setEnabled(false);
                button.setVisibility(View.VISIBLE);
                break;
            case RequestAcceptable:
                button.setText(context.getText(R.string.partyfinder_acceptable));
                button.setEnabled(true);
                button.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    public List<PartyDetails> getParties() {
        return parties;
    }

    public void setParties(List<PartyDetails> parties) {
        this.parties = parties;
    }

    public interface PartyClickListener {

        void onJoinClick(PartyDetails partyDetails);

        Single<List<User>> onExpandClick(String partyId);
    }
}
