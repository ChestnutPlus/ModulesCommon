package com.chestnut.common.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import io.reactivex.Observable;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/2/16 18:36
 *     desc  :  精确定时器，
 *              针对于 <= 5.1 的系统，
 *              其他系统未经测试。
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */

public class ExactTimerUtils {

    private static BroadcastReceiver broadcastReceiver;

    /**
     * 设置定时器
     * Thanks : http://blog.csdn.net/bingshushu/article/details/50433643
     *
     * @param actionAfterTimeMs    毫秒，既是，多少毫秒后，触发
     * @param action    触发，以广播形式，传入广播的action
     */
    public static Observable<Boolean> setAlarmRx(int actionAfterTimeMs, String action, Context applicationContext) {
        return Observable.create(e -> {
            IntentFilter filter = new IntentFilter(action);
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent!=null && intent.getAction()!=null && intent.getAction().equalsIgnoreCase(action)) {
                        e.onNext(true);
                        e.onComplete();
                    }
                }
            };
            applicationContext.registerReceiver(broadcastReceiver,filter);
            setAlarm(actionAfterTimeMs, action, applicationContext);
        }).map(a->{
            try {
                if (broadcastReceiver!=null)
                    applicationContext.unregisterReceiver(broadcastReceiver);
                broadcastReceiver = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        });
    }

    /**
     * 设置定时器
     * Thanks : http://blog.csdn.net/bingshushu/article/details/50433643
     *
     * @param actionAfterTimeMs    毫秒，既是，多少毫秒后，触发
     * @param action    触发，以广播形式，传入广播的action
     */
    public static void setAlarm(int actionAfterTimeMs, String action, Context applicationContext) {
        try {
            AlarmManager am = (AlarmManager) applicationContext.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(action);
            PendingIntent sender = PendingIntent.getBroadcast(applicationContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            if (am!=null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    //参数2是开始时间、参数3是允许系统延迟的时间
                    am.setWindow(AlarmManager.RTC, System.currentTimeMillis() + actionAfterTimeMs, 500, sender);
                } else {
                    am.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + actionAfterTimeMs, 500, sender);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 取消对应action的定时器
     *
     * @param action    action
     */
    public static void canalAlarm(String action, Context applicationContext) {
        try {
            Intent intent = new Intent(action);
            PendingIntent pi = PendingIntent.getBroadcast(applicationContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager am = (AlarmManager) applicationContext.getSystemService(Context.ALARM_SERVICE);
            if (am!=null)
                am.cancel(pi);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
