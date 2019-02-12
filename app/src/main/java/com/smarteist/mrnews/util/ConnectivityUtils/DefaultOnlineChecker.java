package com.smarteist.mrnews.util.ConnectivityUtils;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.common.annotations.VisibleForTesting;

/**
 * Custom OnlineChecker
 */

public class DefaultOnlineChecker implements OnlineChecker {

    private final ConnectivityManager connectivityManager;

    public DefaultOnlineChecker(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    @Override
    public boolean isOnline() {
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}