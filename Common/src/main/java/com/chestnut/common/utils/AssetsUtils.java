package com.chestnut.common.utils;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018年3月27日10:04:17
 *     desc  :  封装有关：Assets 的操作
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class AssetsUtils {

    /**
     * 获取bytes字节
     * @param context 上下文
     * @param assetsFile asset 文件的相对路径，类似于：turing/xx.dat
     * @return bytes
     */
    public static byte[] getFileBytes(Context context, String assetsFile) {
        InputStream is = null;
        try {
            is = context.getAssets().open(assetsFile);
            return ConvertUtils.inputStream2Bytes(is);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (is!=null)
                    is.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 从 Asset 中复制文件
     * @param context 上下文
     * @param assetsFile asset 文件的相对路径，类似于：turing/xx.dat
     * @param newPathFile 新文件的路径
     * @return 是否成功
     */
    public static boolean copyTo(Context context, String assetsFile, String newPathFile) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            File file = new File(newPathFile);
            is = context.getAssets().open(assetsFile);
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[7168];
            int count = 0;
            while ((count = is.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            fos.close();
            is.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (is!=null)
                    is.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            try {
                if (fos!=null)
                    fos.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return false;
        }
    }
}
