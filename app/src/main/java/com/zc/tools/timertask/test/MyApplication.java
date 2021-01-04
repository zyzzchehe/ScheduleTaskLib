package com.zc.tools.timertask.test;

import android.app.Application;

import com.torchmu.schedule.ScheduleProxy;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // step 1 of 3, initialize
        ScheduleProxy.init(this);
    }
}
