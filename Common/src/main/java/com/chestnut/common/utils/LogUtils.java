package com.chestnut.common.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import com.chestnut.common.helper.si.XUtilsHelper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
 *          2017年12月6日16:09:07
 *              1.  修改API
 *              2.  增加 init 的时候去删除过期的日志，日志保留 7 天。
 * </pre>
 */
public class LogUtils {

    @SuppressLint("SimpleDateFormat")
    public final static SimpleDateFormat FILE_SUFFIX = new SimpleDateFormat("yyyy-MM-dd");          //日志文件格式
    @SuppressLint("SimpleDateFormat")
    public final static SimpleDateFormat LOG_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //日志的输出格式
    public static boolean OpenLogCat = true;

    private LogUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init() {
        new Thread(LogUtils::delFile).start();
    }

    /**
     * 删除制定的日志文件
     * */
    private static void delFile() {//删除日志文件
        try {
            long delTime = getDateBefore().getTime();
            File path = new File(XUtilsHelper.getCachePath());
            if(path.isDirectory()){
                //返回文件夹中有的数据
                File[] files = path.listFiles();
                //先判断下有没有权限，如果没有权限的话，就不执行了
                if(null == files)
                    return;
                for (File file : files) {
                    if (file.isFile() && file.lastModified() < delTime) {
                        file.delete();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到现在时间前的几天日期，用来得到需要删除的日志文件名
     * */
    private static Date getDateBefore() {
        Date nowtime = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(nowtime);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - 7); //保留7天日志。
        return now.getTime();
    }

    /**
     * Warning
     * @param isOpenLogcat    是否开启
     * @param isDetail  是否输出详细
     * @param tag       tag
     * @param msg       msg
     * @param isOpenWriteToFile 是否写入文件
     */
    public static void w(boolean isOpenLogcat, boolean isDetail, String tag, String msg, boolean isOpenWriteToFile) {
        if (isOpenLogcat) {
            if (isDetail) {
                Thread thread = Thread.currentThread();
                StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(),LogUtils.class);
                log(msg,'w',thread,stackTraceElement);
            }
            else
                log(tag, msg, 'w');
        }
        if (isOpenWriteToFile) {
            Thread thread = Thread.currentThread();
            StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
            log2File(thread,stackTraceElement,'w',tag, msg);
        }
    }

    public static void w(boolean isOpenLogcat, String tag, String msg, boolean isOpenWriteToFile) {
        w(isOpenLogcat, false, tag, msg, false);
        if (isOpenWriteToFile) {
            Thread thread = Thread.currentThread();
            StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
            log2File(thread,stackTraceElement,'w',tag, msg);
        }
    }

    public static void w(boolean isOpenLogcat, String tag, String msg) {
        w(isOpenLogcat, false, tag, msg, false);
    }

    public static void w(String tag, String msg) {
        w(true, false, tag, msg, false);
    }

    /**
     * Debug
     * @param isOpenLogcat    是否开启
     * @param isDetail  是否输出详细
     * @param tag       tag
     * @param msg       msg
     * @param isOpenWriteToFile 是否写入文件
     */
    public static void d(boolean isOpenLogcat, boolean isDetail, String tag, String msg, boolean isOpenWriteToFile) {
        if (isOpenLogcat) {
            if (isDetail) {
                Thread thread = Thread.currentThread();
                StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(),LogUtils.class);
                log(msg,'d',thread,stackTraceElement);
            }
            else
                log(tag, msg, 'd');
        }
        if (isOpenWriteToFile) {
            Thread thread = Thread.currentThread();
            StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
            log2File(thread,stackTraceElement,'d',tag, msg);
        }
    }

    public static void d(boolean isOpenLogcat, String tag, String msg, boolean isOpenWriteToFile) {
        d(isOpenLogcat, false, tag, msg, false);
        if (isOpenWriteToFile) {
            Thread thread = Thread.currentThread();
            StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
            log2File(thread,stackTraceElement,'d',tag, msg);
        }
    }

    public static void d(boolean isOpenLogcat, String tag, String msg) {
        d(isOpenLogcat, false, tag, msg, false);
    }

    public static void d(String tag, String msg) {
        d(true, false, tag, msg, false);
    }

    /**
     * Error
     * @param isOpenLogcat    是否开启
     * @param isDetail  是否输出详细
     * @param tag       tag
     * @param msg       msg
     * @param isOpenWriteToFile 是否写入文件
     */
    public static void e(boolean isOpenLogcat, boolean isDetail, String tag, String msg, boolean isOpenWriteToFile) {
        if (isOpenLogcat) {
            if (isDetail) {
                Thread thread = Thread.currentThread();
                StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(),LogUtils.class);
                log(msg,'e',thread,stackTraceElement);
            }
            else
                log(tag, msg, 'e');
        }
        if (isOpenWriteToFile) {
            Thread thread = Thread.currentThread();
            StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
            log2File(thread,stackTraceElement,'e',tag, msg);
        }
    }

    public static void e(boolean isOpenLogcat, String tag, String msg, boolean isOpenWriteToFile) {
        e(isOpenLogcat, false, tag, msg, false);
        if (isOpenWriteToFile) {
            Thread thread = Thread.currentThread();
            StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
            log2File(thread,stackTraceElement,'e',tag, msg);
        }
    }

    public static void e(boolean isOpenLogcat, String tag, String msg) {
        e(isOpenLogcat, false, tag, msg, false);
    }

    public static void e(String tag, String msg) {
        e(true, false, tag, msg, false);
    }

    /**
     * Info
     * @param isOpenLogcat    是否开启
     * @param isDetail  是否输出详细
     * @param tag       tag
     * @param msg       msg
     * @param isOpenWriteToFile 是否写入文件
     */
    public static void i(boolean isOpenLogcat, boolean isDetail, String tag, String msg, boolean isOpenWriteToFile) {
        if (isOpenLogcat) {
            if (isDetail) {
                Thread thread = Thread.currentThread();
                StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(),LogUtils.class);
                log(msg,'i',thread,stackTraceElement);
            }
            else
                log(tag, msg, 'i');
        }
        if (isOpenWriteToFile) {
            Thread thread = Thread.currentThread();
            StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
            log2File(thread,stackTraceElement,'i',tag, msg);
        }
    }

