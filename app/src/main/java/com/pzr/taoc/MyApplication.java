package com.pzr.taoc;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;

public class MyApplication extends Application {

    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this,"e7c3309a0542e6305b5481e486aaa295");

        context = this;

    }


    /**
     * 获取全局上下文*/
    public static Context getContext() {
        return context;
    }

}
