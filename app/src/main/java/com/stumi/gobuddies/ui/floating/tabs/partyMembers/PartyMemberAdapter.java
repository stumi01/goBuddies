package com.stumi.gobuddies.ui.floating.tabs.partyMembers;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;
import com.stumi.gobuddies.R;
import com.stumi.gobuddies.model.TeamType;
import com.stumi.gobuddies.model.User;
import com.stumi.gobuddies.system.animation.ButtonAnimationOnTouchListener;
import com.transitionseverywhere.Rotate;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.List;

/**
 * @author stumpfb on 28/08/2016.
 */
public class PartyMemberAdapter extends SupportAnnotatedAdapter implements PartyMemberAdapterBinder {

    @ViewType(
            layout = R.layout.item_team_member,
            views = {@ViewField(
                    id = R.id.team_member_icon,
                    name = "icon",
                    type = ImageView.class), @ViewField(
                    id = R.id.team_member_name,
                    name = "name",
                    type = TextView.class), @ViewField(
                    id = R.id.team_member_leader_icon,
                    name = "leaderIcon",
                    type = ImageView.class), @ViewField(
                    id = R.id.team_member_buttons,
                    name = "buttonsView",
                    type = LinearLayout.class), @ViewField(
                    id = R.id.pass_button,
                    name = "passButton",
                    type = Button.class), @ViewField(
                    id = R.id.kick_button,
                    name = "kickButton",
                    type = Button.class), @ViewField(
                    id = R.id.team_member_edit,
                    name = "memberEdit",
                    type = ImageView.class), @ViewField(
                    id = R.id.team_member_row_layout,
                    name = "rowLayout",
                    type = View.class)})

    public final int VIEWTYPE_TEAM = 0;

    private List<User> members;

    private boolean currentUserLeader;

    private Context context;
    private final PassButtonClickListener listener;

    public PartyMemberAdapter(Context context, PassButtonClickListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return members != null ? members.size() : 0;
    }

    @Override
    public void bindViewHolder(PartyMemberAdapterHolders.VIEWTYPE_TEAMViewHolder vh, int position) {
        final User user = members.get(position);
        initIcon(vh.icon, user);
        vh.name.setText(user.getName());
        initLeaderIcon(vh, user);
        initButtons(vh, user);
    }

    private void initButtons(PartyMemberAdapterHolders.VIEWTYPE_TEAMViewHolder vh, User user) {
        if (currentUserLeader && !user.isLeader()) {
            initMemberButtons(vh, user);
        } else {
            hideAllButtons(vh);
        }
    }

    private void initLeaderIcon(PartyMemberAdapterHolders.VIEWTYPE_TEAMViewHolder vh, User user) {
        if (user.isLeader()) {
            vh.leaderIcon.setVisibility(View.VISIBLE);
        } else {
            vh.leaderIcon.setVisibility(View.GONE);
        }
    }

    private void initMemberButtons(PartyMemberAdapterHolders.VIEWTYPE_TEAMViewHolder vh, User user) {
        vh.memberEdit.setVisibility(View.VISIBLE);
        vh.rowLayout.setOnClickListener(v -> switchMemberButtons(vh));
        vh.buttonsView.setVisibility(View.GONE);
        vh.passButton.setOnClickListener(v -> {
            listener.onPassClick(user);
            setMemberEditVisible(vh);
        });
        vh.passButton.setOnTouchListener(new ButtonAnimationOnTouchListener(context));

        vh.kickButton.setOnClickListener(v -> {
            listener.onKickClick(user);
            setMemberEditVisible(vh);
        });
        vh.kickButton.setOnTouchListener(new ButtonAnimationOnTouchListener(context));

    }

    private void hideAllButtons(PartyMemberAdapterHolders.VIEWTYPE_TEAMViewHolder vh) {
        vh.buttonsView.setVisibility(View.GONE);
        vh.memberEdit.setVisibility(View.GONE);
    }

    private void switchMemberButtons(PartyMemberAdapterHolders.VIEWTYPE_TEAMViewHolder vh) {
        createTransitionSet(vh);
        if (vh.buttonsView.getVisibility() == View.VISIBLE) {
            vh.memberEdit.setRotation(0);
            vh.buttonsView.setVisibility(View.GONE);
        } else {
            vh.buttonsView.setVisibility(View.VISIBLE);
            vh.memberEdit.setRotation(-180);
        }
    }

    private void createTransitionSet(PartyMemberAdapterHolders.VIEWTYPE_TEAMViewHolder vh) {
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.setDuration(200);
        transitionSet.addTransition(new Rotate());
        transitionSet.addTransition(new Slide(Gravity.LEFT));
        TransitionManager.beginDelayedTransition((ViewGroup) vh.itemView, transitionSet);
    }

    private void setMemberEditVisible(PartyMemberAdapterHolders.VIEWTYPE_TEAMViewHolder vh) {
        createTransitionSet(vh);
        vh.memberEdit.setRotation(0);
        vh.buttonsView.setVisibility(View.GONE);
        vh.memberEdit.setVisibility(View.VISIBLE);
    }

    private void initIcon(ImageView icon, User user) {
        switch (TeamType.getTeamType(user.getTeam())) {
            case NoTeam:
                icon.setImageResource(R.drawable.no_team_single);
                break;
            case Instinct:
                icon.setImageResource(R.drawable.instinct_single);
                break;
            case Mystic:
                icon.setImageResource(R.drawable.mystic_single);
                break;
            case Valor:
                icon.setImageResource(R.drawable.valor_single);
                break;
            default:
                break;
        }
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public void setData(PartyMembers data) {
        setMembers(data.getFullPartyInitialized());
        currentUserLeader = data.getIsCurrentUserPartyLeader();
    }

    public interface PassButtonClickListener {

        void onPassClick(User user);

        void onKickClick(User user);
    }
}
