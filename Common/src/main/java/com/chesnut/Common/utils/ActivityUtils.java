package com.chesnut.Common.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : 
 *     time  : 2016年10月7日16:54:15
 *     desc  : Activity相关工具类
 *     thanks To:
 *     dependent on:
 *          IntentUtils
 * </pre>
 */
public class ActivityUtils {

    private ActivityUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断是否存在Activity
     *
     * @param context     上下文
     * @param packageName 包名
     * @param className   activity全路径类名
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isExistActivity(Context context, String packageName, String className) {
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        return !(context.getPackageManager().resolveActivity(intent, 0) == null ||
                intent.resolveActivity(context.getPackageManager()) == null ||
                context.getPackageManager().queryIntentActivities(intent, 0).size() == 0);
    }

    /**
     * 打开Activity
     *
     * @param context     上下文
     * @param packageName 包名
     * @param className   全类名
     */
    public static void launchActivity(Context context, String packageName, String className) {
        launchActivity(context, packageName, className, null);
    }

    /**
     * 打开Activity
     *
     * @param context     上下文
     * @param packageName 包名
     * @param className   全类名
     * @param bundle      bundle
     */
    public static void launchActivity(Context context, String packageName, String className, Bundle bundle) {
        context.startActivity(IntentUtils.getComponentIntent(packageName, className, bundle));
    }

    /**
     * 得到The top Task
     *      或许需要：
     *          uses-permission android:name="android.permission.GET_TASKS"
     * @param mContext 上下文
     * @return  ActivityManager.RunningTaskInfo
     */
    public static ActivityManager.RunningTaskInfo getTopTask(Context mContext) {
        ActivityManager mActivityManager;
        mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = mActivityManager.getRunningTasks(1);
        if (tasks != null && !tasks.isEmpty()) {
            return tasks.get(0);
        }
        return null;
    }

    /**
     *      判断 activity 是否在前台。
     * @param activity  活动
     * @return  boolean
     */
    public static boolean isTopActivity(Activity activity) {
        ActivityManager.RunningTaskInfo topTask = getTopTask(activity.getApplicationContext());
        if (topTask != null) {
            ComponentName topActivity = topTask.topActivity;
            if (topActivity.getClassName().equals(activity.getPackageName()+"."+activity.getLocalClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     *      得到 Activity 的名称，非全限定包名
     * @param context 上下文
     * @return String
     */
    public static String getActivityName(Context context) {
        String contextString = context.toString();
        return contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));
    }

    /**
     *      从程序开启，是否是第一次进入此Activity
     * @param activity
     * @return
     */
    private static List<String> activitys = new ArrayList<>();
    public static boolean isFirstEnterThisActivity(Activity activity) {
        String name = getActivityName(activity);
        if (activitys.contains(name)) {
            return false;
        }
        activitys.add(name);
        return true;
    }

}
