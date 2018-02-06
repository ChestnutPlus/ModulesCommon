package com.chestnut.common.helper.def;

import android.app.Activity;

import com.chestnut.common.contract.permission.PermissionInterface;
import com.chestnut.common.utils.DeviceUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observable;


/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/7/5 9:27
 *     desc  :  封装权限管理
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class PermissionHelper implements PermissionInterface<String,Activity>{

    public Observable<Boolean> rxAsk(Activity activity, String permission) {
        if (DeviceUtils.getSDK()>=23) {
            RxPermissions rxPermissions = new RxPermissions(activity);
            return rxPermissions.request(permission);
        }
        else
            return Observable.just(false).map(a -> {
                throw new RuntimeException("the sdk is lower than 23....");
            });
    }

    public Observable<Boolean> rxAsk(Activity activity, String... permissionName) {
        if (DeviceUtils.getSDK()>=23) {
            RxPermissions rxPermissions = new RxPermissions(activity);
            return rxPermissions.request(permissionName);
        }
        else
            return Observable.just(false).map(a -> {
                throw new RuntimeException("the sdk is lower than 23....");
            });
    }
}
