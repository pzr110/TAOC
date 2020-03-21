package com.pzr.taoc;

import android.app.Application;

import cn.bmob.v3.Bmob;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this,"e7c3309a0542e6305b5481e486aaa295");

    }
}
