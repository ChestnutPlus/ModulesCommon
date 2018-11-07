package com.chestnut.RouterArchitecture.ModulesCommon.fun.recordTest;

import android.app.Activity;

import com.chestnut.RouterArchitecture.ModulesCommon.base.CommonContract;
import com.chestnut.RouterArchitecture.ModulesCommon.view.recyclerView.SimpleAdapter;
import com.chestnut.RouterArchitecture.ModulesCommon.view.recyclerView.item.TxtItem;
import com.chestnut.common.helper.RecorderHelper;
import com.chestnut.common.helper.RecorderListener;
import com.chestnut.common.ui.XToast;
import com.chestnut.common.utils.LogUtils;
import com.chestnut.common.utils.RxUtils;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/2/6 12:49
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class ModelRecordTest implements CommonContract{

    private RecorderHelper recorderHelper;

    @Override
    public void onModelTest(SimpleAdapter simpleAdapter, XToast toast, String TAG, Activity activity) {
        TxtItem t2 = new TxtItem("ModelRecordTest");
        t2.setCallback(s -> {
            recorderHelper = new RecorderHelper();
            recorderHelper.init("/sdcard/adb.mp3");
            recorderHelper.setMaxTimeAndNotifyLeftTime(30,10);
            recorderHelper.startRecord();
            recorderHelper.setCallBack(new RecorderListener() {
                @Override
                public void onRecordStart(String file) {
                    toast.setText("onRecordStart").show();
                    LogUtils.i("ModelRecordTest","onRecordStart,"+file);
                }

                @Override
                public void onRecordFail(String file, String msg) {
                    toast.setText("onRecordFail").show();
                    LogUtils.i("ModelRecordTest","onRecordFail,"+file+","+msg);
                }

                @Override
                public void onRecordEnd(String file, int duration) {
                    toast.setText("onRecordEnd").show();
                    LogUtils.i("ModelRecordTest","onRecordEnd,"+file+","+duration);
                }
            });
            RxUtils.countDown(60)
                    .subscribe(integer -> {},throwable -> {},()->{
                        recorderHelper.stopRecord();
                    });
        });
        simpleAdapter.add(t2);
    }
}
