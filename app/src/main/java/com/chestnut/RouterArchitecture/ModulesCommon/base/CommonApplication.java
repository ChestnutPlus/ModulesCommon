package com.chestnut.RouterArchitecture.ModulesCommon.base;

import android.app.Application;

import com.chestnut.common.helper.si.XFontHelper;
import com.chestnut.common.helper.si.XUtilsHelper;
import com.chestnut.common.utils.LogUtils;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/7/6 10:50
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class CommonApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        XUtilsHelper.init(this);
        XFontHelper.getInstance()
                .init(this)
                .loadWithSoftRef(ViewConfig.TypeFace_Zip_Min)
                .loadWithSoftRef(ViewConfig.TypeFace_HK)
                .loadWithStrongRef(ViewConfig.TypeFace_Cao_Ni_Ma)
                .loadWithSoftRef(ViewConfig.TypeFace_TEST);
        LogUtils.i("TestBgService","CommonApplication:onCreate");
    }
}
