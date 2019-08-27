package com.smarteist.mrnews.di.modules;


import android.app.Application;
import android.content.Context;

import com.smarteist.mrnews.di.AppComponent;

import dagger.Binds;
import dagger.Module;

/**
 * This is the app's Dagger module. We use this to bind our App class as a Context in the AppComponent.
 * By using Dagger Android we do not need to pass our App instance to any module,
 * we simply need to expose our App as Context.
 * through Dagger.Android our App & Activities are provided into your graph for us.
 * {@link
 * AppComponent}.
 */
@Module
public abstract class AppModule {
    //expose App as an injectable context
    @Binds
    abstract Context bindContext(Application application);
}