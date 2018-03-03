package com.chestnut.RouterArchitecture.ModulesCommon.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import com.chestnut.RouterArchitecture.ModulesCommon.R;
import com.chestnut.RouterArchitecture.ModulesCommon.base.ViewConfig;
import com.chestnut.RouterArchitecture.ModulesCommon.fun.diySurfaceView.ModelSurfaceView;
import com.chestnut.RouterArchitecture.ModulesCommon.fun.helperCamera.ModelCameraHelper;
import com.chestnut.RouterArchitecture.ModulesCommon.fun.hyBluetoothPlay.ModelBluetoothRecordPlay;
import com.chestnut.RouterArchitecture.ModulesCommon.fun.hyMarket.ModelHyMarket;
import com.chestnut.RouterArchitecture.ModulesCommon.fun.hyXinYiHe.ModelXYH;
import com.chestnut.RouterArchitecture.ModulesCommon.fun.lottieAnimationViewAndVLayout.ModelLottieAnimationViewAndVLayout;
import com.chestnut.RouterArchitecture.ModulesCommon.fun.oemHwl.ModelOemHwl;
import com.chestnut.RouterArchitecture.ModulesCommon.fun.retrofit.ModelRetrofit;
import com.chestnut.RouterArchitecture.ModulesCommon.fun.rx2.ModelRx2;
import com.chestnut.RouterArchitecture.ModulesCommon.fun.tSnackBar.ModelTSnackBar;
import com.chestnut.RouterArchitecture.ModulesCommon.view.recyclerView.SimpleAdapter;
import com.chestnut.common.helper.si.RxBus;
import com.chestnut.common.helper.si.XFontHelper;
import com.chestnut.common.ui.XToast;
import com.chestnut.common.utils.AppUtils;
import com.chestnut.common.utils.LogUtils;
import com.chestnut.common.utils.ScreenUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class CommonHomeActivity extends AppCompatActivity {

    private XToast toast;
    private SimpleAdapter simpleAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_home);
        XFontHelper.getInstance().setActivityFont(this,ViewConfig.TypeFace_HK);
        toast = new XToast(this, Toast.LENGTH_LONG);
        toast.setTextTypeface(XFontHelper.getInstance().get(ViewConfig.TypeFace_HK));
        toast.setTextSize(15);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(ScreenUtils.getScreenHeight_Dip(this)/50,StaggeredGridLayoutManager.HORIZONTAL));
        simpleAdapter = new SimpleAdapter();
        initData();
        recyclerView.setAdapter(simpleAdapter);
        RxBus.getInstance().listen(String.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    toast.setText(s).show();
                    LogUtils.i("CommonHomeActivity",s);
                });
    }

    private void initData() {
        String TAG = "CommonHomeActivity";
        //测试阿里vLayout和lottieAnimation
        new ModelLottieAnimationViewAndVLayout().onModelTest(simpleAdapter,toast, TAG,this);
        //测试小哈应用市场的卸载，安装
        new ModelHyMarket().onModelTest(simpleAdapter,toast, TAG,this);
        //Oem-好未来
        new ModelOemHwl().onModelTest(simpleAdapter,toast, TAG,this);
        //Diy-SurfaceView帧动画
        new ModelSurfaceView().onModelTest(simpleAdapter,toast, TAG,this);
        //BlueTooth录放
        new ModelBluetoothRecordPlay().onModelTest(simpleAdapter,toast, TAG,this);
        //Op-Retrofit
        new ModelRetrofit().onModelTest(simpleAdapter,toast, TAG,this);
        //Hy-XinYiHe
        new ModelXYH().onModelTest(simpleAdapter,toast, TAG,this);
        //View-SnackBar
        new ModelTSnackBar().onModelTest(simpleAdapter,toast, TAG,this);
        //RxJava2
        new ModelRx2().onModelTest(simpleAdapter,toast, TAG,this);
        //CameraHelper测试
        new ModelCameraHelper().onModelTest(simpleAdapter,toast, TAG,this);
    }

    @Override
    public void onBackPressed() {
        AppUtils.pressTwiceExitApp(this,false,2000, new AppUtils.ExitAppCallBack() {
            @Override
            public void firstAsk() {
                LogUtils.i("CommonHomeActivity","firstAsk");
                toast.setText("再按一次就退出啦~").show();
            }

            @Override
            public void beginExit() {
                LogUtils.i("CommonHomeActivity","beginExit");
            }
        });
    }
}
