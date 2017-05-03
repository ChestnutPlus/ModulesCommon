package com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView;

import com.chestnut.Common.ui.RecyclerView.Base.BaseAdapter;
import com.chestnut.Common.ui.RecyclerView.Base.BaseHolder;
import com.chestnut.Common.ui.RecyclerView.Base.BaseItem;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/4/30 21:27
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */

public class SimpleAdapter extends BaseAdapter<BaseItem> {

    //在这里统一Item的类型
    public static final int TYPE_BUTTON = -1;

    /**
     * 如果子类需要在onBindViewHolder 回调的时候做的操作可以在这个方法里做
     *
     * @param holder
     * @param position
     */
    @Override
    protected void onViewHolderBound(BaseHolder holder, int position) {

    }
}
