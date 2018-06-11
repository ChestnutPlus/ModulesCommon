package com.chestnut.RouterArchitecture.ModulesCommon.fun.soundMng;

import android.app.Activity;

import com.chestnut.RouterArchitecture.ModulesCommon.R;
import com.chestnut.RouterArchitecture.ModulesCommon.base.CommonContract;
import com.chestnut.RouterArchitecture.ModulesCommon.view.recyclerView.SimpleAdapter;
import com.chestnut.RouterArchitecture.ModulesCommon.view.recyclerView.item.TxtItem;
import com.chestnut.common.manager.SoundManager;
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
public class ModelSoundMng implements CommonContract{

    @Override
    public void onModelTest(SimpleAdapter simpleAdapter, XToast toast, String TAG, Activity activity) {
        TxtItem t2 = new TxtItem("ModelSoundMng");
        t2.setCallback(s -> {
            toast.setText(s).show();
            LogUtils.i(TAG, s);
            SoundManager.getInstance().stop(R.raw.letter_t);
        });
        simpleAdapter.add(t2);
    }
}
