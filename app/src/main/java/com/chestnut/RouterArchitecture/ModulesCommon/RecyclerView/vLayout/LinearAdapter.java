package com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.vLayout;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.chestnut.RouterArchitecture.ModulesCommon.R;

import java.util.List;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/9/3 22:43
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */

public class LinearAdapter extends DelegateAdapter.Adapter<LinearAdapter.LinearViewHolder>{

    private LayoutHelper layoutHelper;
    private List<String> stringList;

    public LinearAdapter(LayoutHelper layoutHelper, List<String> stringList) {
        this.layoutHelper = layoutHelper;
        this.stringList = stringList;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @Override
    public LinearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_1,parent,false));
    }

    @Override
    public void onBindViewHolder(LinearViewHolder holder, int position) {
        holder.tv1.setText(stringList.get(position));
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    static class LinearViewHolder extends RecyclerView.ViewHolder {
        public TextView tv1;
        public LinearViewHolder(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.textView);
        }
    }
}
