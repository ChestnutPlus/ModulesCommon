package com.chestnut.RouterArchitecture.ModulesCommon.fun.lottieAnimationViewAndVLayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.FloatLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.chestnut.RouterArchitecture.ModulesCommon.R;
import com.chestnut.RouterArchitecture.ModulesCommon.fun.lottieAnimationViewAndVLayout.vLayout.FloatAdapter;
import com.chestnut.RouterArchitecture.ModulesCommon.fun.lottieAnimationViewAndVLayout.vLayout.LinearAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LottieAnimationViewAndVLayoutTestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie_animation_v_layout);
        LottieAnimationView lottieAnimationView = (LottieAnimationView) findViewById(R.id.animation_view);
        lottieAnimationView.playAnimation();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //绑定VirtualLayoutManager
        VirtualLayoutManager layoutManager = new VirtualLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //设置Adapter列表
        List<DelegateAdapter.Adapter> adapters = new LinkedList<>();

        //线性布局
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        int itemCount = 5;
        linearLayoutHelper.setItemCount(itemCount);     //设置Item个数
        linearLayoutHelper.setDividerHeight(2);         //设置间隔高度
        linearLayoutHelper.setMarginBottom(100);        //设置下边距
        //制作假数据：
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            strings.add("线性布局:"+i);
        }
        adapters.add(new LinearAdapter(linearLayoutHelper,strings));

        //浮动布局
        FloatLayoutHelper floatLayoutHelper = new FloatLayoutHelper();
        floatLayoutHelper.setDefaultLocation(0,250);
        //制作假数据：
        List<Integer> integers = new ArrayList<>();
        integers.add(R.drawable.li_bao_en);
        adapters.add(new FloatAdapter(floatLayoutHelper,integers));

        //绑定delegateAdapter
        DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager);
        delegateAdapter.setAdapters(adapters);
        recyclerView.setAdapter(delegateAdapter);
    }
}
