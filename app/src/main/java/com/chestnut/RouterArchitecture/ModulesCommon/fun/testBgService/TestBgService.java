package com.chestnut.RouterArchitecture.ModulesCommon.fun.testBgService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.chestnut.common.utils.LogUtils;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/12/26 14:57
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class TestBgService extends Service{

    private String TAG = "TestBgService";

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i(TAG,"onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.i(TAG,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.i(TAG,"onBind");
        return null;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        LogUtils.i(TAG,"onTrimMemory:内存不足,即将被回收");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i(TAG,"onDestroy");
    }
}
