package com.chestnut.RouterArchitecture.ModulesCommon.view.recyclerView.item;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chestnut.RouterArchitecture.ModulesCommon.R;
import com.chestnut.RouterArchitecture.ModulesCommon.base.ViewConfig;
import com.chestnut.RouterArchitecture.ModulesCommon.view.recyclerView.SimpleAdapter;
import com.chestnut.common.helper.si.XFontHelper;
import com.chestnut.common.ui.recyclerView.XHolder;
import com.chestnut.common.ui.recyclerView.XItem;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/2/5 23:33
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */

public class TxtItem extends XItem<String>{

    public TxtItem(String s) {
        super(s);
    }

    @Override
    public XHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new XHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_txt,null));
    }

    @Override
    public void onBindViewHolder(XHolder holder, int position) {
        TextView textView = (TextView) holder.getViewById(R.id.txt);
        XFontHelper.getInstance().setViewFont(textView, ViewConfig.TypeFace_HK);
        textView.setText(data);
        if (callback!=null)
            textView.setOnClickListener(view -> {
                if (callback!=null)
                    callback.onItemClick(data);
            });
    }

    @Override
    public int getItemType() {
        return SimpleAdapter.TYPE_ITEM_TXT;
    }

    @Override
    public void releaseRes() {

    }

    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void onItemClick(String s);
    }
}
