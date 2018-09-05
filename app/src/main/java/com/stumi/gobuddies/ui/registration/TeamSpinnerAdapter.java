package com.stumi.gobuddies.ui.registration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.stumi.gobuddies.R;
import com.stumi.gobuddies.ui.UIUtils;

/**
 * @author stumpfb on 21/12/2016.
 */

public class TeamSpinnerAdapter extends ArrayAdapter {

    private final String[] teamTypes;

    private final LayoutInflater inflater;

    public TeamSpinnerAdapter(Context context, int textViewResourceId, String[] teamTypes,
                              LayoutInflater inflater) {
        super(context, textViewResourceId, teamTypes);
        this.inflater = inflater;
        this.teamTypes = teamTypes;
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        View layout = inflater.inflate(R.layout.spinner_row_team, parent, false);

        TextView tvLanguage = (TextView) layout.findViewById(R.id.team_select_name);
        tvLanguage.setText(teamTypes[position]);
        ImageView img = (ImageView) layout.findViewById(R.id.team_select_icon);
        img.setImageResource(UIUtils.getIconResource(position, 1));
        return layout;
    }

    // It gets a View that displays in the drop down popup the data at the specified position
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // It gets a View that displays the data at the specified position
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
}