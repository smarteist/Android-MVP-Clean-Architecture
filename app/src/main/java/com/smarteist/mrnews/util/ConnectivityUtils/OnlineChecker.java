package com.smarteist.mrnews.util.ConnectivityUtils;

/**
 * Simple interface that contains online/offline state indicator
 */

public interface OnlineChecker {

    boolean isOnline();

    void setOnNetworkStateChangedListener(OnlineCheckerListener listener);

    interface OnlineCheckerListener {

        void networkStateChanged(boolean isOnline);
    }
}