package com.chestnut.RouterArchitecture.ModulesCommon.fun.testBRVAH;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chestnut.RouterArchitecture.ModulesCommon.R;

import java.util.ArrayList;
import java.util.List;

public class BRVAHActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brvah);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //data
        List<BrvahBean> brvahBeans = new ArrayList<>();
        int[] ints = {
            R.drawable.beauty1,
            R.drawable.beauty2,
            R.drawable.beauty3,
            R.drawable.beauty4,
        };
        String[] strings = {
                "王尼玛",
                "草泥马",
                "雅蠛蝶",
                "法克鱿",
        };
        for (int i = 0; i < 30; i++) {
            BrvahBean brvahBean = new BrvahBean();
            brvahBean.img = ints[(int) (System.nanoTime()%ints.length)];
            brvahBean.str = strings[(int) (System.nanoTime()%strings.length)];
            brvahBeans.add(brvahBean);
        }
        //bind
        MyAdapter mAnimationAdapter = new MyAdapter(brvahBeans);
        //开启item出现的动画，默认有5种
        mAnimationAdapter.openLoadAnimation();
        mAnimationAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAnimationAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAnimationAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        mAnimationAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mAnimationAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        mAnimationAdapter.setNotDoAnimationCount(3);
        mAnimationAdapter.setOnItemChildClickListener((adapter, view, position) -> {

        });
        mRecyclerView.setAdapter(mAnimationAdapter);
    }
}
