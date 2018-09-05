package com.stumi.gobuddies.base.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import butterknife.ButterKnife;
import icepick.Icepick;

/**
 * @author stumpfb on 27/08/2016.
 */
public abstract class MvpBaseLceFragment<CV extends View, M, V extends MvpLceView<M>, P extends MvpPresenter<V>> extends MvpLceFragment<CV, M, V, P> {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Icepick.restoreInstanceState(this, savedInstanceState);
        return inflater.inflate(getLayoutRes(), container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    protected abstract int getLayoutRes();


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        injectDependencies();
        ButterKnife.bind(this, view);
        super.onViewCreated(view, savedInstanceState);
        onViewCreated(savedInstanceState);
    }

    protected abstract void injectDependencies();


    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return null;
    }


    protected abstract void onViewCreated(Bundle savedInstanceState);


}
