package com.stumi.gobuddies.ui.floating.tabs.partyFinder.party;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;
import com.stumi.gobuddies.R;
import com.stumi.gobuddies.model.User;
import com.stumi.gobuddies.ui.UIUtils;

import java.util.List;

/**
 * @author stumpfb on 26/10/2016.
 */
public class PartyFinderMemberAdapter extends SupportAnnotatedAdapter
        implements PartyFinderMemberAdapterBinder {

    @ViewType(
            layout = R.layout.item_party_member,
            views = {@ViewField(id = R.id.party_member_icon, name = "icon", type = ImageView.class),
                    @ViewField(id = R.id.party_member_name, name = "name", type = TextView.class)})
    public final int VIEWTYPE_MEMBERS = 0;

    private final List<User> users;

    public PartyFinderMemberAdapter(Context context, List<User> users) {
        super(context);
        this.users = users;
    }

    @Override
    public int getItemCount() {
        return users == null ? 0 : users.size();
    }

    @Override
    public void bindViewHolder(PartyFinderMemberAdapterHolders.VIEWTYPE_MEMBERSViewHolder vh, int position) {
        User user = users.get(position);
        vh.icon.setImageResource(UIUtils.getIconResource(user.getTeam(), 1));
        vh.name.setText(user.getName());
    }

}
