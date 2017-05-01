package com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.Base;

import android.view.ViewGroup;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/4/27 22:41
 *     desc  :  对于多种Adapter中的
 *              Item进行抽离共有部分。
 *              1 . 视图绑定
 *              2 . 数据绑定
 *              3 . 资源释放
 *              4 . 唯一id
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */

public interface BaseItem {

    /**
     * 1.创建视图
     * @param parent    parent
     * @param viewType  type
     * @return  holder
     */
    BaseItemHolder onCreateViewHolder(ViewGroup parent, int viewType);

    /**
     * 2.数据绑定
     * @param holder    视图拥有者
     * @param position  位置
     */
    void onBindViewHolder(BaseItemHolder holder, int position);

    /**
     * 3.释放资源
     */
    void releaseRes();

    /**
     * 4.获取ViewType
     * @return  type
     */
    int getItemType();
}
