package com.chestnut.common.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : 
 *     time  : 2016年10月7日16:17:54
 *     desc  : App相关工具类
 *     thanks To:
 *     dependent on:
 *          IntentUtils
 *          StringUtils
 *          FileUtils
 *          CleanUtils
 *          EmptyUtils
 *     update log:
 *          1.  2017年1月30日16:37:45  xxx by xxx
 * </pre>
 */
public class AppUtils {

    private AppUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断App是否安装
     *
     * @param context     上下文
     * @param packageName 包名
     * @return {@code true}: 已安装<br>{@code false}: 未安装
     */
    public static boolean isInstallApp(Context context, String packageName) {
        return !StringUtils.isSpace(packageName) && IntentUtils.getLaunchAppIntent(context, packageName) != null;
    }

    /**
     * 安装App(支持6.0)
     *
     * @param context  上下文
     * @param filePath 文件路径
     */
    public static void installApp(Context context, String filePath) {
        installApp(context, FileUtils.getFileByPath(filePath));
    }

    /**
     * 安装App(支持6.0)
     *
     * @param context 上下文
     * @param file    文件
     */
    public static void installApp(Context context, File file) {
        if (file == null) return;
        context.startActivity(IntentUtils.getInstallAppIntent(file));
    }

    /**
     * 安装App(支持6.0)
     *
     * @param activity    activity
     * @param filePath    文件路径
     * @param requestCode 请求值
     */
    public static void installApp(Activity activity, String filePath, int requestCode) {
        installApp(activity, com.chestnut.common.utils.FileUtils.getFileByPath(filePath), requestCode);
    }

    /**
     * 安装App(支持6.0)
     *
     * @param activity    activity
     * @param file        文件
     * @param requestCode 请求值
     */
    public static void installApp(Activity activity, File file, int requestCode) {
        if (file == null) return;
        activity.startActivityForResult(com.chestnut.common.utils.IntentUtils.getInstallAppIntent(file), requestCode);
    }

