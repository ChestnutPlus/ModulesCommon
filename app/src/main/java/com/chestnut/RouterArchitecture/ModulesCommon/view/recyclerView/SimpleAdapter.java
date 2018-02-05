package com.chestnut.RouterArchitecture.ModulesCommon.view.recyclerView;

import com.chestnut.common.ui.recyclerView.XAdapter;
import com.chestnut.common.ui.recyclerView.XHolder;
import com.chestnut.common.ui.recyclerView.XItem;

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

public class SimpleAdapter extends XAdapter<XItem> {

    //在这里统一Item的类型
    public static final int TYPE_ITEM_1= -1;
    public static final int TYPE_ITEM_2= -2;
    public static final int TYPE_ITEM_GALLERY = -3;
    public static final int TYPE_ITEM_TXT = -4;

    /**
     * 如果子类需要在onBindViewHolder 回调的时候做的操作可以在这个方法里做
     */
    @Override
    protected void onViewHolderBound(XHolder holder, int position) {

    }
}
