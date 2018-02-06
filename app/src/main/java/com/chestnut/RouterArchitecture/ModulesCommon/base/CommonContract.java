package com.chestnut.RouterArchitecture.ModulesCommon.base;

import android.app.Activity;

import com.chestnut.RouterArchitecture.ModulesCommon.view.recyclerView.SimpleAdapter;
import com.chestnut.common.ui.XToast;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/2/5 23:24
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */

public interface CommonContract {
    void onModelTest(SimpleAdapter simpleAdapter, XToast toast, String TAG, Activity activity);
}
