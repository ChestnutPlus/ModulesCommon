package com.chestnut.RouterArchitecture.ModulesCommon.fun.retrofit;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.chestnut.RouterArchitecture.ModulesCommon.base.CommonContract;
import com.chestnut.RouterArchitecture.ModulesCommon.view.recyclerView.SimpleAdapter;
import com.chestnut.RouterArchitecture.ModulesCommon.view.recyclerView.item.TxtItem;
import com.chestnut.common.os.OkHttpCommonInterceptor;
import com.chestnut.common.ui.XToast;
import com.chestnut.common.utils.LogUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/2/6 13:01
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class ModelRetrofit implements CommonContract{
    @Override
    public void onModelTest(SimpleAdapter simpleAdapter, XToast toast, String TAG, Activity activity) {
        TxtItem t2 = new TxtItem("Op-Retrofit");
        t2.setCallback(s -> {
            toast.setText(s).show();
            LogUtils.i(TAG,s);
            //声明缓存地址和大小
            Cache cache = new Cache(activity.getCacheDir(),10*1024*1024);
            //构建 Client
            //参考：http://blog.csdn.net/changsimeng/article/details/54668884
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new OkHttpCommonInterceptor.CommonNoNetCache(20,activity))
                    .addInterceptor(new OkHttpCommonInterceptor.Retry(3))
                    .addInterceptor(new OkHttpCommonInterceptor.CommonLog())
                    .addNetworkInterceptor(new OkHttpCommonInterceptor.CommonNetCache(15))
                    .cache(cache)
                    .retryOnConnectionFailure(true)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .build();
            //构建 Retrofit
            Retrofit retrofit1 = new Retrofit.Builder()
                    .baseUrl("http://windowserl.honeybot.cn:8080/Market/") //设置网络请求的Url地址
                    .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                    .client(client)
                    .build();
            //创建 网络请求接口 的实例
            GetAppList getAppList = retrofit1.create(GetAppList.class);
            Call<AppListBean> appListBeanCall1 = getAppList.get(1,8);
            appListBeanCall1.enqueue(new Callback<AppListBean>() {
                @Override
                public void onResponse(@NonNull Call<AppListBean> call, @NonNull retrofit2.Response<AppListBean> response) {
                    if (response.isSuccessful()) {
                        if (response.body()!=null && response.body().data!=null)
                            for (AppListBean.DataBean d : response.body().data) {
                                LogUtils.i(TAG,d.toString());
                            }
                    }
                }
                @Override
                public void onFailure(@NonNull Call<AppListBean> call, @NonNull Throwable t) {
                    LogUtils.i(TAG,t.getMessage());
                }
            });

        });
        simpleAdapter.add(t2);
    }
}
