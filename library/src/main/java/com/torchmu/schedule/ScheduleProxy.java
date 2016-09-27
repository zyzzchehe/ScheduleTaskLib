package com.torchmu.schedule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

/**
 * description：添加/删除定时任务代理类
 * creator：Mu Peiwen
 * create time：2016/3/9 15:02
 * reasons for modification：
 * Modifier：Mu Peiwen
 * Modify time：2016/3/9 15:02
 */
public class ScheduleProxy {
    
    private static ScheduleProxy sProxy;
    
    private static Context sContext;

    public static void init(Context context) {
        sContext = context.getApplicationContext();
    }

    public static ScheduleProxy getInstance() {
        if (null == sProxy) {
            synchronized (ScheduleProxy.class) {
                if (null == sProxy) {
                    sProxy = new ScheduleProxy();
                }
            }
        }
        return sProxy;
    }

    private final String TAG = getClass().getSimpleName();
    private AlarmManager mAlarmManager;

    public ScheduleProxy() {
        //初始化闹钟组件
        mAlarmManager = (AlarmManager) sContext.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
    }

    /**
     * 注册定时任务
     *
     * @param task  定时任务
     */
    public void register(ScheduleTask task) {
        if (null == sContext || null == task) {
            return;
        }
        Log.d(TAG, "register: id=" + task.getId());

        // 取消上一次
        unregister(task);
        // 添加到队列
        ScheduleQueue.getInstance().add(task);
        // 设置下一个定时
        rearrangeAccurate(task);
    }

    public boolean containTask(ScheduleTask task) {
        if (null == task)   return false;
        return ScheduleQueue.getInstance().contain(task.getId());
    }

    /**
     * 为指定任务设置下一个闹钟
     */
    public void rearrangeAccurate(ScheduleTask task) {
        if (null == task)   return;

        if (task.isCycleFinish()) {
            unregister(task);
            Log.d(TAG, "rearrangeAccurate id:" + task.getId() + " Cycle Finished");
        } else {
            //定时开启service的Intent
            PendingIntent pi = getPendingIntent(sContext, task.getId());

            //设置立即触发
            mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + task.getIntervalTime(), pi);
        }
    }

    /**
     * 注销定时任务
     * @param task  定时任务
     */
    public void unregister(ScheduleTask task) {
        if (null != task) {
            unregister(task.getId());
        }
    }

    public synchronized void unregister(String id) {
        Log.d(TAG, "unregister: id=" + id);
        if (null == sContext || null == id) {
            return;
        }

        // 队列移除
        ScheduleQueue.getInstance().remove(id);

        // 结束定时
        mAlarmManager.cancel(getPendingIntent(sContext, id));
    }

    /**
     * 获取启动service的pendingIntent
     */
    private PendingIntent getPendingIntent(Context context, String id) {
        Intent intent = new Intent(context, ScheduleService.class);
        intent.setAction(id);   //影响不同任务的Intent.filterEquals()返回值不同，同时保证同一task的cancel和setRepeat的intent保持一致
        intent.putExtra(ScheduleTask.ID, id);
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
