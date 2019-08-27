package com.smarteist.mrnews.util.ConnectivityUtils;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Handler;
import android.util.Log;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

/**
 * Custom OnlineChecker
 */

public class DefaultOnlineChecker implements OnlineChecker {

    private final Application application;
    private final ConnectivityManager connectivityManager;
    private String TAG = "DefaultOnlineChecker";


    public DefaultOnlineChecker(Application application) {
        this.application = application;
        this.connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public boolean isOnline() {
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    @Override
    public void setOnNetworkStateChangedListener(final OnlineCheckerListener listener) {

        final Handler mainHandler = new Handler(application.getMainLooper());


        if (SDK_INT >= LOLLIPOP) {

            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR);
            builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
            builder.addTransportType(NetworkCapabilities.TRANSPORT_VPN);

            ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback() {

                @Override
                public void onAvailable(Network network) {
                    super.onAvailable(network);
                    Log.i(TAG, "isOnline: " + isOnline());
                    Runnable publish = new Runnable() {
                        @Override
                        public void run() {
                            listener.networkStateChanged(isOnline());
                        }
                    };
                    mainHandler.post(publish);
                }

                @Override
                public void onLost(Network network) {
                    super.onLost(network);
                    Log.i(TAG, "isOnline: " + isOnline());
                    Runnable publish = new Runnable() {
                        @Override
                        public void run() {
                            listener.networkStateChanged(isOnline());
                        }
                    };
                    mainHandler.post(publish);
                }

            };
            connectivityManager.registerNetworkCallback(builder.build(), callback);

        } else {

            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Log.i(TAG, "isOnline: " + isOnline());
                    Runnable publish = new Runnable() {
                        @Override
                        public void run() {
                            listener.networkStateChanged(isOnline());
                        }
                    };
                    mainHandler.post(publish);
                }
            };

            application.registerReceiver(receiver, filter);

        }

    }

}