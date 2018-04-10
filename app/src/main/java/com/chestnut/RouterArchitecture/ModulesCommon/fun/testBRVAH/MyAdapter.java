package com.chestnut.RouterArchitecture.ModulesCommon.fun.testBRVAH;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chestnut.RouterArchitecture.ModulesCommon.R;

import java.util.List;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/4/6 23:13
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */

public class MyAdapter extends BaseQuickAdapter<BrvahBean,MyViewHolder>{

    public MyAdapter(@Nullable List<BrvahBean> data) {
        super(R.layout.adapter_brvah_str, data);
    }

    /**
     * View Bind 的回调
     * @param helper holder
     * @param item data
     */
    @Override
    protected void convert(MyViewHolder helper, BrvahBean item) {
        helper.setText(R.id.tv_content,item.str);
        helper.setText(R.id.tv_position,String.valueOf(helper.getLayoutPosition()));
        helper.setImageResource(R.id.img,item.img);
    }
}
