package com.smarteist.mrnews.util.ChromeTabsUtils;

import android.content.Context;
import android.net.Uri;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.v4.content.ContextCompat;

import com.smarteist.mrnews.R;


public class ChromeTabsWrapper implements ServiceConnectionCallback {
    private static final String CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";

    private Context mContext;
    private CustomTabsServiceConnection mConnection;
    private CustomTabsClient mClient;

    public ChromeTabsWrapper(Context context) {
        mContext = context;
    }


    public void openCustomtab(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setExitAnimations(mContext, R.anim.fade_in, R.anim.fade_out);
        builder.setToolbarColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(mContext, Uri.parse(url));
    }

    public void bindCustomTabsService() {
        if (mClient != null) return;
        if (mConnection == null) {
            mConnection = new ServiceConnection(this);
        }
        CustomTabsClient.bindCustomTabsService(mContext, CUSTOM_TAB_PACKAGE_NAME, mConnection);
    }

    public void unbindCustomTabsService() {
        if (mConnection == null) return;
        mContext.unbindService(mConnection);
        mClient = null;
        mConnection = null;
    }

    @Override
    public void onServiceConnected(CustomTabsClient client) {
        mClient = client;
    }

    @Override
    public void onServiceDisconnected() {
        mClient = null;
    }
}
