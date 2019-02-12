package com.smarteist.mrnews;

import android.os.Bundle;

/**
 * Created by Ali Hosseini on 6/24/2018.
 */
public interface BaseContracts {

    interface View<T extends BaseContracts.Presenter> {

        void initViews();

        T getPresenter();

    }

    abstract class Presenter<T extends BaseContracts.View> {

        protected T view;

        public void attach(T view) {
            this.view = view;
        }

        public void detach() {
            this.view = null;
        }

        public T getView() {
            return view;
        }

        public abstract void viewCreated();

        public abstract void viewResume();

        public abstract void viewStopped();

        public abstract void viewDestroyed();
    }

}
