package com.chestnut.Common.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.annotation.DrawableRes;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/5/30 12:04
 *     desc  :  封装系统的Notification
 *     thanks To:
 *          1.  http://www.jianshu.com/p/e1e20e0ee18c
 *     dependent on:
 *     update log:
 * </pre>
 */

public class NotificationUtils {

    /*  summary
        设置系统默认提醒效果，一旦设置默认提醒效果，则自定义的提醒效果会全部失效。具体可看源码
        <uses-permission android:name="android.permission.VIBRATE" />
        Notification.DEFAULT_VIBRATE    //添加默认震动效果,需要申请震动权限
        Notification.DEFAULT_SOUND      //添加系统默认声音效果，设置此值后，调用setSound()设置自定义声音无效
        Notification.DEFAULT_LIGHTS     //添加默认呼吸灯效果，使用时须与 Notification.FLAG_SHOW_LIGHTS 结合使用，否则无效
        Notification.DEFAULT_ALL        //添加上述三种默认提醒效果
    */

    /**
     * 发送一个简单的默认通知：
     * @param context   上下文
     * @param ticker    摘要
     * @param title     标题
     * @param text      内容
     * @param icon      icon
     * @param msgId     消息ID
     * @param pendingIntent 跳转Action
     */
    public static void sendDefault(Context context,
                                   String ticker,
                                   String title,
                                   String text,
                                   @DrawableRes int icon,
                                   int msgId,
                                   PendingIntent pendingIntent) {
        send(context, ticker, title, text, icon, msgId, true, pendingIntent, Notification.DEFAULT_ALL);
    }

    /**
     * 发送通知：
     *      小图标，标题，内容
     * @param context   上下文
     * @param ticker    摘要
     * @param title     标题
     * @param text      内容
     * @param icon      icon
     * @param msgId     消息ID
     * @param autoCancel 点击时取消
     * @param pendingIntent 跳转Action
     * @param defaults 额外的信息：振动/声音/灯光
     */
    public static void send(Context context,
                            String ticker,
                            String title,
                            String text,
                            @DrawableRes int icon,
                            int msgId,
                            boolean autoCancel,
                            PendingIntent pendingIntent,
                            int defaults) {
        NotificationManager manager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setTicker(ticker)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(autoCancel)
                .setSmallIcon(icon)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setDefaults(defaults)
                .setPriority(Notification.PRIORITY_HIGH);
        manager.notify(msgId,builder.build());
    }

    /**
     * 清除通知
     * @param context   上下文
     * @param msgId id
     */
    public static void cleanById(Context context, int msgId) {
        try {
            NotificationManager  mNotificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancel(msgId);
        } catch (Exception ignored) {}
    }
}
