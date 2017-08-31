package com.chestnut.RouterArchitecture.ModulesCommon;

import android.app.Application;

import com.chestnut.Common.utils.UtilsManager;
import com.chestnut.Common.utils.XFontUtils;

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
        UtilsManager.init(this);
        XFontUtils.getInstance()
                .init(this)
                .load("fonts/fontzipMin.ttf")
                .load("fonts/caonima.ttf")
                .load("fonts/Test.TTF");
    }
}
