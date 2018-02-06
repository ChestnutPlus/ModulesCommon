package com.chestnut.common.contract.permission;

import io.reactivex.Observable;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/7/5 9:20
 *     desc  :  权限请求的接口规范
                @param <PermissionName>  权限名称
                @param <CommonVar> 每次调用方法时候需要传入的参数，例如上下文
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public interface PermissionInterface<PermissionName,CommonVar> {
    /**
     * rxAsk 请求权限
     * @param permissionName  权限的名称
     * @return  rx true or false
     */
    Observable<Boolean> rxAsk(CommonVar commonVar, PermissionName permissionName);
    Observable<Boolean> rxAsk(CommonVar commonVar, PermissionName... permissionName);
}
