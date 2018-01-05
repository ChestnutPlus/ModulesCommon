package com.chestnut.common.utils;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/1/4 18:29
 *     desc  :  封装一些Android系统的方法
 *              可能需要一些系统权限
 *              测试：
 *                  5.1可用
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class SystemUtils {

    private SystemUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Thanks:  http://blog.csdn.net/wzj0808/article/details/52608940
     * 保持系统CUP Active
     * 需要权限：
     *      <uses-permission android:name="android.permission.DEVICE_POWER" />
     *      <uses-permission android:name="android.permission.WAKE_LOCK" />
     */
    private static PowerManager.WakeLock wl;
    public static void keepSystemAlive(Context context) {
        try {
            if (wl!=null)
                releaseKeepSystemAlive();
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if (pm!=null) {
                wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ScreenOff");
                wl.acquire();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放锁
     */
    public static void releaseKeepSystemAlive() {
        try {
            if (wl!=null)
                wl.release();
            wl = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 唤醒屏幕 & 解锁
     * 需要权限：
     *  <uses-permission android:name="android.permission.DISABLE_KEYGUARD" />
     * @param context 上下文
     */
    public static void wakeUpAndUnlock(Context context){
        try {
            //屏锁管理器
            KeyguardManager km= (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
            //解锁
            kl.disableKeyguard();
            //获取电源管理器对象
            PowerManager pm=(PowerManager) context.getSystemService(Context.POWER_SERVICE);
            //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
            if (pm!=null) {
                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
                //点亮屏幕
                wl.acquire();
                //释放
                wl.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
