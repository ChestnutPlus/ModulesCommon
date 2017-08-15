package com.chestnut.Common.rx;

import android.view.View;
import android.view.animation.Animation;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.Subject;

/**
 * <pre>
 *     author: Chestnut
 *     blog  :
 *     time  : 2016年10月20日16:28:09
 *     desc  : Rx   一些常用 小功能 代码集合
 *     thanks To:
 *     dependent on:
 *     updateLog：
 *          1.0.0   倒计时。
 *          1.0.1   2017年1月29日15:09:25  把原生的动画，撸成Rx by 栗子
 * </pre>
 */
public class RxUtils {

    /**
     * 倒计时，倒计 time 秒
     * @param time  单位：秒
     * @return  Observable
     */
    public static Observable<Integer> countDown(int time) {
        if (time < 0) time = 0;
        final int countTime = time;
        return Subject.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .map(increaseTime -> countTime - increaseTime.intValue())
                .take(countTime + 1);
    }

    /**
     * 开启动画
     * 要清楚，动画的回调，若是有重复，则是 ： {0} - {-1} - {-1} ... {1}
     * 若，没有重复，则是：{0} - {1}
     *
     * @param animation 动画
     * @param view  指定的View
     * @return  Observable，Int， 0 开始 ， 1 结束 ， -1 重复
     */
    public static Observable<Integer> startAnim(Animation animation, View view) {
        return Observable.create((Observable.OnSubscribe<Integer>) subscriber -> {
            int[] repeatCount = {animation.getRepeatCount()};
            int[] thisCount = {0};
            Animation.AnimationListener animationListener = new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation1) {
                    subscriber.onNext(0);
                }

                @Override
                public void onAnimationEnd(Animation animation1) {
                    subscriber.onNext(1);
                    if (repeatCount[0] == thisCount[0]) {
                        subscriber.onCompleted();
                    }
                    thisCount[0]++;
                }

                @Override
                public void onAnimationRepeat(Animation animation1) {
                    subscriber.onNext(-1);
                }
            };
            animation.setAnimationListener(animationListener);
            view.startAnimation(animation);
        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Integer> countClickNum(View view) {
        return Observable.create(subscriber -> {
            if (view!=null) {
                view.setOnClickListener(v -> {
                    subscriber.onNext(1);
                });
            }
        }).debounce(1500,TimeUnit.MILLISECONDS)
                .map(o -> 1);
    }
}
