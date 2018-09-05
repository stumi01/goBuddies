package com.stumi.gobuddies.ui.floating.tabs.partyFinder;

/**
 * @author stumpfb on 20/08/2016.
 */
public class AcceptJoinResponseModel {

    private boolean lead;


    public boolean isLead() {
        return lead;
    }

    public void setLead(boolean lead) {
        this.lead = lead;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AcceptJoinResponseModel that = (AcceptJoinResponseModel) o;

        return lead == that.lead;

    }

    @Override
    public int hashCode() {
        return (lead ? 1 : 0);
    }

    @Override
    public String toString() {
        return "AcceptJoinResponseModel{" +
                "lead=" + lead +
                '}';
    }
}
