package com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.item;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chestnut.common.helper.si.XFontHelper;
import com.chestnut.common.ui.recyclerView.XHolder;
import com.chestnut.common.ui.recyclerView.XItem;
import com.chestnut.RouterArchitecture.ModulesCommon.R;
import com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.SimpleAdapter;
import com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.bean.ItemBean1;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/7/6 17:47
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class Item1 extends XItem<ItemBean1>{

    public Item1(ItemBean1 itemBean1) {
        super(itemBean1);
    }

    @Override
    public XHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new XHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_1,null));
    }

    @Override
    public void onBindViewHolder(XHolder holder, int position) {
        TextView textView = (TextView) holder.getViewById(R.id.textView);
        textView.setTypeface(XFontHelper.getInstance().get("fonts/caonima.ttf"));
        textView.setTag(position);
        textView.setOnClickListener(view -> {
            if (onItemListener!=null)
                onItemListener.onItemClick(view, (Integer) view.getTag());
        });
    }

    @Override
    public int getItemType() {
        return SimpleAdapter.TYPE_ITEM_1;
    }

    @Override
    public void releaseRes() {

    }
}
