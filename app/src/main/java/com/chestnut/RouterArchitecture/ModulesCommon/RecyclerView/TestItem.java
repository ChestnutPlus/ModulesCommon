package com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chestnut.RouterArchitecture.ModulesCommon.R;
import com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.Base.BaseItemHolder;
import com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.Base.Item;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/4/27 23:22
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */

public class TestItem extends Item<String> {

    public static final int TYPE = 1;
    public String s;

    public TestItem(String s) {
        super(s);
        this.s = s;
    }

    @Override
    public BaseItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_row,null));
    }

    @Override
    public void onBindViewHolder(BaseItemHolder holder, int position) {
        ((TextView)holder.getViewById(R.id.textView)).setText(s);
        holder.getViewById(R.id.button).setTag(position);
        holder.getViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemListener!=null) {
                    onItemListener.onItemClick(view, (Integer) view.getTag());
                }
            }
        });
    }

    @Override
    public int getItemType() {
        return TYPE;
    }
}
