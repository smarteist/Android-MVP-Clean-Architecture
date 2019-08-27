package com.smarteist.mrnews;

public abstract class BasePresenter<T extends BaseContracts.View> implements BaseContracts.Presenter<T> {

    protected T view;

    @Override
    public void attach(T view) {
        this.view = view;
    }

    @Override
    public void detach() {
        this.view = null;
    }

    @Override
    public T getView() {
        return view;
    }

    @Override
    public void onViewCreated(android.view.View root) {
        view.initViews(root);
    }

    @Override
    public void onViewCreated(android.app.Activity root) {
        view.initViews(root.findViewById(android.R.id.content).getRootView());
    }

}
