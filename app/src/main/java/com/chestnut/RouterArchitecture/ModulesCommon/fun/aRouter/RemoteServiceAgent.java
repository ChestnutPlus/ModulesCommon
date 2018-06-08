package com.chestnut.RouterArchitecture.ModulesCommon.fun.aRouter;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chestnut.common.utils.LogUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/6/7 10:00
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
@Route(path = "/service/RemoteServiceAgent", name = "test-service")
public class RemoteServiceAgent implements RemoteService {

    private String TAG = "RemoteServiceAgent";

    @Override
    public void sayHello() {
        LogUtils.i(TAG,"sayHello");
    }

    @Override
    public void setCallback(Callback callback) {
        LogUtils.i(TAG,"setCallback");
        Observable.just(callback)
                .observeOn(Schedulers.io())
                .delay(5, TimeUnit.SECONDS)
                .map(callback1 -> {
                    callback1.onStart();
                    LogUtils.i(TAG,"setCallback, onStart");
                    return callback1;
                })
                .delay(5, TimeUnit.SECONDS)
                .subscribe(callback1 -> {
                    callback1.onStop();
                    LogUtils.i(TAG,"setCallback, onStop");
                }, throwable -> {
                    LogUtils.i(TAG,"setCallback, err, "+throwable.getMessage());
                });
    }

    @Override
    public void init(Context context) {
        LogUtils.i(TAG,"init");
    }
}
