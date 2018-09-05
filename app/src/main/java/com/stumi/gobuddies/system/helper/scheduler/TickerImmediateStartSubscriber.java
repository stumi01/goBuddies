package com.stumi.gobuddies.system.helper.scheduler;

/**
 * @author stumpfb on 17/09/2016.
 */
public class TickerImmediateStartSubscriber extends TickerSubscriber {

    public TickerImmediateStartSubscriber(TickerListener tickerListener) {
        super(tickerListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        onNext(-1L);
    }
}
