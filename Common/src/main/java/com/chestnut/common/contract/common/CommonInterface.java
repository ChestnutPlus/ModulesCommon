package com.chestnut.common.contract.common;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/2/5 13:55
 *     desc  :  通用接口
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public interface CommonInterface {
    interface SuccessCallback<T> {
        void onSuccess(T t);
    }

    interface ErrorCallback<T> {
        void onError(T t);
    }
}
