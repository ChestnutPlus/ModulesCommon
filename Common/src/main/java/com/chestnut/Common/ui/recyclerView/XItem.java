package com.chestnut.Common.ui.recyclerView;

import android.view.View;
import android.view.ViewGroup;

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

public abstract class XItem<DATA> {

    public DATA data;   //数据实体，数据Bean

    public XItem(DATA data) {
        this.data = data;
    }

    /**
     * 1.创建视图
     * @param parent    parent
     * @param viewType  type
     * @return  holder
     */
    public abstract XHolder onCreateViewHolder(ViewGroup parent, int viewType);

    /**
     * 2.数据绑定
     * @param holder    视图拥有者
     * @param position  位置
     */
    public abstract void onBindViewHolder(XHolder holder, int position);

    /**
     * 4.获取ViewType
     * @return  type
     */
    public abstract int getItemType();

    /**
     * 释放资源
     *  如果有需要回收的资源，子类自己实现
     */
    public abstract void releaseRes();

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
