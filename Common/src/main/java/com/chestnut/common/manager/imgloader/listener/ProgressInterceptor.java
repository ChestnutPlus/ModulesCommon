package com.chestnut.common.manager.imgloader.listener;

import com.chestnut.common.manager.imgloader.contract.ImgLoaderListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/6/8 15:14
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class ProgressInterceptor implements Interceptor {

    static final Map<String, ImgLoaderListener> LISTENER_MAP = new HashMap<>();

    public static void addListener(String url, ImgLoaderListener listener) {
        if (listener!=null)
            LISTENER_MAP.put(url, listener);
    }

    public static void removeListener(String url) {
        LISTENER_MAP.remove(url);
    }

    public static ImgLoaderListener getListener(String url) {
        return LISTENER_MAP.get(url);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        String url = request.url().toString();
        ResponseBody body = response.body();
        return response.newBuilder().body(new ProgressResponseBody(url, body)).build();
    }
}
