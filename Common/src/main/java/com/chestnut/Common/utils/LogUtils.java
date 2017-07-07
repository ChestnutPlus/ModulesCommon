package com.chestnut.Common.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.chestnut.Common.utils.LogUtils.Config.FILE_SUFFIX;
import static com.chestnut.Common.utils.LogUtils.Config.LOG_FILE_NAME;
import static com.chestnut.Common.utils.LogUtils.Config.LOG_FILE_PATH;
import static com.chestnut.Common.utils.LogUtils.Config.LOG_FORMAT;
import static com.chestnut.Common.utils.LogUtils.Config.LOG_SWITCH;
import static com.chestnut.Common.utils.LogUtils.Config.LOG_TAG;
import static com.chestnut.Common.utils.LogUtils.Config.LOG_TO_FILE;
import static com.chestnut.Common.utils.LogUtils.Config.LOG_TYPE;

/**
 * <pre>
 *     author: Chestnut
 *     blog  :
 *     time  : 2016年10月17日22:09:30
 *     desc  :  日志输出：logcat 和 文件日志
 *     thanks To:
 *     dependent on:
 *     update：
 *          2017年1月8日20:02:25   栗子
 *              1. 修复 log 方法：String 为 null 的时候，会崩溃的 bug
 *              2. 同上：log2File
 *          2017年2月15日21:26:21  栗子
 *              1.  修改Log2File的默认存储位置。
 *              2.  增加 setLogFilePath
 * </pre>
 */
public class LogUtils {

    private LogUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static class Config {
        public static Boolean LOG_SWITCH = true;    //日志文件总开关
        public static Boolean LOG_TO_FILE = false;  //日志写入文件开关
        public static String LOG_TAG = "TAG";       //默认的tag
        public static char LOG_TYPE = 'v';          //输入日志类型，v代表输出所有信息,w则只输出警告...
        public final static SimpleDateFormat LOG_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //日志的输出格式
        public final static SimpleDateFormat FILE_SUFFIX = new SimpleDateFormat("yyyy-MM-dd");          //日志文件格式
        public static String LOG_FILE_PATH;         //日志文件保存路径
        public static String LOG_FILE_NAME;         //日志文件保存名称
    }

    public static void init(Context context) {      //在Application中初始化
        if (context.getExternalCacheDir()!=null) {
            LOG_FILE_PATH = context.getExternalCacheDir().getAbsolutePath();
        }
        else if (Environment.getExternalStorageDirectory()!=null){
            LOG_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        else {
            LOG_FILE_PATH = context.getCacheDir().getAbsolutePath();
        }
        LOG_FILE_NAME = "Log";
    }

    /****************************
     * Warn
     *********************************/
    public static void w(boolean isOpen, String tag, String msg) {
        if (isOpen)
            w(tag, msg, null);
    }

    public static void w(String msg) {
        w(LOG_TAG, msg);
    }

    public static void w(String tag, String msg) {
        w(tag, msg, null);
    }

    public static void w(String tag, String msg, Throwable tr) {
        log(tag, msg, tr, 'w');
    }

    public static void wD(String msg) {
        Thread thread = Thread.currentThread();
        StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(),LogUtils.class);
        log(msg,'w',thread,stackTraceElement);
    }

    public static void wToFile(String msg) {
        Thread thread = Thread.currentThread();
        StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
        log2File(thread,stackTraceElement,'W',LOG_TAG, msg);
    }

    public static void wToFile(String tag, String msg) {
        Thread thread = Thread.currentThread();
        StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
        log2File(thread,stackTraceElement,'W',tag, msg);
    }

    public static void W(boolean isOpen, String msg) {
        if (isOpen) {
            Thread thread = Thread.currentThread();
            StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
            String s = stackTraceElement.getClassName();
            log("["+s.substring(s.lastIndexOf(".") + 1) + "][" + stackTraceElement.getMethodName()+"]", msg, null, 'w');
        }
    }

    public static void W(String msg) {
        Thread thread = Thread.currentThread();
        StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
        String s = stackTraceElement.getClassName();
        log("["+s.substring(s.lastIndexOf(".") + 1) + "][" + stackTraceElement.getMethodName()+"]", msg, null, 'w');
    }

    /***************************
     * Error
     ********************************/
    public static void e(boolean isOpen, String tag, String msg) {
        if (isOpen)
            e(tag, msg, null);
    }

    public static void e(String msg) {
        e(LOG_TAG, msg);
    }

    public static void e(String tag, String msg) {
        e(tag, msg, null);
    }

    public static void e(String tag, String msg, Throwable tr) {
        log(tag, msg, tr, 'e');
    }

