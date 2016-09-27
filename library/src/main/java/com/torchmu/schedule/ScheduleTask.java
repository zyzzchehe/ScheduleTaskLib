package com.torchmu.schedule;

public abstract class ScheduleTask {
    // ---------- 内部用变量 ---------------
    /** 内部自动分配的默认id */
    private static int sTaskId = 1;
    private static final long DEFAULT_INTERVAL = 60 * 1000L;

    /** 当前TASK的id */
    protected String mId;
    /** 上一次任务执行时的时间 */
    protected long mLastCallBackTime = 0L;
    /** 当前已经循环的次数 */
    protected int mCurrentLoop = 1;

    // ---------- 业务方可以使用的值 --------
    public static final String ID = "TASK_ID";
    /** 无限循环事件的标志 */
    public static final int CYCLE_INFINITY = -1;

    /** 定时间隔，默认1分钟 */
    protected long mIntervalTime;
    /** 重复次数，默认为-1表示无限重复 */
    protected int mCycleLoop;



    public ScheduleTask(String id, long interval) {
        this(id, interval, CYCLE_INFINITY);
    }

    public ScheduleTask(String id, long interval, int loops) {
        setId(id);
        setIntervalTime(interval);
        setCycleLoop(loops);
    }

    /**
     * 定时任务到期回调
     */
    public abstract void callback();

    public void onTimeUp() {
        callback();
        mCurrentLoop++;
        mLastCallBackTime = System.currentTimeMillis();
    }

    /**
     * 当前是否执行完了所有循环周期
     */
    public boolean isCycleFinish() {
       return mCurrentLoop >= mCycleLoop && mCycleLoop != CYCLE_INFINITY;
    }

    public int getCycleLoop() {
        return mCycleLoop;
    }

    public int getCurrentLoop() {
        return mCurrentLoop;
    }

    /**
     * 设置循环次数，默认为无限循环
     */
    public void setCycleLoop(int cycleTime) {
        this.mCycleLoop = cycleTime > 0 ? cycleTime : CYCLE_INFINITY;
    }

    public String getId() {
        return mId;
    }

    public void setId(String tag) {
        mId = null == tag || tag.isEmpty() ?
                "ACTION_ALARM_" +  System.currentTimeMillis() + "_" + sTaskId++ : tag;
    }

    public long getIntervalTime() {
        return mIntervalTime;
    }

    /**
     * 设置循环间隔时间，最短为5s
     */
    public void setIntervalTime(long interval) {
        this.mIntervalTime = interval > 0 ? interval : DEFAULT_INTERVAL;
    }

    public long getLastCallBackTime() {
        return mLastCallBackTime;
    }

}
