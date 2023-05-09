package com.ldi19.notyourenemy.app;

/**
 * Created by macair on 23/05/2018.
 */

import android.app.Application;
import android.content.Context;

public class AppContext extends Application {

    private static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }
}