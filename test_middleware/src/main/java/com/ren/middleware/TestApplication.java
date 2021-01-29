package com.ren.middleware;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ARouter.openLog();     // Print log
        ARouter.openDebug();
        ARouter.init(this); // As early as possible, it is recommended to initialize in the Application
    }
}
