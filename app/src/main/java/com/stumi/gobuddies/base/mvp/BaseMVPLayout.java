package com.stumi.gobuddies.base.mvp;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.layout.MvpFrameLayout;
import com.stumi.gobuddies.R;

import org.osmdroid.config.Configuration;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by sourc on 2017. 03. 10..
 */

public abstract class BaseMVPLayout<V extends MvpView, P extends MvpPresenter<V>> extends MvpFrameLayout<V, P> {

    protected final Context context;
    private Unbinder unbinder;
    protected View rootView;

    public BaseMVPLayout(@NonNull Context context) {
        super(context);
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        this.context = new ContextThemeWrapper(context, R.style.Theme_AppCompat_Translucent);
        rootView = LayoutInflater.from(this.context).inflate(getLayoutRes(), this);
        unbinder = ButterKnife.bind(this, this);
    }


    protected abstract int getLayoutRes();

}
