package com.chestnut.RouterArchitecture.ModulesCommon.fun.aRouter;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chestnut.RouterArchitecture.ModulesCommon.R;
import com.chestnut.common.manager.imgloader.ImgLoaderConfig;
import com.chestnut.common.manager.imgloader.ImgLoaderManager;
import com.chestnut.common.manager.imgloader.contract.ImgLoaderListener;
import com.chestnut.common.utils.ConvertUtils;
import com.chestnut.common.utils.LogUtils;

@Route(path = "/router/ARouterOneActivity")
public class ARouterOneActivity extends AppCompatActivity {

    @Autowired
    public String name;

    @Autowired(name = "age")
    public int agedfsfd = -111;

    @Autowired(name = "abc")
    public SomeBean someBean;

    @Autowired(name = "/service/RemoteServiceAgent")
    public RemoteService remoteService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arouter_one);
        ARouter.getInstance().inject(this);
        LogUtils.i("ARouterOneActivity", name+","+agedfsfd+","+someBean.toString());
        remoteService.sayHello();
        remoteService.setCallback(new RemoteService.Callback() {
            @Override
            public void onStart() {
                LogUtils.i("ARouterOneActivity", "onStart");
            }

            @Override
            public void onStop() {
                LogUtils.i("ARouterOneActivity", "onStop");
            }
        });
        ImgLoaderManager.getInstance().load(this, ImgLoaderConfig.builder()
                .from("https://img.zcool.cn/community/019c4558d62ff0a801219c77b3cf16.jpg@1280w_1l_2o_100sh.webp")
                .roundTransformDp(100)
                .to(findViewById(R.id.img))
                .size(200,200)
                .listen(new ImgLoaderListener() {
                    @Override
                    public void onProgress(int progress) {
                        LogUtils.i("ImgLoaderManager", "onProgress, " + progress);
                    }

                    @Override
                    public void onErr() {
                        LogUtils.i("ImgLoaderManager", "onErr");
                    }

                    @Override
                    public void onReady(Drawable drawable) {
                        LogUtils.i("ImgLoaderManager", "onReady");
                    }
                })
                .build());
        LogUtils.i("ImgLoaderManager", "cache, " + ImgLoaderManager.getInstance().getAllCacheSizeBytes(this));
        LogUtils.i("ImgLoaderManager", "cache, " + ConvertUtils.byte2FitSize(ImgLoaderManager.getInstance().getAllCacheSizeBytes(this)));
    }
}
