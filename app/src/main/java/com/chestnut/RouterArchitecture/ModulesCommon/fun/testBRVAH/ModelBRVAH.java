package com.chestnut.RouterArchitecture.ModulesCommon.fun.testBRVAH;

import android.app.Activity;
import android.content.Intent;

import com.chestnut.RouterArchitecture.ModulesCommon.base.CommonContract;
import com.chestnut.RouterArchitecture.ModulesCommon.view.recyclerView.SimpleAdapter;
import com.chestnut.RouterArchitecture.ModulesCommon.view.recyclerView.item.TxtItem;
import com.chestnut.common.ui.XToast;
import com.chestnut.common.utils.LogUtils;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/2/6 16:28
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class ModelBRVAH implements CommonContract{

    @Override
    public void onModelTest(SimpleAdapter simpleAdapter, XToast toast, String TAG, Activity activity) {
        TxtItem t2 = new TxtItem("BaseRecyclerViewAdapterHelper");
        t2.setCallback(s -> {
            toast.setText(s).show();
            LogUtils.i(TAG, s);
            activity.startActivity(new Intent(activity,BRVAHActivity.class));
        });
        simpleAdapter.add(t2);
    }
}
