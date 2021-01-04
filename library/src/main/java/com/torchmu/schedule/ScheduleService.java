package com.torchmu.schedule;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;


/**
 * date: 2016/5/28 14:09
 *
 * @author mupeiwen
 */
public class ScheduleService extends Service {

    private String TAG = getClass().getSimpleName();

    private Handler mMainThreadHandler;

    public ScheduleService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMainThreadHandler = new Handler();
        Log.d(TAG, this + " onCreate " + Thread.currentThread());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, this + " onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String id = intent.getStringExtra(ScheduleTask.ID);

        Log.d(TAG, this + " onStartCommand ID:" + id);

        if (null != id) {
            handleTask(id);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void handleTask(String id) {
        ScheduleTask task = ScheduleQueue.sInstance.get(id);
        if (null != task) {
            task.onTimeUp();

            ScheduleProxy.getInstance().rearrangeAccurate(task);
        }
    }


    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, this + " onUnbind");

        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, this + " onRebind");

        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, this + " onDestroy");
        mMainThreadHandler = null;
        super.onDestroy();
    }

    class TimeUpRunnable implements Runnable {

        String id;

        public TimeUpRunnable(String id) {
            this.id = id;
        }

        @Override
        public void run() {
            ScheduleTask task = ScheduleQueue.sInstance.get(id);
            if (null != task) {
                task.callback();
            }
        }
    }
}
