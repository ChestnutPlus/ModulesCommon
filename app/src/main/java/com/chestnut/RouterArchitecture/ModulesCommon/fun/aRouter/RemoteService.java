package com.chestnut.RouterArchitecture.ModulesCommon.fun.aRouter;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/6/7 9:58
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public interface RemoteService extends IProvider{
    void sayHello();
    void setCallback(Callback callback);
    interface Callback{
        void onStart();
        void onStop();
    }
}
