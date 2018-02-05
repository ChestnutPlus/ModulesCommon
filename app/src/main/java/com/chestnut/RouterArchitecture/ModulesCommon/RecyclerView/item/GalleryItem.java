package com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.item;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chestnut.common.ui.recyclerView.XHolder;
import com.chestnut.common.ui.recyclerView.XItem;
import com.chestnut.common.utils.MathUtils;
import com.chestnut.RouterArchitecture.ModulesCommon.R;
import com.chestnut.RouterArchitecture.ModulesCommon.view.recyclerView.SimpleAdapter;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/8/29 13:59
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class GalleryItem extends XItem<String> {

    private static int[] pics = {
            R.drawable.pic4,
            R.drawable.pic5,
            R.drawable.pic6,
    };

    public GalleryItem(String s) {
        super(s);
    }

    @Override
    public XHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new XHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_gallery,null));
    }

    @Override
    public void onBindViewHolder(XHolder holder, int position) {
        Glide.with(holder.getItemHoldView().getContext())
                .load(pics[MathUtils.randomInt(0,2)])
                .placeholder(R.drawable.li_bao_en)
                .into((ImageView) holder.getViewById(R.id.image));
    }

    @Override
    public int getItemType() {
        return SimpleAdapter.TYPE_ITEM_GALLERY;
    }

    @Override
    public void releaseRes() {

    }
}