    public static void i(boolean isOpenLogcat, String tag, String msg, boolean isOpenWriteToFile) {
        i(isOpenLogcat, false, tag, msg, false);
        if (isOpenWriteToFile) {
            Thread thread = Thread.currentThread();
            StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
            log2File(thread,stackTraceElement,'i',tag, msg);
        }
    }

    public static void i(boolean isOpenLogcat, String tag, String msg) {
        i(isOpenLogcat, false, tag, msg, false);
    }

    public static void i(String tag, String msg) {
        i(true, false, tag, msg, false);
    }

    /**
     * Verbose
     * @param isOpenLogcat    是否开启
     * @param isDetail  是否输出详细
     * @param tag       tag
     * @param msg       msg
     * @param isOpenWriteToFile 是否写入文件
     */
    public static void v(boolean isOpenLogcat, boolean isDetail, String tag, String msg, boolean isOpenWriteToFile) {
        if (isOpenLogcat) {
            if (isDetail) {
                Thread thread = Thread.currentThread();
                StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(),LogUtils.class);
                log(msg,'v',thread,stackTraceElement);
            }
            else
                log(tag, msg, 'v');
        }
        if (isOpenWriteToFile) {
            Thread thread = Thread.currentThread();
            StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
            log2File(thread,stackTraceElement,'v',tag, msg);
        }
    }

    public static void v(boolean isOpenLogcat, String tag, String msg, boolean isOpenWriteToFile) {
        v(isOpenLogcat, false, tag, msg, false);
        if (isOpenWriteToFile) {
            Thread thread = Thread.currentThread();
            StackTraceElement stackTraceElement = getCurrentStack(thread.getStackTrace(), LogUtils.class);
            log2File(thread,stackTraceElement,'v',tag, msg);
        }
    }

    public static void v(boolean isOpenLogcat, String tag, String msg) {
        v(isOpenLogcat, false, tag, msg, false);
    }

    public static void v(String tag, String msg) {
        v(true, false, tag, msg, false);
    }

    /**
     * 根据logLevel输出线程名，位置，和msg
     * @param msg   msg
     * @param logLevel  level
     * @param thread    thread
     * @param stackTraceElement stackTraceElement
     */
    private static void log(String msg, char logLevel, Thread thread, StackTraceElement stackTraceElement) {
        if (!OpenLogCat)
            return;
        if (msg==null || msg.isEmpty())
            msg = "null";
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

    /**
     * 根据tag, msg和等级，输出日志
     *  @param tag   tag
     * @param msg   msg
     * @param level v.i.e.w.d
     */
    private static void log(String tag, String msg, char level) {
        if (!OpenLogCat)
            return;
        if (tag==null)
            tag = "TAG";
        if (msg==null || msg.isEmpty())
            msg = "null";
        switch (level) {
            case 'e':
                Log.e(tag, msg, null);
                break;
            case 'w':
                Log.w(tag, msg, null);
                break;
            case 'd':
                Log.d(tag, msg, null);
                break;
            case 'i':
                Log.i(tag, msg, null);
                break;
            default:
                Log.v(tag, msg, null);
                break;
        }
    }

    /**
     * 打开日志文件并写入日志
     **/
    private synchronized static void log2File(Thread thread, StackTraceElement stackTraceElement, char level, String tag, String text) {
        if (text==null)
            text="null";
        if (tag==null)
            tag = "TAG";
        Date nowTime = new Date();
        String date = FILE_SUFFIX.format(nowTime);

        String x = stackTraceElement.toString();
        String threadMsg = null;
        try {
            int indexOf = x.indexOf("(") + 1;
            threadMsg = thread.getName() + "-" + x.substring(indexOf,x.length()-1);
        } catch (Exception ignored) {}

        String dateLogContent = LOG_FORMAT.format(nowTime) + " [" + level + "] [" + threadMsg + "] [" + tag + "]\t" + text;

        File destDir = new File(XUtilsHelper.getCachePath());
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File file = new File(XUtilsHelper.getCachePath(), "log-" + date);
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
