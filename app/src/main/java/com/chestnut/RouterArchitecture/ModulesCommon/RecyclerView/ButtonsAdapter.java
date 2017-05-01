package com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chestnut.RouterArchitecture.ModulesCommon.R;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/4/27 18:28
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */

public class ButtonsAdapter extends android.support.v7.widget.RecyclerView.Adapter<ButtonsViewHolder> {

    private String[] msg = null;
    private String[] btnTitles = null;
    private Context context = null;

    public ButtonsAdapter(String[] msg, String[] btnTitles, Context context) {
        this.msg = msg;
        this.btnTitles = btnTitles;
        this.context = context;
    }

    @Override
    public ButtonsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.adapter_row, parent, false);
        return new ButtonsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ButtonsViewHolder holder, int position) {
        holder.textView.setText(msg[position]);
        holder.button.setText(btnTitles[position]);
        holder.button.setTag(position);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemListener!=null)
                    onItemListener.onItemClick(view, (Integer) view.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return btnTitles.length;
    }

    private OnItemListener onItemListener = null;

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public interface OnItemListener {
        void onItemClick(View view, int position);
    }
}
