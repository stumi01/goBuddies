package com.stumi.gobuddies.base;

import com.annimon.stream.Stream;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.stumi.gobuddies.system.network.request.Cancellable;

import java.util.HashSet;
import java.util.Set;

/**
 * @author stumpfb on 18/01/2017.
 */

public class RequestAwarePresenter<V extends MvpView> extends MvpBasePresenter<V> {

    private Set<Cancellable> requests = new HashSet<>();

    protected void watchForCancel(Cancellable cancellable) {
        requests.add(cancellable);
    }

    protected void cancelAll() {
        Stream.of(requests).forEach(Cancellable::cancel);
        requests.clear();
    }

    protected boolean isRequestsEmpty() {
        return requests.isEmpty();
    }
}
