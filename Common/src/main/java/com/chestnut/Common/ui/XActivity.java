package com.chestnut.Common.ui;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/10/14 22:20
 *     desc  :  封装一些常用方法到Activity中
 *              项目中只需要继承此类就好
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */

public abstract class XActivity extends RxAppCompatActivity{

    private LayoutInflater mInflater;
    private boolean isFirstEnterResume = true;
    protected boolean activityIsRunning = false;

    @Override
    protected void onResume() {
        super.onResume();
        activityIsRunning = true;
        if (isFirstEnterResume) {
            isFirstEnterResume = false;
            initView();
            initData();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityIsRunning = false;
    }

    /**
     * 充气筒，注入View
     * @param resId id
     * @return  view
     */
    protected View inflateView(@LayoutRes int resId) {
        if (mInflater==null) {
            mInflater = getLayoutInflater();
        }
        return mInflater.inflate(resId, null);
    }

    protected void initView() {

    }

    protected void initData() {

    }
}
