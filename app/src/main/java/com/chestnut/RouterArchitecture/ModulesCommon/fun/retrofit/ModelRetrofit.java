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

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

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
            testGSonConverter(activity, TAG);
            testScalarsConverter(activity, TAG);
            testRxGsonConverter(activity, TAG);
        });
        simpleAdapter.add(t2);
    }

    private void testGSonConverter(Activity activity,String TAG) {
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
                //这里顺序添加了两个解析器，Retrofit会按顺序去尝试解析
                //当解析成功则跳过后面的解析器
                //若解析失败，则尝试后面的。
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .addConverterFactory(ScalarsConverterFactory.create())
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
    }

    private void testScalarsConverter(Activity activity,String TAG) {
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
                .addConverterFactory(ScalarsConverterFactory.create()) //设置数据解析器
                .client(client)
                .build();
        //创建 网络请求接口 的实例
        GetAppList getAppList = retrofit1.create(GetAppList.class);
        Call<String> appListBeanCall1 = getAppList.getString(1,8);
        appListBeanCall1.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body()!=null)
                        LogUtils.i(TAG, response.body());
                }
            }
            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                LogUtils.i(TAG,t.getMessage());
            }
        });
    }

    private void testRxGsonConverter(Activity activity,String TAG) {
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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //设置请求适配器
                .client(client)
                .build();
        //创建 网络请求接口 的实例
        GetAppList getAppList = retrofit1.create(GetAppList.class);
        Observable<AppListBean> appListBeanCall1 = getAppList.getBeanRx(1,8);
        appListBeanCall1.subscribeOn(Schedulers.io())
                .map(appListBean -> {
                    LogUtils.i(TAG,"map:"+Thread.currentThread().getName());
                    return appListBean;
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnNext(appListBean -> {
                    LogUtils.i(TAG,"doOnNext:"+Thread.currentThread().getName());
                })
                .observeOn(Schedulers.io())
                .subscribe(appListBean -> {
                    LogUtils.i(TAG,"subscribe:"+Thread.currentThread().getName());
                    for (AppListBean.DataBean d : appListBean.data) {
                        LogUtils.i(TAG,d.toString());
                    }
                },throwable -> {
                    LogUtils.i(TAG,"throwable:"+Thread.currentThread().getName());
                },()->{
                    LogUtils.i(TAG,"compete:"+Thread.currentThread().getName());
                });
    }


}
