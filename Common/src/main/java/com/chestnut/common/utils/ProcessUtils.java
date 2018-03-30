package com.chestnut.common.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/2/18 23:55
 *     desc  :  多进程工具类
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */

public class ProcessUtils {

    /**
     * 获取当前的进程号
     * @return int
     */
    public int getProcessPid() {
        return android.os.Process.myPid();
    }

    /**
     * 获取当前环境的进程名称
     * @param cxt 上下文
     * @return name
     */
    public static String getCurrentProcessName(Context cxt) {
        int pid = android.os.Process.myPid();
        return getProcessName(cxt,pid);
    }

    /**
     * 获取某个PID进程的进程名称
     * @param cxt   上下文
     * @param pid   进程号
     * @return  name
     */
    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
            List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
            if (runningApps == null) {
                return "";
            } else {
                for (ActivityManager.RunningAppProcessInfo p : runningApps) {
                    if (p.pid == pid) {
                        return p.processName;
                    }
                }
                return "";
            }
        } else
            return "";
    }
}
