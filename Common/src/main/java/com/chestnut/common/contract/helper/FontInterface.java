package com.chestnut.common.contract.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/2/5 0:27
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */

public interface FontInterface<This> {

    /**
     * 初始化
     * @param context 上下文
     * @return this
     */
    This init(Context context);

    /**
     * 加载字体
     *  区分以强/软方式
     * @param fontPathInAssets  字体路径：fonts/Test.TTF"，字体应该放置于asset目录下
     * @return this
     */
    This loadWithStrongRef(String fontPathInAssets);
    This loadWithSoftRef(String fontPathInAssets);

    /**
     * 获取字体
     * 先从强引用列表中获取，若找不到，则从软引用中找，
     * 如果都找不到，则加载到软引用列表中。
     * @param fontPathInAssets 字体路径：fonts/Test.TTF"，字体应该放置于asset目录下
     * @return this
     */
    Typeface get(String fontPathInAssets);

    /**
     * 为Activity设置字体
     * @param activity   activity
     * @param fontPathInAssets 字体路径：fonts/Test.TTF"，字体应该放置于asset目录下
     */
    void setActivityFont(Activity activity, String fontPathInAssets);

    /**
     * 对某个View设置字体
     * @param view view
     * @param fontPathInAssets 字体路径：fonts/Test.TTF"，字体应该放置于asset目录下
     */
    <V extends View> void setViewFont(V view, String fontPathInAssets);
}
