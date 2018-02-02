package com.chestnut.RouterArchitecture.ModulesCommon;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chestnut.RouterArchitecture.ModulesCommon.retrofit.AppListBean;
import com.chestnut.RouterArchitecture.ModulesCommon.retrofit.GetAppList;
import com.chestnut.common.helper.MediaPlayerHelper;
import com.chestnut.common.ui.XToast;
import com.chestnut.common.utils.AppUtils;
import com.chestnut.common.utils.LogUtils;
import com.chestnut.common.utils.XFontUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends RxAppCompatActivity {

    private String TAG = "MainActivity";
    private boolean OpenLog = true;
    private XToast toast;
    ImageView img1;
    ImageView img2;
    ImageView img3;
    ImageView img4;
    TextView txtLog;
    SeekBar seekBar1;
    SeekBar seekBar2;
    List<String> logs;

    int btnIds[] = {
            R.id.btn_1,
            R.id.btn_2,
            R.id.btn_3,
            R.id.btn_4,
            R.id.btn_5,
            R.id.btn_6,
            R.id.btn_7,
            R.id.btn_8,
            R.id.btn_9,
            R.id.btn_10,
            R.id.btn_11,
            R.id.btn_12,
    };

    String toastAndBtnName[] = {
            "1_"+"PayTest",
            "2_"+"蓝牙",
            "3_"+"floatView",
            "4_"+"OemHWLActivity",
            "5_"+"DemoInstallUninstallActivity",
            "6_"+"",
            "7_"+"",
            "8_"+"",
            "9_"+"",
            "10_"+"",
            "11_"+"",
            "12_"+"",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XFontUtils.getInstance().activitySetFont(this,"fonts/Test.TTF");
        logs = new ArrayList<>();

        img1 = (ImageView) findViewById(R.id.img_1);
        img2 = (ImageView) findViewById(R.id.img_2);
        img3 = (ImageView) findViewById(R.id.img_3);
        img4 = (ImageView) findViewById(R.id.img_4);

        seekBar1 = (SeekBar) findViewById(R.id.seekBar_1);
        seekBar2 = (SeekBar) findViewById(R.id.seekBar_2);
        toast = new XToast(this, Toast.LENGTH_LONG);
        toast.setTextTypeface(XFontUtils.getInstance().get("fonts/caonima.ttf"));
        toast.setTextSize(20);

        TextView textView = null;
        txtLog = (TextView) findViewById(R.id.txt_log);
        for (int i = 0; i < btnIds.length; i++) {
            textView = (TextView) findViewById(btnIds[i]);
            textView.setText(toastAndBtnName[i]);
            textView.setOnClickListener(onClickListener);
            textView.setTag(i);
        }

        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                toast.setText("1:"+seekBar.getProgress()).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                toast.setText("1:"+seekBar.getProgress()).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                toast.setText("2:"+seekBar.getProgress()).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                toast.setText("2:"+seekBar.getProgress()).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void viewLog(String TAG,String msg) {
        if (logs.size()>=6)
            logs.remove(0);
        logs.add(TAG+"\t\t"+msg);
        StringBuilder result = new StringBuilder();
        for (String s :
                logs) {
            result.append(s).append("\n");
        }
        txtLog.setText(result.toString());
    }

    private String url = "http://windowserl.honeybot.cn:8080/Market/getAppList?pageIndex=0&pageSize=8";

    private View.OnClickListener onClickListener = view -> {
        toast.setText(toastAndBtnName[(int) view.getTag()]).show();
        LogUtils.i(OpenLog,TAG,"btn-info:"+toastAndBtnName[(int) view.getTag()]);
        viewLog(TAG,toastAndBtnName[(int) view.getTag()]);
        switch (view.getId()) {
            case R.id.btn_1:
                startActivity(new Intent(this,PayActivity.class));
                break;
            case R.id.btn_2:
                startActivity(new Intent(this,RecordPlayActivity.class));
                break;
            case R.id.btn_3:
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setImageResource(R.mipmap.gift_icon);
                imageView.setOnClickListener(v -> toast.setText("img").show());
                FloatWindow.with(getApplicationContext())
                        .setView(imageView)
                        .setX(1144)
                        .setY(160)
                        .setIsRecordXY()
                        .setMoveType(MoveType.slide)
                        .setMoveStyle(300,null)
                        .build();
                break;
            case R.id.btn_4:
                startActivity(new Intent(this,OemHWLActivity.class));
                break;
            case R.id.btn_5:
                startActivity(new Intent(this,DemoInstallUninstallActivity.class));
                break;
            case R.id.btn_6:
                break;
            case R.id.btn_7:
                break;
            case R.id.btn_8:
                break;
            case R.id.btn_9:
                break;
            case R.id.btn_10:
                break;
            case R.id.btn_11:

                MediaPlayerHelper mediaPlayerHelper = new MediaPlayerHelper();
                mediaPlayerHelper.init(this);
                mediaPlayerHelper.setCallBack(new MediaPlayerHelper.MediaPlayerHelperListener() {
                    @Override
                    public void onStart(MediaPlayer mediaPlayer, int allSecond) {
                        super.onStart(mediaPlayer, allSecond);
                    }
                });

                break;
            case R.id.btn_12:
                //声明缓存地址和大小
                Cache cache = new Cache(this.getCacheDir(),10*1024*1024);
                //构建 Client
                OkHttpClient client = new OkHttpClient.Builder()
                        .retryOnConnectionFailure(true)
                        //addInterceptor()添加的是应用拦截器，他只会在response被调用一次。
//                        .addInterceptor(new Interceptor() {
//                            @Override
//                            public Response intercept(Chain chain) throws IOException {
//                                Request request = chain.request();
//                                Log.i(TAG,"0");
//                                if (!NetworkUtils.isConnected(MainActivity.this)) {
//                                    Log.i(TAG,"1,not-net-work");
//                                    request = request.newBuilder()
//                                            .cacheControl(CacheControl.FORCE_CACHE)
//                                            .build();
//                                    return chain.proceed(request)
//                                            .newBuilder()
//                                            .header("Cache-Control", "public, only-if-cached, max-stale=" + 30)
//                                            .removeHeader("Pragma")
//                                            .build();
//                                }
//                                else
//                                    return chain.proceed(request);
//                            }
//                        })
//                        .addNetworkInterceptor(new Interceptor() {
//                            @Override
//                            public Response intercept(Chain chain) throws IOException {
//                                Request request = chain.request();
//                                Response response = chain.proceed(request);
//                                Log.i(TAG,"3");
//                                if (NetworkUtils.isConnected(MainActivity.this)) {
//                                    int maxAge = 20;
//                                    // 有网络时 设置缓存超时时间0个小时
//                                    Log.i(TAG,"4");
//                                    response.newBuilder()
//                                            .header("Cache-Control", "public, max-age=" + maxAge)
//                                            .removeHeader("Pragma")
//                                            .build();
//                                } else {
//                                    // 无网络时，设置超时为1周
//                                    int maxStale = 30;
//                                    Log.i(TAG,"5");
//                                    response.newBuilder()
//                                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
//                                            .removeHeader("Pragma")
//                                            .build();
//                                }
//                                return response;
//                            }
//                        })
                        //addNetworkInterceptor()添加的是网络拦截器，它会在request和response时分别被调用一次
                        .cache(cache)
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
                // 创建 网络请求接口 的实例
                GetAppList getAppList = retrofit1.create(GetAppList.class);
                Call<AppListBean> appListBeanCall1 = getAppList.get(1,8);
                appListBeanCall1.enqueue(new Callback<AppListBean>() {
                    @Override
                    public void onResponse(Call<AppListBean> call, retrofit2.Response<AppListBean> response) {
                        if (response!=null && response.isSuccessful()) {
                            if (response.body()!=null && response.body().data!=null)
                                for (AppListBean.DataBean d :
                                        response.body().data) {
                                    LogUtils.i(TAG,d.toString());
                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<AppListBean> call, Throwable t) {
                        LogUtils.i(TAG,t.getMessage());
                    }
                });
                break;
        }
    };

    @Override
    public void onBackPressed() {
        AppUtils.pressTwiceExitApp(this,false,2000, new AppUtils.ExitAppCallBack() {
            @Override
            public void firstAsk() {
                LogUtils.i(OpenLog,TAG,"firstAsk");
                toast.setText("再按一次就退出啦~").show();
            }

            @Override
            public void beginExit() {
                LogUtils.i(OpenLog,TAG,"beginExit");
            }
        });
    }
}
