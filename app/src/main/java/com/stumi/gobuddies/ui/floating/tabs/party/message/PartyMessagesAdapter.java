package com.stumi.gobuddies.ui.floating.tabs.party.message;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;
import com.stumi.gobuddies.R;
import com.stumi.gobuddies.ui.UIUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * @author stumpfb on 29/08/2016.
 */
public class PartyMessagesAdapter extends SupportAnnotatedAdapter implements PartyMessagesAdapterBinder {

    private List<PartyMessage> messages = new ArrayList<>();

    @ViewType(
            layout = R.layout.item_team_message,
            views = {@ViewField(
                    id = R.id.message_sender_icon,
                    name = "senderIcon",
                    type = ImageView.class), @ViewField(
                    id = R.id.message_sender_leader_icon,
                    name = "senderCrown",
                    type = ImageView.class), @ViewField(
                    id = R.id.message_sender_name,
                    name = "senderName",
                    type = TextView.class), @ViewField(
                    id = R.id.message_text,
                    name = "message",
                    type = TextView.class), @ViewField(
                    id = R.id.message_sender_time,
                    name = "time",
                    type = TextView.class), @ViewField(
                    id = R.id.message_self_name,
                    name = "selfName",
                    type = TextView.class), @ViewField(
                    id = R.id.message_self_icon,
                    name = "selfIcon",
                    type = ImageView.class), @ViewField(
                    id = R.id.message_self_leader_icon,
                    name = "selfCrown",
                    type = ImageView.class), @ViewField(
                    id = R.id.message_self_text,
                    name = "selfMessage",
                    type = TextView.class), @ViewField(
                    id = R.id.message_self,
                    name = "selfLayout",
                    type = View.class), @ViewField(
                    id = R.id.message_self_time,
                    name = "selfTime",
                    type = TextView.class), @ViewField(
                    id = R.id.message_outer,
                    name = "outerLayout",
                    type = View.class), @ViewField(
                    id = R.id.message_system_text,
                    name = "systemText", //TODO padding to the bottom.
                    type = AppCompatTextView.class)

            })
    public final int VIEWTYPE_MESSAGE = 0;

    public PartyMessagesAdapter(Context context) {
        super(context);
    }

    @Override
    public void bindViewHolder(PartyMessagesAdapterHolders.VIEWTYPE_MESSAGEViewHolder vh, int position) {
        PartyMessage message = messages.get(position);
        if (message.isSystem()) {
            manageSystemMessage(vh, message);
        } else {
            manageUserMessage(vh, message);
        }
    }

    private void manageSystemMessage(PartyMessagesAdapterHolders.VIEWTYPE_MESSAGEViewHolder vh,
                                     PartyMessage message) {
        vh.outerLayout.setVisibility(View.GONE);
        vh.selfLayout.setVisibility(View.GONE);
        vh.systemText.setVisibility(View.VISIBLE);
        vh.systemText.setText(message.getMessage());
    }

    private void manageUserMessage(PartyMessagesAdapterHolders.VIEWTYPE_MESSAGEViewHolder vh,
                                   PartyMessage message) {
        vh.systemText.setVisibility(View.GONE);
        if (message.isMe()) {
            manageSelfMessage(vh, message);
        } else {
            manageOuterMessage(vh, message);
        }
    }

    private void manageOuterMessage(PartyMessagesAdapterHolders.VIEWTYPE_MESSAGEViewHolder vh,
                                    PartyMessage message) {
        vh.outerLayout.setVisibility(View.VISIBLE);
        vh.selfLayout.setVisibility(View.GONE);
        vh.time.setText(formatTime(message.getTime()));
        vh.message.setText(message.getMessage());
        vh.senderName.setText(message.getName());
        vh.senderIcon.setImageResource(UIUtils.getIconResource(message.getTeam(), 1));
        vh.senderCrown.setVisibility(message.isLead() ? View.VISIBLE : View.GONE);
    }

    private void manageSelfMessage(PartyMessagesAdapterHolders.VIEWTYPE_MESSAGEViewHolder vh,
                                   PartyMessage message) {
        vh.outerLayout.setVisibility(View.GONE);
        vh.selfLayout.setVisibility(View.VISIBLE);
        vh.selfTime.setText(formatTime(message.getTime()));
        vh.selfMessage.setText(message.getMessage());
        vh.selfName.setText(message.getName());
        vh.selfIcon.setImageResource(UIUtils.getIconResource(message.getTeam(), 1));
        vh.selfCrown.setVisibility(message.isLead() ? View.VISIBLE : View.GONE);
    }

    private String formatTime(long elapsedSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, -(int) elapsedSeconds);
        return String.format(Locale.ENGLISH, "%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE));
    }

    @Override
    public int getItemCount() {
        return messages == null ? 0 : messages.size();
    }

    public void setMessages(List<PartyMessage> messages) {
        this.messages = messages;
    }

    public void appendMessages(List<PartyMessage> msgs) {
        messages.addAll(msgs);
        this.notifyDataSetChanged();
    }
}
