package com.pzr.taoc;

import android.app.Application;

public class App extends Application {
    public static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }
}