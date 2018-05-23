package com.chestnut.common.manager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.chestnut.common.utils.AppUtils;
import com.chestnut.common.utils.FileUtils;
import com.chestnut.common.utils.TimeUtils;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * <pre>
 *     author: Chestnut
 *     blog  :
 *     time  : 2016年10月7日
 *     desc  : 崩溃相关工具类
 *     thanks To :
 *          http://blog.csdn.net/hehe9737/article/details/7662123
 *          http://www.yiibai.com/java/lang/read_getdefaultuncaughtexceptionhandler.html
 *     dependent on:
 *           FileUtils
 *           TimeUtils
 *           AppUtils
 *     updateLog:
 *           2017年2月26日20:17:11     栗子
 *              1.  增加崩溃前的回调
 * </pre>
 */
public class CrashManager implements UncaughtExceptionHandler {

    private volatile static CrashManager mInstance;
    private Context mContext;
    private UncaughtExceptionHandler mHandler;
    private boolean mInitialized;

    private CrashManager() {

    }

    public interface CallBack {
        void catchCrash(String msg);
    }
    private CallBack callBack = null;

    /**
     * 获取单例
     *
     * @return 单例
     */
    public static CrashManager getInstance() {
        synchronized (CrashManager.class) {
            if (null == mInstance) {
                mInstance = new CrashManager();
            }
        }
        return mInstance;
    }

    /**
     * 初始化
     *
     * @param mContext 应用
     */
    public void init(Context mContext) {
        if (mInitialized) return;
        mInitialized = true;
        this.mContext = mContext.getApplicationContext();
        mHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     *  初始化
     *
     * @param mContext   应用
     * @param callBack  回掉
     */
    public void init(Context mContext,CallBack callBack) {
        this.callBack = callBack;
        init(mContext);
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable throwable) {
        String dir;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            try {
                dir = mContext.getExternalCacheDir().getPath();
            } catch (Exception e) {
                dir = mContext.getCacheDir().getPath();
            }
        } else {
            dir = mContext.getCacheDir().getPath();
        }
        String fullPath = dir + File.separator + "Crash_" + TimeUtils.getCurTimeString() + ".txt";
        if (!FileUtils.createOrExistsFile(fullPath)) return;
        StringBuilder sb = new StringBuilder();
        sb.append(getCrashHead());
        Writer writer = new StringWriter();
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(writer);
            throwable.printStackTrace(pw);
            Throwable cause = throwable.getCause();
            while (cause != null) {
                cause.printStackTrace(pw);
                cause = cause.getCause();
            }
        } finally {
            FileUtils.closeIO(pw);
        }
        sb.append(writer.toString());
        sb.append("\n************* Crash Log End ****************\n\n");
        Log.e("Unknown-Crash", sb.toString());
        FileUtils.writeFileFromString(fullPath, sb.toString(), false);
        boolean[] isTimeToExit = {false};
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "程序遇到问题，需要重启...", Toast.LENGTH_LONG).show();
                if (callBack != null)
                    callBack.catchCrash(sb.toString());
                isTimeToExit[0] = true;
                Looper.loop();
            }
        }.start();
        while (!isTimeToExit[0]);
        SystemClock.sleep(500);
        AppUtils.exitApp(mContext);
//        if (mHandler != null) {
//            mHandler.uncaughtException(thread, throwable);
//        }
    }

    /**
     * 获取崩溃头
     *
     * @return 崩溃头
     */
    private StringBuilder getCrashHead() {
        StringBuilder sb = new StringBuilder();
        try {
            PackageInfo pi = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            sb.append("\n************* Crash Log Head ****************");
            sb.append("\nDevice Manufacturer: ").append(Build.MANUFACTURER);// 设备厂商
            sb.append("\nDevice Model       : ").append(Build.MODEL);// 设备型号
            sb.append("\nAndroid Version    : ").append(Build.VERSION.RELEASE);// 系统版本
            sb.append("\nAndroid SDK        : ").append(Build.VERSION.SDK_INT);// SDK版本
            sb.append("\nApp VersionName    : ").append(pi.versionName);
            sb.append("\nApp VersionCode    : ").append(pi.versionCode);
            sb.append("\nCrash Des          : ").append("As follows\n");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return sb;
    }
}
