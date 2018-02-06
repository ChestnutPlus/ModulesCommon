package com.chestnut.RouterArchitecture.ModulesCommon.fun.retrofit;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/1/21 13:13
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */

public interface GetAppList {

    @GET("getAppList?pageIndex=0&pageSize=8")
    Call<AppListBean> get();

    //返回的是Bean，所以需要使用GSON的解析器
    @GET("getAppList")
    @Headers("Cache-Control:public,max-age=20")
    Call<AppListBean> get(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    //返回的是String，需使用Scalars的解析器
    @GET("getAppList")
    @Headers("Cache-Control:public,max-age=20")
    Call<String> getString(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    //返回的是Bean，所以需要使用GSON的解析器
    //而且是Rx的类型，还需要，Rx的适配器
    @GET("getAppList")
    @Headers("Cache-Control:public,max-age=20")
    Observable<AppListBean> getBeanRx(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);
}
