package com.torchmu.schedule;

import java.util.HashMap;
import java.util.Map;

/**
 * 定时任务队列
 * date: 2016/5/30 14:02
 *
 * @author mupeiwen
 */
public class ScheduleQueue {

    static ScheduleQueue sInstance = new ScheduleQueue();

    Map<String, ScheduleTask> mTaskMap = new HashMap<>();

    public static ScheduleQueue getInstance() {
        return sInstance;
    }

    public void add(ScheduleTask task) {
        if (null == task || null == task.getId())   return;
        mTaskMap.put(task.getId(), task);
    }

    public synchronized boolean contain(String id) {
        return mTaskMap.containsKey(id);
    }

    public synchronized ScheduleTask get(String id) {
        return mTaskMap.get(id);
    }

    public synchronized ScheduleTask remove(String id) {
        return mTaskMap.remove(id);
    }
}
