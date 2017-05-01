package com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chestnut.RouterArchitecture.ModulesCommon.R;
import com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.Base.BaseItemHolder;
import com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.Base.Item;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/4/27 23:29
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */

public class TxtItem extends Item<String>{

    public static final int TYPE = 2;
    public String s;

    public TxtItem(String s) {
        super(s);
        this.s = s;
    }

    @Override
    public BaseItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cell,null));
    }

    @Override
    public void onBindViewHolder(BaseItemHolder holder, int position) {
        ((TextView)holder.getViewById(R.id.textView2)).setText(s);
    }


    @Override
    public int getItemType() {
        return TYPE;
    }
}
