package com.stumi.gobuddies.ui;

import android.support.annotation.DrawableRes;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.stumi.gobuddies.R;
import com.stumi.gobuddies.model.TeamType;

import java.util.HashMap;

/**
 * Created by david on 18/09/16.
 */
public final class UIUtils {

    private static final HashMap<Pair<TeamType, Boolean>, Integer> ICON_MAP = new HashMap<>();

    private static final HashMap<TeamType, Integer> BACKGROUND_MAP = new HashMap<>();


    static {
        ICON_MAP.put(new Pair<>(TeamType.NoTeam, true), R.drawable.no_team_single);
        ICON_MAP.put(new Pair<>(TeamType.NoTeam, false), R.drawable.no_team);
        ICON_MAP.put(new Pair<>(TeamType.Instinct, true), R.drawable.instinct_single);
        ICON_MAP.put(new Pair<>(TeamType.Instinct, false), R.drawable.instinct_team);
        ICON_MAP.put(new Pair<>(TeamType.Mystic, true), R.drawable.mystic_single);
        ICON_MAP.put(new Pair<>(TeamType.Mystic, false), R.drawable.mystic_team);
        ICON_MAP.put(new Pair<>(TeamType.Valor, true), R.drawable.valor_single);
        ICON_MAP.put(new Pair<>(TeamType.Valor, false), R.drawable.valor_team);

        BACKGROUND_MAP.put(TeamType.NoTeam, R.drawable.chat_input_background_no_team);
        BACKGROUND_MAP.put(TeamType.Instinct, R.drawable.chat_input_background_instinct);
        BACKGROUND_MAP.put(TeamType.Mystic, R.drawable.chat_input_background_mystic);
        BACKGROUND_MAP.put(TeamType.Valor, R.drawable.chat_input_background_valor);
    }

    private static boolean isPartySingle(int groupSize) {
        return groupSize <= 1;
    }

    @DrawableRes
    public static Integer getBackgroundResource(int team) {
        return BACKGROUND_MAP.get(TeamType.getTeamType(team));
    }

    @DrawableRes
    public static int getIconResource(int team, int party) {
        return ICON_MAP.get(Pair.create(TeamType.getTeamType(team), isPartySingle(party)));
    }

    public static void initPartySizeBubbleTextField(TextView textView, int partySize) {
        if (partySize == 1) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setText("" + partySize);
            textView.setVisibility(View.VISIBLE);
        }
    }

    public static String getReadableDistance(String distanceInMeters) {
        double distanceM = Double.parseDouble(distanceInMeters);
        if (distanceM >= 1000) {
            return roundMToKm(distanceM) + " Km";
        }
        return round(distanceM) + " m";
    }

    private static String round(double distance) {
        return "" + lastDigitToZero(Math.round(distance));
    }

    private static long lastDigitToZero(long input) {
        return (input / 10l) * 10l;
    }

    private static String roundMToKm(double distance) {
        return String.format("%.1f", distance / 1000);
    }

}
