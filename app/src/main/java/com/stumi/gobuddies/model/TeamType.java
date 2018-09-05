package com.stumi.gobuddies.model;

/**
 * @author stumpfb on 20/08/2016.
 */
public enum TeamType {
    NoTeam, Instinct, Mystic, Valor;

    public static TeamType getTeamType(int team) {
        switch (team) {
            case 0:
                return TeamType.NoTeam;
            case 1:
                return TeamType.Instinct;
            case 2:
                return TeamType.Mystic;
            case 3:
                return TeamType.Valor;
            default:
                return TeamType.NoTeam;
        }
    }
}
