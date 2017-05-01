package com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chestnut.RouterArchitecture.ModulesCommon.R;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/4/27 18:30
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */

public class ButtonsViewHolder extends RecyclerView.ViewHolder {

    public TextView textView;
    public Button button;

    public ButtonsViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.textView);
        button = (Button) itemView.findViewById(R.id.button);
    }
}
