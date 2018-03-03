package com.chestnut.RouterArchitecture.ModulesCommon.fun.rx2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chestnut.RouterArchitecture.ModulesCommon.R;
import com.chestnut.common.helper.si.RxBus;
import com.chestnut.common.helper.si.XUtilsHelper;
import com.chestnut.common.utils.LogUtils;
import com.chestnut.common.utils.RxUtils;
import com.chestnut.common.utils.SimpleDownloadUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

public class Rx2Activity extends AppCompatActivity {

    private String TAG = "Rx2Activity";
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx2);

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(RxUtils.countDown(5)
                .subscribe(integer -> {
                    LogUtils.i(TAG,"countDown:"+integer);
                },throwable -> {},()->{}));

        //示例创建操作符
        findViewById(R.id.txt_create).setOnClickListener(view -> {
            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> e) throws Exception {
                    e.onNext("hello");
                    e.onNext(null);
                    e.onComplete();
                }
            }).subscribe(new Consumer<String>() {
                @Override
                public void accept(String s) throws Exception {
                    LogUtils.i(TAG,"accept,"+s);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    LogUtils.i(TAG,"accept,"+throwable.getMessage());
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    LogUtils.i(TAG,"onComplete");
                }
            });
        });

        //示例原来RxJava1.x的方法直接改成RxJava2.x的方法
        findViewById(R.id.txt_downRx).setOnClickListener(view -> {
            SimpleDownloadUtils.downLoadRx("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1517919794691&di=a83628a6219f4fbe6470decc6b1a05b2&imgtype=0&src=http%3A%2F%2Fp3.gexing.com%2Fshaitu%2F20130405%2F1020%2F515e35039c8c4.jpg",
                    XUtilsHelper.getCachePath()+"/beauty.png")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<Boolean>() {
                        @Override
                        public void onNext(Boolean aBoolean) {
                            LogUtils.i(TAG,"onNext,"+aBoolean);
                            if (aBoolean)
                                Glide.with(Rx2Activity.this)
                                    .load(XUtilsHelper.getCachePath()+"/beauty.png")
                                    .into((ImageView) findViewById(R.id.img));
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtils.i(TAG,"onError,"+e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            LogUtils.i(TAG,"onComplete");
                        }
                    });
        });

        //测试RxBus
        findViewById(R.id.txt_bus).setOnClickListener(view -> {
            LogUtils.i(TAG,"compositeDisposable,"+compositeDisposable.size());
            RxBus.getInstance().post(String.valueOf(System.currentTimeMillis()));
        });
        compositeDisposable.add(RxBus.getInstance().listen(String.class)
                .subscribe(s -> LogUtils.i(TAG,"RxBus:"+s)));

        //click filter
        RxUtils.filterClick(findViewById(R.id.txt_filter),2000)
                .subscribe(o -> {
                    LogUtils.i(TAG,"txt_filter");
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
