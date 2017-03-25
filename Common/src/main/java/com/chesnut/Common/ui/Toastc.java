package com.chesnut.Common.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * <pre>
 *     author: Chestnut
 *     blog  :
 *     time  : 2016年10月20日16:28:09
 *     desc  : DIY Toast
 *     thanks To:
 *     dependent on:
 *     updateLog：
 *          1.0.0   基本功能。
 * </pre>
 */
public class Toastc {

    private Toast toast;
    private LinearLayout toastView;         //Toast的根布局

    /**
     * 完全自定义布局Toast
     */
    public Toastc() {
    }

    /**
     * 系统原生的布局Toast
     * @param context
     */
    public Toastc(Context context,int duration){
        toast=Toast.makeText(context.getApplicationContext(),"",duration);
        toast.setDuration(duration);
        toastView = (LinearLayout) toast.getView();
    }

    /**
     * 完全自定义布局Toast
     * @param context
     * @param view
     */
    public Toastc(Context context, View view, int gravity, int duration){
        toast=new Toast(context.getApplicationContext());
        toast.setView(view);
        toast.setDuration(duration);
        toast.setGravity(gravity,0,0);
        toastView = (LinearLayout) toast.getView();
    }

    /**
     * 向Toast中添加自定义view
     * @param view  完全自定义的。
     * @param gravity 位置
     * @return
     */
    public  Toastc setView(View view,int gravity) {
        toast.setGravity(gravity,0,0);
        toast.setView(view);
        return this;
    }

    /**
     *      设置内容
     * @param message   设置内容
     * @return
     */
    public Toastc setText(CharSequence message) {
        TextView textView=((TextView) toastView.findViewById(android.R.id.message));
        textView.setText(message);
        return this;
    }

    /**
     * 设置Toast字体及背景颜色
     * @param messageColor
     * @param backgroundColor
     * @return
     */
    public Toastc setToastColor(int messageColor, int backgroundColor) {
        View view = toast.getView();
        if(view!=null){
            TextView message=((TextView) view.findViewById(android.R.id.message));
            message.setBackgroundColor(backgroundColor);
            message.setTextColor(messageColor);
        }
        return this;
    }

    /**
     * 设置Toast字体及背景
     * @param messageColor  字体颜色
     * @param background    背景资源地址，传入自定义xml地址
     * @return
     */
    public Toastc setToastBackground(int messageColor, int background) {
        View view = toast.getView();
        view.setBackgroundColor(Color.TRANSPARENT);
        if(view!=null){
            TextView message=((TextView) view.findViewById(android.R.id.message));
            message.setBackgroundResource(background);
            message.setTextColor(messageColor);
        }
        return this;
    }


    /**
     * 短时间显示Toast
     */
    public  Toastc Short(Context context, CharSequence message){
        if(toast==null||(toastView!=null&&toastView.getChildCount()>1)){
            toast= Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toastView=null;
        }else{
            toast.setText(message);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        return this;
    }

    /**
     * 短时间显示Toast
     */
    public Toastc Short(Context context, int message) {
        if(toast==null||(toastView!=null&&toastView.getChildCount()>1)){
            toast= Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toastView=null;
        }else{
            toast.setText(message);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        return this;
    }

    /**
     * 长时间显示Toast
     */
    public Toastc Long(Context context, CharSequence message){
        if(toast==null||(toastView!=null&&toastView.getChildCount()>1)){
            toast= Toast.makeText(context, message, Toast.LENGTH_LONG);
            toastView=null;
        }else{
            toast.setText(message);
            toast.setDuration(Toast.LENGTH_LONG);
        }
        return this;
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public Toastc Long(Context context, int message) {
        if(toast==null||(toastView!=null&&toastView.getChildCount()>1)){
            toast= Toast.makeText(context, message, Toast.LENGTH_LONG);
            toastView=null;
        }else{
            toast.setText(message);
            toast.setDuration(Toast.LENGTH_LONG);
        }
        return this;
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public Toastc Indefinite(Context context, CharSequence message, int duration) {
        if(toast==null||(toastView!=null&&toastView.getChildCount()>1)){
            toast= Toast.makeText(context, message,duration);
            toastView=null;
        }else{
            toast.setText(message);
            toast.setDuration(duration);
        }
        return this;
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public Toastc Indefinite(Context context, int message, int duration) {
        if(toast==null||(toastView!=null&&toastView.getChildCount()>1)){
            toast= Toast.makeText(context, message,duration);
            toastView=null;
        }else{
            toast.setText(message);
            toast.setDuration(duration);
        }
        return this;
    }

    /**
     * 显示 Toast
     * @return
     */
    public Toastc show() {
        if (toast!=null) {
            toast.show();
        }
        return this;
    }
}
