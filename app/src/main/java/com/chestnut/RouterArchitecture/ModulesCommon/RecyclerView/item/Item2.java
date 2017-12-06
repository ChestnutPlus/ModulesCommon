package com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.item;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chestnut.common.ui.recyclerView.XHolder;
import com.chestnut.common.ui.recyclerView.XItem;
import com.chestnut.RouterArchitecture.ModulesCommon.R;
import com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.SimpleAdapter;
import com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.bean.ItemBean2;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/7/6 17:48
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class Item2 extends XItem<ItemBean2>{

    public Item2(ItemBean2 itemBean2) {
        super(itemBean2);
    }

    @Override
    public XHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new XHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_2,null));
    }

    @Override
    public void onBindViewHolder(XHolder holder, int position) {

    }

    @Override
    public int getItemType() {
        return SimpleAdapter.TYPE_ITEM_2;
    }

    @Override
    public void releaseRes() {

    }
}
