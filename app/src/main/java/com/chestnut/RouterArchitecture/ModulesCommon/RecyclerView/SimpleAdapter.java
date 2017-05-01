package com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView;

import com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.Base.BaseAdapter;
import com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.Base.BaseItemHolder;
import com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.Base.Item;

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

public class SimpleAdapter extends BaseAdapter<Item>{

    /**
     * 如果子类需要在onBindViewHolder 回调的时候做的操作可以在这个方法里做
     *
     * @param holder
     * @param position
     */
    @Override
    protected void onViewHolderBound(BaseItemHolder holder, int position) {

    }
}
