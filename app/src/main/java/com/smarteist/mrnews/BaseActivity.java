package com.smarteist.mrnews;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

import com.smarteist.mrnews.util.LocaleHelper;

import dagger.android.DaggerApplication;
import dagger.android.support.DaggerAppCompatActivity;
import dagger.internal.Beta;

@Beta
@SuppressLint("Registered")
public abstract class BaseActivity extends DaggerAppCompatActivity
        implements DaggerApplication.ActivityLifecycleCallbacks {

    protected App application = ContextContainer.getInstance().getApplication();

    public BaseActivity() {
        // for each activity this functions is called and so we can setup our customizations
        application.registerActivityLifecycleCallbacks(this);
    }

    /**
     * @param newBase base context locale will be set here
     *                (force locale and language selection).
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        //farsi is default locale
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "fa"));
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // Make to run your application only in LANDSCAPE mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Make to run your application only in portrait mode
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
