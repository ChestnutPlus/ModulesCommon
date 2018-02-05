package com.chestnut.RouterArchitecture.ModulesCommon;

import android.app.Application;

import com.chestnut.common.helper.si.XFontHelper;
import com.chestnut.common.utils.LogUtils;
import com.chestnut.common.helper.si.XUtilsHelper;

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
                .loadWithSoftRef("fonts/fontzipMin.ttf")
                .loadWithStrongRef("fonts/caonima.ttf")
                .loadWithSoftRef("fonts/Test.TTF");
        LogUtils.i("TestBgService","CommonApplication:onCreate");
    }
}
