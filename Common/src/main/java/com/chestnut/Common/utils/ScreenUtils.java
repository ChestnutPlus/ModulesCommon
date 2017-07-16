package com.chestnut.Common.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.PowerManager;
import android.support.annotation.IntDef;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.chestnut.Common.utils.ConvertUtils.px2dp;


/**
 * <pre>
 *     author: Chestnut
 *     blog  :
 *     time  : 2016年10月18日21:37:50
 *     desc  : 屏幕相关工具类
 *     thanks To:
 *     dependent on:
 *          BarUtils
 *          ConvertUtils
 * </pre>
 */
public class ScreenUtils {

    private ScreenUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 获取屏幕的宽度px
     *
     * @param context 上下文
     * @return 屏幕宽px
     */
    public static int getScreenWidth_PX(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕的高度px
     *
     * @param context 上下文
     * @return 屏幕高px
     */
    public static int getScreenHeight_PX(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获取屏幕的宽度dp
     *
     * @param context 上下文
     * @return 屏幕宽dp
     */
    public static int getScreenWidth_Dip(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return px2dp(context,dm.widthPixels);
    }

    /**
     * 获取屏幕的高度dp
     *
     * @param context 上下文
     * @return 屏幕高dp
     */
    public static int getScreenHeight_Dip(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return px2dp(context,dm.heightPixels);
    }

    /**
     * 设置屏幕为横屏
     * <p>还有一种就是在Activity中加属性android:screenOrientation="landscape"</p>
     * <p>不设置Activity的android:configChanges时，切屏会重新调用各个生命周期，切横屏时会执行一次，切竖屏时会执行两次</p>
     * <p>设置Activity的android:configChanges="orientation"时，切屏还是会重新调用各个生命周期，切横、竖屏时只会执行一次</p>
     * <p>设置Activity的android:configChanges="orientation|keyboardHidden|screenSize"（4.0以上必须带最后一个参数）时
     * 切屏不会重新调用各个生命周期，只会执行onConfigurationChanged方法</p>
     *
     * @param activity activity
     */
    public static void setLandscape(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * 设置屏幕为竖屏
     *
     * @param activity activity
     */
    public static void setPortrait(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 判断是否横屏
     *
     * @param context 上下文
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 判断是否竖屏
     *
     * @param context 上下文
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 获取屏幕旋转角度
     *
     * @param activity activity
     * @return 屏幕旋转角度
     */
    public static int getScreenRotation(Activity activity) {
        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
            default:
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity activity
     * @return Bitmap
     */
    public static Bitmap captureWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Bitmap ret = Bitmap.createBitmap(bmp, 0, 0, dm.widthPixels, dm.heightPixels);
        view.destroyDrawingCache();
        return ret;
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity activity
     * @return Bitmap
     */
    public static Bitmap captureWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int statusBarHeight = BarUtils.getStatusBarHeight(activity);
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Bitmap ret = Bitmap.createBitmap(bmp, 0, statusBarHeight, dm.widthPixels, dm.heightPixels - statusBarHeight);
        view.destroyDrawingCache();
        return ret;
    }

    /**
     * 判断是否锁屏
     *
     * @param context 上下文
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isScreenLock(Context context) {
        KeyguardManager km = (KeyguardManager) context
                .getSystemService(Context.KEYGUARD_SERVICE);
        return km.inKeyguardRestrictedInputMode();
    }

    /**
     * 内部静态类，用于监听用户的锁屏，开屏，Home键等等。
     * 最后记得unregisterListener去除，否则造成内存泄漏。
     */
    public static class ScreenListener {
        private Context mContext;
        private ScreenBroadcastReceiver mScreenReceiver;
        private ScreenStateListener mScreenStateListener;

        public ScreenListener(Context context) {
            mContext = context;
            mScreenReceiver = new ScreenBroadcastReceiver();
        }

        /**
         * screen状态广播接收者
         */
        private class ScreenBroadcastReceiver extends BroadcastReceiver {
            private String action = null;

            final String SYSTEM_DIALOG_REASON_KEY = "reason";
            final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
            final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

            @Override
            public void onReceive(Context context, Intent intent) {
                action = intent.getAction();
                if (Intent.ACTION_SCREEN_ON.equals(action)) { // 开屏
                    mScreenStateListener.onScreenOn();
                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
                    mScreenStateListener.onScreenOff();
                } else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁
                    mScreenStateListener.onUserPresent();
                } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) { //home键
                    String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                    if (reason != null) {
                        if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                            // 短按home键
                            mScreenStateListener.onHomePress();
                        } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                            // 长按home键
                        }
                    }
                }
            }
        }

        /**
         * 开始监听screen状态
         *
         * @param listener
         */
        public void begin(ScreenStateListener listener) {
            mScreenStateListener = listener;
            registerListener();
            getScreenState();
        }

        /**
         * 获取screen状态
         */
        private void getScreenState() {
            PowerManager manager = (PowerManager) mContext
                    .getSystemService(Context.POWER_SERVICE);
            if (manager.isScreenOn()) {
                if (mScreenStateListener != null) {
                    mScreenStateListener.onScreenOn();
                }
            } else {
                if (mScreenStateListener != null) {
                    mScreenStateListener.onScreenOff();
                }
            }
        }

        /**
         * 停止screen状态监听
         */
        public void unregisterListener() {
            mContext.unregisterReceiver(mScreenReceiver);
        }

        /**
         * 启动screen状态广播接收器
         */
        private void registerListener() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_USER_PRESENT);
            filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            mContext.registerReceiver(mScreenReceiver, filter);
        }

        public interface ScreenStateListener {// 返回给调用者屏幕状态信息
            void onScreenOn();
            void onScreenOff();
            void onUserPresent();
            void onHomePress();
        }
    }

    //新增监听的方法跟接口，简化上面的调用
    public interface Callback {
        void onScreenOn();
        void onScreenOff();
        void onUserUnlockThePhone();
        void onHomeKeyShortPress();
        void onHomeKeyLongPress();
    }

    public static void setListenerScreenLock(Context context, Callback callback) {
        if (callback!=null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_USER_PRESENT);
            filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

            final String SYSTEM_DIALOG_REASON_KEY = "reason";
            final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
            final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    switch (intent.getAction()) {
                        // 开屏
                        case Intent.ACTION_SCREEN_ON:
                            callback.onScreenOn();
                            break;
                        // 锁屏
                        case Intent.ACTION_SCREEN_OFF:
                            callback.onScreenOff();
                            break;
                        // 解锁
                        case Intent.ACTION_USER_PRESENT:
                            callback.onUserUnlockThePhone();
                            break;
                        //  Home键
                        case Intent.ACTION_CLOSE_SYSTEM_DIALOGS:
                            String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                            if (reason != null) {
                                if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                                    // 短按home键
                                    callback.onHomeKeyShortPress();
                                } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                                    // 长按home键
                                    callback.onHomeKeyLongPress();
                                }
                            }
                            break;
                    }
                }
            };
            context.getApplicationContext().registerReceiver(broadcastReceiver,filter);
        }
    }

    private static BroadcastReceiver broadcastReceiver = null;

    public static void removeListenerScreenLock(Context context) {
        if (broadcastReceiver!=null)
            context.getApplicationContext().unregisterReceiver(broadcastReceiver);
    }

    //新增监听实体按键的方法
    //  Thanks To: http://www.tuicool.com/articles/aI36Fz
    public static final int KEYCODE_POWER     = -1;
    public static final int KEYCODE_MENU      = -2;
    public static final int KEYCODE_BACK       = -3;
    public static final int KEYCODE_HOME       = -4;
    public static final int KEYCODE_CAMERA   = -5;
    public static final int KEYCODE_SEARCH   = -6;
    public static final int KEYCODE_VOLUME_UP   = -7;
    public static final int KEYCODE_VOLUME_DOWN    = -8;
    public static final int KEYCODE_VOLUME_MUTE     = -9;
    @IntDef({KEYCODE_POWER,KEYCODE_MENU,KEYCODE_BACK,KEYCODE_HOME,
            KEYCODE_CAMERA,KEYCODE_SEARCH,KEYCODE_VOLUME_UP,
            KEYCODE_VOLUME_DOWN,KEYCODE_VOLUME_MUTE
    })
    @Retention(RetentionPolicy.SOURCE)
    private @interface KEY_NAME {}

    public interface KeyCallback {
        void onPowerDown(int keyCode);
        void onMenuDown(int keyCode);
        void onBackDown(int keyCode);
        void onHomeDown(int keyCode);
        void onCameraDown(int keyCode);
        void onSearchDown(int keyCode);
        void onVoiceUpDown(int keyCode);
        void onVoiceDownDown(int keyCode);
        void onVoiceMuteDown(int keyCode);
    }

    /**
     * 监听Key的点击，需要在Activity中，
     *  在 onKeyDown 中调用
     *
     *   public boolean onKeyDown(int keyCode, KeyEvent event) {
     *       ScreenUtils.setKeyDownListener(xx,xx,xxx);
     *       return super.onKeyDown(keyCode, event);
     *   }
     *
     * @param keyCode   key
     * @param keyCallback   callback
     */
    public static void setKeyDownListener(int keyCode, KeyCallback keyCallback) {
        if (keyCallback!=null) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_POWER:
                    keyCallback.onPowerDown(keyCode);
                    break;
                case KeyEvent.KEYCODE_MENU:
                    keyCallback.onMenuDown(keyCode);
                    break;
                case KeyEvent.KEYCODE_BACK:
                    keyCallback.onBackDown(keyCode);
                    break;
                case KeyEvent.KEYCODE_HOME:
                    keyCallback.onHomeDown(keyCode);
                    break;
                case KeyEvent.KEYCODE_CAMERA:
                    keyCallback.onCameraDown(keyCode);
                    break;
                case KeyEvent.KEYCODE_SEARCH:
                    keyCallback.onSearchDown(keyCode);
                    break;
                case KeyEvent.KEYCODE_VOLUME_UP:
                    keyCallback.onVoiceUpDown(keyCode);
                    break;
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    keyCallback.onVoiceDownDown(keyCode);
                    break;
                case KeyEvent.KEYCODE_VOLUME_MUTE:
                    keyCallback.onVoiceMuteDown(keyCode);
                    break;
            }
        }
    }
}





