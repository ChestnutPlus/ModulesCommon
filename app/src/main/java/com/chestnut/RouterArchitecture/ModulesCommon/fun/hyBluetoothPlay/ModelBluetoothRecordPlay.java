package com.chestnut.RouterArchitecture.ModulesCommon.fun.hyBluetoothPlay;

import android.app.Activity;
import android.content.Intent;

import com.chestnut.RouterArchitecture.ModulesCommon.base.CommonContract;
import com.chestnut.RouterArchitecture.ModulesCommon.fun.hyMarket.DemoInstallUninstallActivity;
import com.chestnut.RouterArchitecture.ModulesCommon.view.recyclerView.SimpleAdapter;
import com.chestnut.RouterArchitecture.ModulesCommon.view.recyclerView.item.TxtItem;
import com.chestnut.common.ui.XToast;
import com.chestnut.common.utils.LogUtils;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/2/6 12:58
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class ModelBluetoothRecordPlay implements CommonContract{
    @Override
    public void onModelTest(SimpleAdapter simpleAdapter, XToast toast, String TAG, Activity activity) {
        TxtItem t1 = new TxtItem("BlueTooth录放");
        t1.setCallback(s -> {
            toast.setText(s).show();
            LogUtils.i(TAG,s);
            activity.startActivity(new Intent(activity,BluetoothRecordPlayActivity.class));
        });
        simpleAdapter.add(t1);
    }
}
