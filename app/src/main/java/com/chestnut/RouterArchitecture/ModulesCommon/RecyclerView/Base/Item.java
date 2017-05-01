package com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.Base;

import android.view.View;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/4/27 22:48
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */

public abstract class Item<DATA> implements BaseItem {

    public DATA data;   //数据实体，数据Bean

    public Item(DATA data) {
        this.data = data;
    }

    /**
     * 释放资源
     *  如果有需要回收的资源，子类自己实现
     */
    @Override
    public void releaseRes() {}

    /**
     * 点击的回调接口，如果需要，则在子类实现逻辑。
     *
     *  例如，Button的点击事件可以这样做：
     *  holder.getViewById(R.id.button).setTag(position);
            holder.getViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                if (onItemListener!=null) {
                    onItemListener.onItemClick(view, (Integer) view.getTag());
                }
                }
            });
     */

    public interface OnItemListener {
        void onItemClick(View view, int position);
    }

    protected OnItemListener onItemListener = null;

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }
}
