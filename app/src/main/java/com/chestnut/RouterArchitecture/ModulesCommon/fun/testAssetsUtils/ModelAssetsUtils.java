package com.chestnut.RouterArchitecture.ModulesCommon.fun.testAssetsUtils;

import android.app.Activity;

import com.chestnut.RouterArchitecture.ModulesCommon.base.CommonContract;
import com.chestnut.RouterArchitecture.ModulesCommon.view.recyclerView.SimpleAdapter;
import com.chestnut.RouterArchitecture.ModulesCommon.view.recyclerView.item.TxtItem;
import com.chestnut.common.ui.XToast;
import com.chestnut.common.utils.AssetsUtils;
import com.chestnut.common.utils.EncryptUtils;
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
public class ModelAssetsUtils implements CommonContract{

    @Override
    public void onModelTest(SimpleAdapter simpleAdapter, XToast toast, String TAG, Activity activity) {
        TxtItem t2 = new TxtItem("AssetsUtils");
        t2.setCallback(s -> {
            toast.setText(s).show();
            LogUtils.i(TAG, s);
            //复制
            LogUtils.i(TAG, ":"+ AssetsUtils.copyTo(activity,"fonts/HKWaWaW5.ttf","/sdcard/temp.ttf"));
            //获取bytes字节流
            LogUtils.i(TAG,"md5:"+ EncryptUtils.encryptMD5ToString(AssetsUtils.getFileBytes(activity,"fonts/HKWaWaW5.ttf")));
            LogUtils.i(TAG,"md5:"+ EncryptUtils.encryptMD5File2String("/sdcard/temp.ttf"));
        });
        simpleAdapter.add(t2);
    }
}
