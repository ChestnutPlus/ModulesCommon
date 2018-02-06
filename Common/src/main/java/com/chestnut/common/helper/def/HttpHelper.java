//package com.chestnut.common.helper.def;
//
//import android.support.annotation.NonNull;
//
//import com.chestnut.common.contract.web.HttpInterface;
//
//import java.io.IOException;
//import java.util.Map;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.FormBody;
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//import rx.Observable;
//
///**
// * <pre>
// *     author: Chestnut
// *     blog  : http://www.jianshu.com/u/a0206b5f4526
// *     time  : 2018/2/5 14:12
// *     desc  :  粗糙地封装了一下Http的请求
// *     thanks To:
// *     dependent on:
// *     update log:
// * </pre>
// */
//public class HttpHelper implements HttpInterface<Map<String,String>,String>{
//
//    @Override
//    public Observable<String> RxGet(String url, Map<String, String> stringStringMap) {
//        return Observable.create(subscriber -> {
//            OkHttpClient client = new OkHttpClient();
//            Request request = new Request.Builder()
//                    .get()
//                    .url(assemblyGetParam(url, stringStringMap))
//                    .build();
//            Call call = client.newCall(request);
//            call.enqueue(new Callback() {
//                @Override
//                public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                    subscriber.onError(e);
//                    subscriber.onCompleted();
//                }
//
//                @Override
//                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                    try {
//                        if (response.body()!=null && response.body().string()!=null) {
//                            subscriber.onNext(response.body().string());
//                        }
//                        else {
//                            subscriber.onNext(null);
//                        }
//                    } catch (Exception e) {
//                        subscriber.onNext(null);
//                    }
//                    subscriber.onCompleted();
//                }
//            });
//        });
//    }
//
//    @Override
//    public Observable<String> RxPost(String url, Map<String, String> stringStringMap) {
//        return Observable.create(subscriber -> {
//            OkHttpClient okHttpClient = new OkHttpClient();
//            FormBody.Builder builder = new FormBody.Builder();
//            for (String key: stringStringMap.keySet()) {
//                builder.add(key,stringStringMap.get(key));
//            }
//            FormBody formBody = builder.build();
//            Request request = new Request.Builder()
//                    .url(url)
//                    .post(formBody)
//                    .build();
//            okHttpClient.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                    subscriber.onError(e);
//                    subscriber.onCompleted();
//                }
//
//                @Override
//                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                    try {
//                        if (response.body()!=null && response.body().string()!=null) {
//                            subscriber.onNext(response.body().string());
//                        }
//                        else {
//                            subscriber.onNext(null);
//                        }
//                    } catch (Exception e) {
//                        subscriber.onNext(null);
//                    }
//                    subscriber.onCompleted();
//                }
//            });
//        });
//    }
//
//    @Override
//    public Observable<String> RxPostFile(String url, Map<String, String> map,String fileType, String fileName, String mediaType, byte[] fileBytes) {
//        return Observable.create(subscriber -> {
//            OkHttpClient mOkHttpClient = new OkHttpClient();
//            MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
//            RequestBody requestBody = RequestBody.create(MediaType.parse(mediaType),fileBytes);
//            body.addFormDataPart(fileType,fileName,requestBody);
//            for (String key : map.keySet()) {
//                body.addFormDataPart(key,map.get(key));
//            }
//            Request request = new Request.Builder()
//                    .url(url)
//                    .post(body.build())
//                    .build();
//            mOkHttpClient.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                    subscriber.onError(e);
//                    subscriber.onCompleted();
//                }
//
//                @Override
//                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                    try {
//                        if (response.body()!=null && response.body().string()!=null) {
//                            subscriber.onNext(response.body().string());
//                        }
//                        else {
//                            subscriber.onNext(null);
//                        }
//                    } catch (Exception e) {
//                        subscriber.onNext(null);
//                    }
//                    subscriber.onCompleted();
//                }
//            });
//        });
//    }
//
//    /**
//     * 拼接Get参数
//     * @param url   地址
//     * @param params    参数
//     * @return result
//     */
//    private static String assemblyGetParam(String url, Map<String, String> params) {
//        StringBuilder s = new StringBuilder();
//        s.append(url).append("?");
//        for (String temp:
//                params.keySet()){
//            s.append(temp).append("=").append(params.get(temp)).append("&");
//        }
//        s.deleteCharAt(s.length()-1);
//        return s.toString();
//    }
//}
