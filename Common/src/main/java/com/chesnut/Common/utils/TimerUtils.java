package com.chesnut.Common.utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * <pre>
 *     author: Chestnut
 *     blog  :
 *     time  : 2016年11月24日22:22:37
 *     desc  : 定时器相关工具类
 *     thanks To:
 *     dependent on:
 * </pre>
 */
public class TimerUtils {

    private static Timer timer = null;
    private static boolean isSetStar = false;
    public interface Callback {
        void TimerStar(long time);
        void TimerEnd(long time);
    }

    /**
     * 设置一个定时器，非全局，通过回调去通知结果
     *
     * @param timeMS    时间
     * @param callback  回调用
     */
    public static Timer setTimer(long timeMS,Callback callback) {
        Timer timer = new Timer();
        if (callback!=null)
            callback.TimerStar(timeMS);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (callback!=null)
                    callback.TimerEnd(timeMS);
            }
        },timeMS);
        return timer;
    }

    /**
     * 设置一个全局的定时器，通过回调接口进行回调通知
     * 如果多次设置的话，要在上一次的定时器跑完后
     * 才有效果，也就是说，这个全局只有一个。
     *
     * @param timeMS    时间。
     * @param callback  回调。
     * @return 是否设置成功
     */
    public static boolean setAsyncGlobalTimer(long timeMS,Callback callback) {
        if (isSetStar)
            return false;
        isSetStar = true;
        timer = new Timer();
        if (callback!=null)
            callback.TimerStar(timeMS);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (callback!=null)
                    callback.TimerEnd(timeMS);
                isSetStar = false;
                timer.cancel();
                timer = null;
            }
        },timeMS);
        return true;
    }

    /**
     * 停止全局的定时器，回调的结束方法不会被回调
     */
    public static void stopAsyncGlobalTimer() {
        if (timer!=null)
            try {
                isSetStar = false;
                timer.cancel();
            }catch (Exception e) {

            }
    }

    /**
     * 停止定时器
     * @param timer
     */
    public static void stopTimer(Timer timer) {
        if (timer!=null)
            try {
                timer.cancel();
            }catch (Exception e) {

            }
    }

    /**
     * 设置一个全局定时器，
     * @param timeMS
     */
    public static void setSyncGlobbalTimer(long timeMS) {
        try {
            Thread.sleep(timeMS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
