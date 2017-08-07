package com.chestnut.RouterArchitecture.ModulesCommon;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class RadioActivity extends AppCompatActivity {

    private ImageView imgLeftWheel;
    private ImageView imgRightWheel;
    private ImageView imgPlay;
    private TextView txtTitle;
    private Subscription subscriptionRotateAnim;
    private ObjectAnimator rotateAnimationLeft;
    private ObjectAnimator rotateAnimationRight;
    private boolean isPlaying = false;

    private View.OnClickListener onClickListener = v -> {
        switch (v.getId()) {
            case R.id.img_close:
                onBackPressed();
                break;
            case R.id.img_play:
                if (isPlaying)
                    stop();
                else
                    play();
                isPlaying = !isPlaying;
                break;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);

        imgLeftWheel = (ImageView) findViewById(R.id.img_left_wheel);
        imgRightWheel = (ImageView) findViewById(R.id.img_right_wheel);
        imgPlay = (ImageView) findViewById(R.id.img_play);

        findViewById(R.id.img_close).setOnClickListener(onClickListener);
        findViewById(R.id.img_next).setOnClickListener(onClickListener);
        findViewById(R.id.img_last).setOnClickListener(onClickListener);
        txtTitle = (TextView) findViewById(R.id.txt_title);
        txtTitle.setSingleLine();
        txtTitle.setSelected(true);
        imgPlay.setOnClickListener(onClickListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (subscriptionRotateAnim!=null && !subscriptionRotateAnim.isUnsubscribed())
            subscriptionRotateAnim.unsubscribe();
        subscriptionRotateAnim = null;
        if (rotateAnimationRight!=null)
            rotateAnimationRight.cancel();
        if (rotateAnimationLeft!=null)
            rotateAnimationLeft.cancel();
        finish();
    }

    private void play() {
        subscriptionRotateAnim = Observable.just(1)
                .delay(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .map(i -> {
                    if (rotateAnimationLeft==null) {
                        rotateAnimationLeft = ObjectAnimator.ofFloat(imgLeftWheel, "rotation", 0.0f, 360.0f);
                        rotateAnimationLeft.setDuration(1000*6);
                        rotateAnimationLeft.setInterpolator(new LinearInterpolator());
                        rotateAnimationLeft.setRepeatCount(-1);
                        rotateAnimationLeft.setRepeatMode(ObjectAnimator.RESTART);
                        rotateAnimationLeft.start();
                    }
                    else
                        rotateAnimationLeft.resume();
                    return i;
                })
                .delay(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> {
                    if (rotateAnimationRight==null) {
                        rotateAnimationRight = ObjectAnimator.ofFloat(imgRightWheel, "rotation", 0.0f, 360.0f);
                        rotateAnimationRight.setDuration(1000*6);
                        rotateAnimationRight.setInterpolator(new LinearInterpolator());
                        rotateAnimationRight.setRepeatCount(-1);
                        rotateAnimationRight.setRepeatMode(ObjectAnimator.RESTART);
                        rotateAnimationRight.start();
                    }
                    else
                        rotateAnimationRight.resume();
                });
        imgPlay.setImageResource(R.drawable.pause_icon);
    }

    private void stop() {
        subscriptionRotateAnim = Observable.just(1)
                .delay(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .map(i -> {
                    rotateAnimationLeft.pause();
                    return i;
                })
                .delay(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> rotateAnimationRight.pause());
        imgPlay.setImageResource(R.drawable.play_icon);
    }
}
