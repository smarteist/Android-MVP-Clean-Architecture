package com.smarteist.mrnews;

import com.smarteist.mrnews.di.AppComponent;
import com.smarteist.mrnews.di.DaggerAppComponent;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * We create a custom App class that extends  {@link DaggerApplication}.
 * We then override applicationInjector() which tells Dagger how to make our @ApplicationScoped Component
 * We never have to call `component.inject(this)` as {@link DaggerApplication} will do that for us.
 */
public class App extends DaggerApplication {

    AppComponent component;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        component = DaggerAppComponent.builder().application(this).build();
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ContextContainer.getInstance().init(this);
    }

}

