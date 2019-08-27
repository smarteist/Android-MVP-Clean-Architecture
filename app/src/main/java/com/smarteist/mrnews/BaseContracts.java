package com.smarteist.mrnews;

/**
 * Created by Ali Hosseini on 6/24/2018.
 */
public interface BaseContracts {

    interface View<T extends BaseContracts.Presenter> {

        String TAG = "View !!! -> ";

        void initViews(android.view.View parentRoot);

        T getPresenter();

    }

    interface Presenter<T extends BaseContracts.View> {

        String TAG = "Presenter !!! -> ";

        void attach(T view);

        void detach();

        T getView();

        void onViewCreated(android.view.View root);

        void onViewCreated(android.app.Activity root);

        void onViewResume();

        void onViewStopped();

        void onViewDestroyed();
    }

}