    public static void eD(String msg) {
        Thread thread = Thread.currentThread();
        StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(),LogUtils.class);
        log(msg,'e',thread,stackTraceElement);
    }

    public static void eD(String msg, Class xClass) {
        Thread thread = Thread.currentThread();
        StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(),xClass);
        log(msg,'e',thread,stackTraceElement);
    }

    public static void eToFile(String msg) {
        Thread thread = Thread.currentThread();
        StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
        log2File(thread,stackTraceElement,'E',LOG_TAG, msg);
    }

    public static void eToFile(String tag, String msg) {
        Thread thread = Thread.currentThread();
        StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
        log2File(thread,stackTraceElement,'E',tag, msg);
    }

    public static void E(boolean isOpen, String msg) {
        if (isOpen) {
            Thread thread = Thread.currentThread();
            StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
            String s = stackTraceElement.getClassName();
            log("["+s.substring(s.lastIndexOf(".") + 1) + "][" + stackTraceElement.getMethodName()+"]", msg, null, 'e');
        }
    }

    public static void E(String msg) {
        Thread thread = Thread.currentThread();
        StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
        String s = stackTraceElement.getClassName();
        log("["+s.substring(s.lastIndexOf(".") + 1) + "][" + stackTraceElement.getMethodName()+"]", msg, null, 'e');
    }

    /***************************
     * Debug
     ********************************/
    public static void d(boolean isOpen, String tag, String msg) {
        if (isOpen)
            d(tag, msg, null);
    }

    public static void d(String msg) {
        d(LOG_TAG, msg);
    }

    public static void d(String tag, String msg) {// 调试信息
        d(tag, msg, null);
    }

    public static void d(String tag, String msg, Throwable tr) {
        log(tag, msg, tr, 'd');
    }

    public static void dD(String msg) {
        Thread thread = Thread.currentThread();
        StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(),LogUtils.class);
        log(msg,'d',thread,stackTraceElement);
    }

    public static void dToFile(String msg) {
        Thread thread = Thread.currentThread();
        StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
        log2File(thread,stackTraceElement,'D',LOG_TAG, msg);
    }

    public static void dToFile(String tag, String msg) {
        Thread thread = Thread.currentThread();
        StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
        log2File(thread,stackTraceElement,'D',tag, msg);
    }

    public static void D(boolean isOpen, String msg) {
        if (isOpen) {
            Thread thread = Thread.currentThread();
            StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
            String s = stackTraceElement.getClassName();
            log("["+s.substring(s.lastIndexOf(".") + 1) + "][" + stackTraceElement.getMethodName()+"]", msg, null, 'd');
        }
    }

    public static void D(String msg) {
        Thread thread = Thread.currentThread();
        StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
        String s = stackTraceElement.getClassName();
        log("["+s.substring(s.lastIndexOf(".") + 1) + "][" + stackTraceElement.getMethodName()+"]", msg, null, 'd');
    }

    /****************************
     * Info
     *********************************/
    public static void i(boolean isOpen, String tag, String msg) {
        if (isOpen)
            i(tag, msg, null);
    }

    public static void i(String msg) {
        i(LOG_TAG, msg);
    }

    public static void i(String tag, String msg) {
        i(tag, msg, null);
    }

    public static void i(String tag, String msg, Throwable tr) {
        log(tag, msg, tr, 'i');
    }

    public static void iD(String msg) {
        Thread thread = Thread.currentThread();
        StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(),LogUtils.class);
        log(msg,'i',thread,stackTraceElement);
    }

    public static void iToFile(String msg) {
        Thread thread = Thread.currentThread();
        StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
        log2File(thread,stackTraceElement,'I',LOG_TAG, msg);
    }

    public static void iToFile(String tag, String msg) {
        Thread thread = Thread.currentThread();
        StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
        log2File(thread,stackTraceElement,'I',tag, msg);
    }

    public static void I(boolean isOpen, String msg) {
        if (isOpen) {
            Thread thread = Thread.currentThread();
            StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
            String s = stackTraceElement.getClassName();
            log("["+s.substring(s.lastIndexOf(".") + 1) + "][" + stackTraceElement.getMethodName()+"]", msg, null, 'i');
        }
    }

    public static void I(String msg) {
        Thread thread = Thread.currentThread();
        StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
        String s = stackTraceElement.getClassName();
        log("["+s.substring(s.lastIndexOf(".") + 1) + "][" + stackTraceElement.getMethodName()+"]", msg, null, 'i');
    }

    /**************************
     * Verbose
     ********************************/
    public static void v(boolean isOpen, String tag, String msg) {
        if (isOpen)
            v(tag, msg, null);
    }

    public static void v(String msg) {
        v(LOG_TAG, msg);
    }

    public static void v(String tag, String msg) {
        v(tag, msg, null);
    }

    public static void v(String tag, String msg, Throwable tr) {
        log(tag, msg, tr, 'v');
    }

    public static void vD(String msg) {
        Thread thread = Thread.currentThread();
        StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(),LogUtils.class);
        log(msg,'v',thread,stackTraceElement);
    }

    public static void vToFile(String msg) {
        Thread thread = Thread.currentThread();
        StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
        log2File(thread,stackTraceElement,'V',LOG_TAG, msg);
    }

    public static void vToFile(String tag, String msg) {
        Thread thread = Thread.currentThread();
        StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
        log2File(thread,stackTraceElement,'V',tag, msg);
    }

    public static void V(boolean isOpen, String msg) {
        if (isOpen) {
            Thread thread = Thread.currentThread();
            StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
            String s = stackTraceElement.getClassName();
            log("["+s.substring(s.lastIndexOf(".") + 1) + "][" + stackTraceElement.getMethodName()+"]", msg, null, 'v');
        }
    }

    public static void V(String msg) {
        Thread thread = Thread.currentThread();
        StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
        String s = stackTraceElement.getClassName();
        log("["+s.substring(s.lastIndexOf(".") + 1) + "][" + stackTraceElement.getMethodName()+"]", msg, null, 'v');
    }

    /**
     * 根据logLevel输出线程名，位置，和msg
     * @param msg
     * @param logLevel
     * @param thread
     * @param stackTraceElement
     */
    private static void log(String msg, char logLevel, Thread thread, StackTraceElement stackTraceElement) {
        if (msg==null)
            msg = "null";
        if (LOG_SWITCH) {
            String x = stackTraceElement.toString();
            int indexOf = x.indexOf("(");
            switch (logLevel) {
                case 'e':
                    Log.e(" [" + thread.getName() + "] " + x.substring(indexOf), msg);
                    break;
                case 'i':
                    Log.i(" [" + thread.getName() + "] " + x.substring(indexOf), msg);
                    break;
                case 'w':
                    Log.w(" [" + thread.getName() + "] " + x.substring(indexOf), msg);
                    break;
                case 'v':
                    Log.v(" [" + thread.getName() + "] " + x.substring(indexOf), msg);
                    break;
                case 'd':
                    Log.d(" [" + thread.getName() + "] " + x.substring(indexOf), msg);
                    break;
            }
        }
    }

    /**
     * 根据tag, msg和等级，输出日志
     *
     * @param tag
     * @param msg
     * @param level
     */
    private static void log(String tag, String msg, Throwable tr, char level) {
        if (tag==null)
            tag = "TAG";
        if (LOG_SWITCH) {
            if ('e' == level && ('e' == LOG_TYPE || 'v' == LOG_TYPE)) { // 输出错误信息
                Log.e(tag, msg, tr);
            } else if ('w' == level && ('w' == LOG_TYPE || 'v' == LOG_TYPE)) {
                Log.w(tag, msg, tr);
            } else if ('d' == level && ('d' == LOG_TYPE || 'v' == LOG_TYPE)) {
                Log.d(tag, msg, tr);
            } else if ('i' == level && ('d' == LOG_TYPE || 'v' == LOG_TYPE)) {
                Log.i(tag, msg, tr);
            } else {
                Log.v(tag, msg, tr);
            }
        }
    }

    /**
     * 打开日志文件并写入日志
     *
     * @return
     **/
    private synchronized static void log2File(
            Thread thread,
            StackTraceElement stackTraceElement,
            char level,
            String tag,
            String text) {
        if (!LOG_TO_FILE)
            return;
        if (text==null)
            text="null";
        if (tag==null)
            tag = "TAG";
        Date nowTime = new Date();
        String date = FILE_SUFFIX.format(nowTime);

        String x = stackTraceElement.toString();
        int indexOf = x.indexOf("(");
        String threadMsg = thread.getName() + " : " + x.substring(indexOf);

        String dateLogContent = LOG_FORMAT.format(nowTime) +
                " [" + level + "] [" + tag + "] \n\t\t" +
                threadMsg + "\n\t\t" +
                text + "\n"; // 日志输出格式

        File destDir = new File(LOG_FILE_PATH);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File file = new File(LOG_FILE_PATH, LOG_FILE_NAME + date);
        try {
            FileWriter filerWriter = new FileWriter(file, true);
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(dateLogContent);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        } catch (IOException e) {
            ExceptionCatchUtils.catchE(e,"LogUtils");
        }
    }

    private static StackTraceElement getCurrentStack(StackTraceElement[] trace, Class cla) {
        StackTraceElement e = null;
        for(int i = 0; i < trace.length; ++i) {
            e = trace[i];
            if(e.getClassName().equals(cla.getName())) {
                ++i;
                return trace[i];
            }
        }
        return e;
    }
}
