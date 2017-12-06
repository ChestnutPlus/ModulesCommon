package com.chestnut.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import rx.Observable;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/8/8 20:54
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class SimpleDownloadUtils {

    public static boolean downLoad(String url, String localFile) {
        try {
            URL url1 = new URL(url);
            InputStream is = url1.openStream();
            File file = new File(localFile);
            if (!file.exists())
                file.createNewFile();
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = is.read(buffer)) > 0)
            {
                outputStream.write(buffer,0,len);
            }
            is.close();
            outputStream.close();
            return true;
        } catch (Exception e) {
            ExceptionCatchUtils.catchE(e);
            return false;
        }
    }

    public static Observable<Boolean> downLoadRx(String url, String localFile) {
        return Observable.create(subscriber -> {
            subscriber.onNext(downLoad(url, localFile));
            subscriber.onCompleted();
        });
    }

    /***
     * 下载，并检验MD5
     *
     * @param url               地址
     * @param localFile         本地文件
     * @param theNewFileMd5     文件md5
     * @param retryNum          重试次数
     * @return  是否成功
     */
    public static Observable<Boolean> downLoadRx(String url, String localFile, String theNewFileMd5, int retryNum) {
        if (theNewFileMd5==null || theNewFileMd5.length()==0)
            return downLoadRx(url, localFile);
        else
            return Observable.create(subscriber -> {
                for (int i = 0; i < retryNum+1; i++) {
                    if (downLoad(url, localFile) && EncryptUtils.encryptMD5File2String(localFile).equalsIgnoreCase(theNewFileMd5)) {
                        subscriber.onNext(true);
                        break;
                    }
                    if (i==retryNum)
                        subscriber.onNext(false);
                    else
                        LogUtils.i(true,"SimpleDownloadUtils",url+",retry:"+(i+1));
                }
                subscriber.onCompleted();
            });
    }

    public static Observable<Boolean> downLoadRx(String url, String localFile, String theNewFileMd5) {
        return downLoadRx(url, localFile, theNewFileMd5,2);
    }
}
