package com.chestnut.RouterArchitecture.ModulesCommon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chestnut.Common.ui.RecyclerView.Base.BaseItem;
import com.chestnut.Common.ui.Toastc;
import com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.ButtonBean;
import com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.ButtonItem;
import com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.SimpleAdapter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class ButtonsActivity extends AppCompatActivity {

    private RecyclerView recyclerView = null;
    private Toastc toast = null;
    private MaterialRefreshLayout refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttons);
        toast = new Toastc(this, Toast.LENGTH_LONG);
        recyclerView = (RecyclerView) findViewById(R.id.LRecyclerView);
        refresh = (MaterialRefreshLayout) findViewById(R.id.refresh);
        refresh.setLoadMore(true);
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                //下拉刷新...
                Observable.just(1)
                        .delay(1000, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                refresh.finishRefresh();
                            }
                        });
            }
            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                //上拉刷新...
                Observable.just(1)
                        .delay(1000, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                refresh.finishRefreshLoadMore();
                            }
                        });
            }
        });
        SimpleAdapter simpleAdapter = new SimpleAdapter();
        setButtons(simpleAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(simpleAdapter);
    }

    private void setButtons(SimpleAdapter simpleAdapter) {
        //测试1：
        ButtonItem buttonItem = new ButtonItem(new ButtonBean("用于测试的一个按钮！","测试按钮-1"));
        buttonItem.setOnItemListener(new BaseItem.OnItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                toast.setText(position+"").show();
            }
        });
        simpleAdapter.add(buttonItem);
    }
}
