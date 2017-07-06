package com.chestnut.Common.ui.recyclerView;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/4/27 23:02
 *     desc  :  封装了Holder。
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */

public class XHolder extends RecyclerView.ViewHolder{

    private SparseArray<View> views;
    private View mItemView;

    public XHolder(View itemView) {
        super(itemView);
        views = new SparseArray<>();
        mItemView = itemView;
    }

    /**
     * 获取 ItemView
     * @return ItemView
     */
    public View getItemHoldView() {
        return mItemView;
    }

    /**
     * 获取View,根据ID
     * @param resId id
     * @return view
     */
    public View getViewById(@IdRes int resId) {
        return retrieveView(resId);
    }

    /**
     * 检索View
     *  若，当前缓存的View没有，
     *      则 find and put into the views
     *
     * @param viewId  viewID
     * @param <V> 类型
     * @return  view
     */
    @SuppressWarnings("unchecked")
    protected <V extends View> V retrieveView(@IdRes int viewId){
        View view = views.get(viewId);
        if(view == null){
            view = mItemView.findViewById(viewId);
            views.put(viewId,view);
        }
        return (V) view;
    }
}
