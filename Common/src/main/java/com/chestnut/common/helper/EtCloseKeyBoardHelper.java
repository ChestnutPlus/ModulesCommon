package com.chestnut.common.helper;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2019/3/21 10:04
 *     desc  : 点击EditText弹出软键盘，点击空白区域让软键盘消失
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class EtCloseKeyBoardHelper {

    private View[] etViews;
    private View[] filterViews;

    public EtCloseKeyBoardHelper(View[] etViews) {
        this.etViews = etViews;
    }

    public EtCloseKeyBoardHelper(View[] etViews, View[] filterViews) {
        this.etViews = etViews;
        this.filterViews = filterViews;
    }

    /**
     * 清除editText的焦点
     * @param views views
     */
    private void clearViewFocus(View v, View[] views) {
        if (null != views && views.length > 0) {
            for (View view : views) {
                if (v==view) {
                    view.clearFocus();
                    return;
                }
            }
        }
    }

    /**
     * 是否焦点在Et上
     *
     * @param v   焦点所在View
     * @param views 输入框
     * @return true代表焦点在edit上
     */
    private boolean isFocusEditText(View v, View[] views) {
        if (v instanceof EditText) {
            EditText et = (EditText) v;
            for (View view : views) {
                if (et.getId() == view.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断触摸点，是否在过滤控件上
     * @param views views
     * @param ev ev
     * @return true / false
     */
    private boolean isTouchView(View[] views, MotionEvent ev) {
        if (views == null || views.length == 0) {
            return false;
        }
        int[] location = new int[2];
        for (View view : views) {
            view.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            if (ev.getX() > x && ev.getX() < (x + view.getWidth())
                    && ev.getY() > y && ev.getY() < (y + view.getHeight())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 在分发方法里面，重写
     * @param ev event
     */
    public void dispatchTouchEvent(MotionEvent ev, Activity activity) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN
                && !isTouchView(filterViews, ev)
                && etViews!=null && etViews.length>0
                && !isTouchView(etViews, ev)) {
            View v = activity.getCurrentFocus();
            if (isFocusEditText(v, etViews)) {
                clearViewFocus(v, etViews);
                closeKeyboard(activity, v);
            }
        }
    }

    private void closeKeyboard(Context context, View v) {
        InputMethodManager imm = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(),0);
        }
    }
}
