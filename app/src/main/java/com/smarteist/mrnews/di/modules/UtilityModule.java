package com.smarteist.mrnews.di.modules;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import com.smarteist.mrnews.di.scopes.ApplicationScoped;
import com.smarteist.mrnews.util.ConnectivityUtils.DefaultOnlineChecker;
import com.smarteist.mrnews.util.ConnectivityUtils.OnlineChecker;
import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;


@Module
public class UtilityModule {

    @Provides
    @ApplicationScoped
    ConnectivityManager provideConnectivityManager(Application context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides
    @ApplicationScoped
    OnlineChecker onlineChecker(ConnectivityManager cm) {
        return new DefaultOnlineChecker(cm);
    }

    @ApplicationScoped
    @Provides
    Picasso providePicasso(Application app) {
        return Picasso.with(app);
    }
}
