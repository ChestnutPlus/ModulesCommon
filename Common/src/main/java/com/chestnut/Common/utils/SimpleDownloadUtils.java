package com.chestnut.Common.utils;

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
            OutputStream outputStream = new FileOutputStream(localFile);
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
}
