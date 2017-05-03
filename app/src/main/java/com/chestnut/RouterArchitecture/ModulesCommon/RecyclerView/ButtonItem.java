package com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.chestnut.Common.ui.RecyclerView.Base.BaseHolder;
import com.chestnut.Common.ui.RecyclerView.Base.BaseItem;
import com.chestnut.RouterArchitecture.ModulesCommon.R;

/**
 * Created by Chestnut on 2017/5/3.
 */

public class ButtonItem extends BaseItem<ButtonBean>{

    private ButtonBean buttonBean;

    public ButtonItem(ButtonBean buttonBean) {
        super(buttonBean);
        this.buttonBean = buttonBean;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_row,null));
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        ((TextView)holder.getViewById(R.id.textView)).setText(buttonBean.msg);
        ((Button)holder.getViewById(R.id.button)).setText(buttonBean.btnName);
        holder.getViewById(R.id.button).setTag(position);
        holder.getViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemListener!=null)
                    onItemListener.onItemClick(view, (Integer) view.getTag());
            }
        });
    }

    @Override
    public int getItemType() {
        return SimpleAdapter.TYPE_BUTTON;
    }

    @Override
    public void releaseRes() {

    }
}
