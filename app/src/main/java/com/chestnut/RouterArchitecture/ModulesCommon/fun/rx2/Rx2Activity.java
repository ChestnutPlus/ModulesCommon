package com.chestnut.RouterArchitecture.ModulesCommon.fun.rx2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chestnut.RouterArchitecture.ModulesCommon.R;
import com.chestnut.common.manager.RxBusManager;
import com.chestnut.common.manager.UtilsManager;
import com.chestnut.common.utils.LogUtils;
import com.chestnut.common.utils.RxUtils;
import com.chestnut.common.utils.SimpleDownloadUtils;

import java.util.concurrent.TimeUnit;

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
    private Observable<Long> longObservable = Observable.interval(1, TimeUnit.SECONDS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx2);

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(RxUtils.countDown(5)
                .doOnSubscribe(disposable -> {

                })
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
                    UtilsManager.getCachePath()+"/beauty.png")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<Boolean>() {
                        @Override
                        public void onNext(Boolean aBoolean) {
                            LogUtils.i(TAG,"onNext,"+aBoolean);
                            if (aBoolean)
                                Glide.with(Rx2Activity.this)
                                        .load(UtilsManager.getCachePath()+"/beauty.png")
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
            RxBusManager.getInstance().post(String.valueOf(System.currentTimeMillis()));
        });
        compositeDisposable.add(RxBusManager.getInstance().listen(String.class)
                .subscribe(s -> LogUtils.i(TAG,"RxBusManager:"+s)));

        //click filter
        compositeDisposable.add(RxUtils.filterClick(findViewById(R.id.txt_filter),1000)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(o -> {
                    LogUtils.i(TAG,"flatMap");
                    return longObservable;
                })
                .subscribe(o -> {
                    LogUtils.i(TAG,"txt_filter");
                }));

        //测试线程的切换
        compositeDisposable.add(Observable.create((ObservableOnSubscribe<Integer>) e -> {
                    LogUtils.i(TAG,"subscribe1:"+Thread.currentThread().getName());
                    e.onNext(1);
                    e.onComplete();
                }).subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.io()).map(integer -> {
                            LogUtils.i(TAG,"map1:"+Thread.currentThread().getName());
                            return integer;
                        })
                        .observeOn(Schedulers.newThread()).map(integer -> {
                            LogUtils.i(TAG,"map2:"+Thread.currentThread().getName());
                            return integer;
                        })
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(integer -> {
                            LogUtils.i(TAG,"subscribe2:"+Thread.currentThread().getName());
                        },throwable -> {

                        },()->{
                            LogUtils.i(TAG,"compete:"+Thread.currentThread().getName());
                        })
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
