package com.smarteist.mrnews;

import android.support.annotation.CallSuper;

public abstract class BasePresenter<T extends BaseContracts.View> implements BaseContracts.Presenter<T> {

    protected T view;

    @CallSuper
    @Override
    public void attach(T view) {
        this.view = view;
    }

    @CallSuper
    @Override
    public void detach() {
        this.view = null;
    }

    @Override
    public T getView() {
        return view;
    }

    @CallSuper
    @Override
    public void onViewCreated(android.view.View root) {
        view.initViews(root);
    }

    @CallSuper
    @Override
    public void onViewCreated(android.app.Activity root) {
        view.initViews(root.findViewById(android.R.id.content).getRootView());
    }

}
