package com.smarteist.mrnews;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class ContextContainer {

    @SuppressLint("StaticFieldLeak")
    private static ContextContainer mInstance;

    private Context mContext;

    public static ContextContainer getInstance() {
        if (mInstance == null) {
            synchronized (ContextContainer.class) {
                if (mInstance == null) {
                    mInstance = new ContextContainer();
                }
            }
        }
        return mInstance;
    }

    void init(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    @SuppressWarnings("unchecked")
    public <T extends Application> T getApplication() {
        return ((T) mContext);
    }

    public boolean isInitialized() {
        return mContext != null;
    }

}