    /**
     * 7.0以上，安装app
     * @param context   上下文
     * @param fileProviderStr   provider str
     * @param file  file apk
     */
    public static void installAppOver7_0(Context context, String fileProviderStr, File file) {
        if (com.chestnut.common.utils.DeviceUtils.getSDK()>=24) {
            if (file != null && file.exists() && file.isFile()) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri apkUri = FileProvider.getUriForFile(context, fileProviderStr, file);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                context.startActivity(intent);
            }
        }
    }

    /**
     * 卸载App
     *
     * @param context     上下文
     * @param packageName 包名
     */
    public static void uninstallApp(Context context, String packageName) {
        if (com.chestnut.common.utils.StringUtils.isSpace(packageName)) return;
        context.startActivity(com.chestnut.common.utils.IntentUtils.getUninstallAppIntent(packageName));
    }

    /**
     * 卸载App
     *
     * @param activity    activity
     * @param packageName 包名
     * @param requestCode 请求值
     */
    public static void uninstallApp(Activity activity, String packageName, int requestCode) {
        if (com.chestnut.common.utils.StringUtils.isSpace(packageName)) return;
        activity.startActivityForResult(com.chestnut.common.utils.IntentUtils.getUninstallAppIntent(packageName), requestCode);
    }

    /**
     * 打开App
     *
     * @param context     上下文
     * @param packageName 包名
     */
    public static void launchApp(Context context, String packageName) {
        if (com.chestnut.common.utils.StringUtils.isSpace(packageName)) return;
        context.startActivity(com.chestnut.common.utils.IntentUtils.getLaunchAppIntent(context, packageName));
    }

    /**
     * 打开App
     *
     * @param activity    activity
     * @param packageName 包名
     * @param requestCode 请求值
     */
    public static void launchApp(Activity activity, String packageName, int requestCode) {
        if (com.chestnut.common.utils.StringUtils.isSpace(packageName)) return;
        activity.startActivityForResult(com.chestnut.common.utils.IntentUtils.getLaunchAppIntent(activity, packageName), requestCode);
    }

    /**
     * 获取App包名
     *
     * @param context 上下文
     * @return App包名
     */
    public static String getAppPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 获取App具体设置
     *
     * @param context 上下文
     */
    public static void getAppDetailsSettings(Context context) {
        getAppDetailsSettings(context, context.getPackageName());
    }

    /**
     * 获取App具体设置
     *
     * @param context     上下文
     * @param packageName 包名
     */
    public static void getAppDetailsSettings(Context context, String packageName) {
        if (com.chestnut.common.utils.StringUtils.isSpace(packageName)) return;
        context.startActivity(com.chestnut.common.utils.IntentUtils.getAppDetailsSettingsIntent(packageName));
    }

    /**
     * 获取App名称
     *
     * @param context 上下文
     * @return App名称
     */
    public static String getAppName(Context context) {
        return getAppName(context, context.getPackageName());
    }

    /**
     * 获取App名称
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App名称
     */
    public static String getAppName(Context context, String packageName) {
        if (com.chestnut.common.utils.StringUtils.isSpace(packageName)) return null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            com.chestnut.common.utils.ExceptionCatchUtils.catchE(e,"AppUtils");
            return null;
        }
    }

    /**
     * 获取App图标
     *
     * @param context 上下文
     * @return App图标
     */
    public static Drawable getAppIcon(Context context) {
        return getAppIcon(context, context.getPackageName());
    }

    /**
     * 获取App图标
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App图标
     */
    public static Drawable getAppIcon(Context context, String packageName) {
        if (com.chestnut.common.utils.StringUtils.isSpace(packageName)) return null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            com.chestnut.common.utils.ExceptionCatchUtils.catchE(e,"AppUtils");
            return null;
        }
    }

    /**
     * 获取App路径
     *
     * @param context 上下文
     * @return App路径
     */
    public static String getAppPath(Context context) {
        return getAppPath(context, context.getPackageName());
    }

    /**
     * 获取App路径
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App路径
     */
    public static String getAppPath(Context context, String packageName) {
        if (com.chestnut.common.utils.StringUtils.isSpace(packageName)) return null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            com.chestnut.common.utils.ExceptionCatchUtils.catchE(e,"AppUtils");
            return null;
        }
    }

    /**
     * 获取App版本号
     *
     * @param context 上下文
     * @return App版本号
     */
    public static String getAppVersionName(Context context) {
        return getAppVersionName(context, context.getPackageName());
    }

    /**
     * 获取App版本号
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App版本号
     */
    public static String getAppVersionName(Context context, String packageName) {
        if (com.chestnut.common.utils.StringUtils.isSpace(packageName)) return null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            com.chestnut.common.utils.ExceptionCatchUtils.catchE(e,"AppUtils");
            return null;
        }
    }

    /**
     * 获取App版本码
     *
     * @param context 上下文
     * @return App版本码
     */
    public static int getAppVersionCode(Context context) {
        return getAppVersionCode(context, context.getPackageName());
    }

    /**
     * 获取App版本码
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App版本码
     */
    public static int getAppVersionCode(Context context, String packageName) {
        if (com.chestnut.common.utils.StringUtils.isSpace(packageName)) return -1;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            com.chestnut.common.utils.ExceptionCatchUtils.catchE(e,"AppUtils");
            return -1;
        }
    }

    /**
     * 获取App签名
     *
     * @param context 上下文
     * @return App签名
     */
    public static Signature[] getAppSignature(Context context) {
        return getAppSignature(context, context.getPackageName());
    }

    /**
     * 获取App签名
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App签名
     */
    public static Signature[] getAppSignature(Context context, String packageName) {
        if (com.chestnut.common.utils.StringUtils.isSpace(packageName)) return null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.signatures;
        } catch (PackageManager.NameNotFoundException e) {
            com.chestnut.common.utils.ExceptionCatchUtils.catchE(e,"AppUtils");
            return null;
        }
    }

    /**
     * 判断App是否是系统应用
     *
     * @param context 上下文
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isSystemApp(Context context) {
        return isSystemApp(context, context.getPackageName());
    }

    /**
     * 判断App是否是系统应用
     *
     * @param context     上下文
     * @param packageName 包名
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isSystemApp(Context context, String packageName) {
        if (com.chestnut.common.utils.StringUtils.isSpace(packageName)) return false;
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            return ai != null && (ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            com.chestnut.common.utils.ExceptionCatchUtils.catchE(e,"AppUtils");
        }
        return false;
    }

    /**
     * 判断App是否处于前台
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.GET_TASKS"/>}</p>
     * <p>并且必须是系统应用该方法才有效</p>
     *
     * @param context 上下文
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isAppForeground(Context context) {
        return isAppForeground(context, context.getPackageName());
    }

    /**
     * 判断App是否处于前台
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.GET_TASKS"/>}</p>
     * <p>并且必须是系统应用该方法才有效</p>
     *
     * @param context 上下文
     * @param packageName 包名
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isAppForeground(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        return !tasks.isEmpty() && tasks.get(0).topActivity.getPackageName().equals(packageName);
    }

    /**
     * 封装App信息的Bean类
     */
    public static class AppInfo {

        private String name;
        private Drawable icon;
        private String packageName;
        private String packagePath;
        private String versionName;
        private int versionCode;
        private boolean isSystem;

        public Drawable getIcon() {
            return icon;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }

        public boolean isSystem() {
            return isSystem;
        }

        public void setSystem(boolean isSystem) {
            this.isSystem = isSystem;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packagName) {
            this.packageName = packagName;
        }

        public String getPackagePath() {
            return packagePath;
        }

        public void setPackagePath(String packagePath) {
            this.packagePath = packagePath;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        /**
         * @param name        名称
         * @param icon        图标
         * @param packageName 包名
         * @param packagePath 包路径
         * @param versionName 版本号
         * @param versionCode 版本Code
         * @param isSystem    是否系统应用
         */
        public AppInfo(String name, Drawable icon, String packageName, String packagePath,
                       String versionName, int versionCode, boolean isSystem) {
            this.setName(name);
            this.setIcon(icon);
            this.setPackageName(packageName);
            this.setPackagePath(packagePath);
            this.setVersionName(versionName);
            this.setVersionCode(versionCode);
            this.setSystem(isSystem);
        }
    }

    /**
     * 获取App信息
     * <p>AppInfo（名称，图标，包名，版本号，版本Code，是否系统应用）</p>
     *
     * @param context 上下文
     * @return 当前应用的AppInfo
     */
    public static AppInfo getAppInfo(Context context) {
        return getAppInfo(context, context.getPackageName());
    }

    /**
     * 获取App信息
     * <p>AppInfo（名称，图标，包名，版本号，版本Code，是否系统应用）</p>
     *
     * @param context 上下文
     * @param packageName 包名
     * @return 当前应用的AppInfo
     */
    public static AppInfo getAppInfo(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return getBean(pm, pi);
        } catch (PackageManager.NameNotFoundException e) {
            com.chestnut.common.utils.ExceptionCatchUtils.catchE(e,"AppUtils");
            return null;
        }
    }

    /**
     * 得到AppInfo的Bean
     *
     * @param pm 包的管理
     * @param pi 包的信息
     * @return AppInfo类
     */
    private static AppInfo getBean(PackageManager pm, PackageInfo pi) {
        if (pm == null || pi == null) return null;
        ApplicationInfo ai = pi.applicationInfo;
        String name = ai.loadLabel(pm).toString();
        Drawable icon = ai.loadIcon(pm);
        String packageName = pi.packageName;
        String packagePath = ai.sourceDir;
        String versionName = pi.versionName;
        int versionCode = pi.versionCode;
        boolean isSystem = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != 0;
        return new AppInfo(name, icon, packageName, packagePath, versionName, versionCode, isSystem);
    }

    /**
     * 获取所有已安装App信息
     * <p>{@link #getBean(PackageManager, PackageInfo)}（名称，图标，包名，包路径，版本号，版本Code，是否系统应用）</p>
     * <p>依赖上面的getBean方法</p>
     *
     * @param context 上下文
     * @return 所有已安装的AppInfo列表
     */
    public static List<AppInfo> getAppsInfo(Context context) {
        List<AppInfo> list = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        // 获取系统中安装的所有软件信息
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        for (PackageInfo pi : installedPackages) {
            AppInfo ai = getBean(pm, pi);
            if (ai == null) continue;
            list.add(ai);
        }
        return list;
    }

    /**
     * 清除App所有数据
     *
     * @param context  上下文
     * @param dirPaths 目录路径
     * @return 是否成功
     */
    public static boolean cleanAppData(Context context, String... dirPaths) {
        File[] dirs = new File[dirPaths.length];
        int i = 0;
        for (String dirPath : dirPaths) {
            dirs[i++] = new File(dirPath);
        }
        return cleanAppData(context, dirs);
    }

    /**
     * 清除App所有数据
     *
     * @param context 上下文
     * @param dirs    目录
     * @return 是否成功
     */
    public static boolean cleanAppData(Context context, File... dirs) {
        boolean isSuccess = com.chestnut.common.utils.CleanUtils.cleanInternalCache(context);
        isSuccess &= com.chestnut.common.utils.CleanUtils.cleanInternalDbs(context);
        isSuccess &= com.chestnut.common.utils.CleanUtils.cleanInternalSP(context);
        isSuccess &= com.chestnut.common.utils.CleanUtils.cleanInternalFiles(context);
        isSuccess &= com.chestnut.common.utils.CleanUtils.cleanExternalCache(context);
        for (File dir : dirs) {
            isSuccess &= com.chestnut.common.utils.CleanUtils.cleanCustomCache(dir);
        }
        return isSuccess;
    }

    /**
     *  退出app
     *      如果没有效果，请加入以下的权限：
     *          uses-permission android:name="android.permission.RESTART_PACKAGES"
     *          uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES"
     * @param context 上下文
     */
    public static void exitApp(Context context, boolean isBackToLaunch) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
            if (isBackToLaunch) {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(startMain);
            }
            System.exit(0);
        }  else {// android2.1
            ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            am.restartPackage(context.getPackageName());
        }
    }

    public static void exitApp(Context context) {
        exitApp(context,true);
    }

    /**
     *      按两下退出app，要在 onBackPressed() 中调用
     */
    public interface ExitAppCallBack {
        void firstAsk();
        void beginExit();
    }
    private static long exitTime = 0;
    public static void pressTwiceExitApp(Activity activity, boolean isBackToLaunch, long exitTimeMsSpace, ExitAppCallBack exitAppCallBack) {
        if ((System.currentTimeMillis()-exitTime)>exitTimeMsSpace) {
            exitTime = System.currentTimeMillis();
            if (exitAppCallBack!=null)
                exitAppCallBack.firstAsk();
        } else {
            if (exitAppCallBack!=null)
                exitAppCallBack.beginExit();
            activity.finish();
            exitApp(activity,isBackToLaunch);
        }
    }

    public static void pressTwiceExitApp(Activity activity, String msg, long exitTimeMsSpace, ExitAppCallBack exitAppCallBack) {
        pressTwiceExitApp(activity,true,exitTimeMsSpace,exitAppCallBack);
    }

    /**
     *      得到本程序的 MD5,小写。
     * @param context 传入上下文
     * @param isUpper true:大写，false：小写。
     * @return  返回的是类似于：7a020deacb854eb8c1990fc2776978d8 格式的MD5
     */
    public static String getThisAppMd5(Context context,boolean isUpper) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature signature = pi.signatures[0];
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(signature.toByteArray());
            byte[] digest = md.digest();
            return isUpper ? com.chestnut.common.utils.ConvertUtils.bytes2HexString(digest) : com.chestnut.common.utils.StringUtils.changeTOLowerCase(com.chestnut.common.utils.ConvertUtils.bytes2HexString(digest));
        } catch (PackageManager.NameNotFoundException e) {
            com.chestnut.common.utils.ExceptionCatchUtils.catchE(e,"AppUtils");
            return "null";
        } catch (NoSuchAlgorithmException e) {
            com.chestnut.common.utils.ExceptionCatchUtils.catchE(e,"AppUtils");
            return "null";
        }
    }

    /**
     * 判断当前应用是否是debug状态
     */
    public static boolean isInDebug(Context context) {
        boolean result = false;
        try {
            ApplicationInfo info = context.getApplicationInfo();
            result = (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("AppUtils", "isInDebug, " + result);
        return result;
    }
}
