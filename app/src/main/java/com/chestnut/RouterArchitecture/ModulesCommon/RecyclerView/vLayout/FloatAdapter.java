package com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.vLayout;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.chestnut.RouterArchitecture.ModulesCommon.R;

import java.util.List;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/9/3 23:07
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */

public class FloatAdapter extends DelegateAdapter.Adapter<FloatAdapter.FloatViewHolder>{

    private LayoutHelper layoutHelper;
    private List<Integer> integerList;

    public FloatAdapter(LayoutHelper layoutHelper, List<Integer> integerList) {
        this.layoutHelper = layoutHelper;
        this.integerList = integerList;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @Override
    public FloatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FloatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_img,parent,false));
    }

    @Override
    public void onBindViewHolder(FloatViewHolder holder, int position) {
        holder.imageView.setBackgroundResource(integerList.get(position));
    }

    @Override
    public int getItemCount() {
        return integerList.size();
    }

    static class FloatViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public FloatViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
