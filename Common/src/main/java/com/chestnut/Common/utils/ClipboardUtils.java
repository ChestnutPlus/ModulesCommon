package com.chestnut.Common.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author: Chestnut
 *     blog  :
 *     time  : 2016年10月6日
 *     desc  : 剪贴板相关工具类
 *     thanks To:
 *     dependent on:
 * </pre>
 */
public class ClipboardUtils {

    public enum Type{
        Text,
        Uri,
        Intent,
        Null,
    }

    private ClipboardUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 复制文本到剪贴板
     *
     * @param context 上下文
     * @param text    文本
     */
    public static void copyText(Context context, CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("text", text));
    }

    /**
     * 获取剪贴板的文本
     *
     * @param context 上下文
     * @return 剪贴板的文本
     */
    public static CharSequence getText(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = clipboard.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).coerceToText(context);
        }
        return null;
    }

    /**
     * 复制uri到剪贴板
     *
     * @param context 上下文
     * @param uri     uri
     */
    public static void copyUri(Context context, Uri uri) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newUri(context.getContentResolver(), "uri", uri));
    }

    /**
     * 获取剪贴板的uri
     *
     * @param context 上下文
     * @return 剪贴板的uri
     */
    public static Uri getUri(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = clipboard.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).getUri();
        }
        return null;
    }

    /**
     * 复制意图到剪贴板
     *
     * @param context 上下文
     * @param intent  意图
     */
    public static void copyIntent(Context context, Intent intent) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newIntent("intent", intent));
    }

    /**
     * 获取剪贴板的意图
     *
     * @param context 上下文
     * @return 剪贴板的意图
     */
    public static Intent getIntent(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = clipboard.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).getIntent();
        }
        return null;
    }

    /**
     *  获取剪切板的内容
     * @param context 上下文
     * @return  返回的是List，0：Type，1：内容
     */
    public static List getContent(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = clipboard.getPrimaryClip();
        List<Object> list = new ArrayList<>();
        if (clip != null && clip.getItemCount() > 0) {
            ClipData.Item item = clip.getItemAt(0);
            try {
                item.getUri().getAuthority();
                list.add(Type.Uri);
                list.add(item.getUri());
                return list;
            }catch (Exception n) {
                ExceptionCatchUtils.catchE(n,"ClipboardUtils");
                list.clear();
                try {
                    item.getIntent().getAction();
                    list.add(Type.Intent);
                    list.add(item.getIntent());
                    return list;
                }catch (Exception n1) {
                    ExceptionCatchUtils.catchE(n1,"ClipboardUtils");
                    list.clear();
                    try {
                        list.add(Type.Text);
                        list.add(item.getText());
                        return list;
                    }catch (Exception n2) {
                        ExceptionCatchUtils.catchE(n2,"ClipboardUtils");
                        list.clear();
                        list.add(Type.Null);
                        list.add(Type.Null);
                        return list;
                    }
                }
            }
        }
        list.clear();
        list.add(Type.Null);
        list.add(Type.Null);
        return list;
    }

}
