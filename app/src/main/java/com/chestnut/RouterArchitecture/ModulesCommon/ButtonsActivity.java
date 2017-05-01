package com.chestnut.RouterArchitecture.ModulesCommon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.chestnut.Common.ui.Toastc;
import com.chestnut.Common.utils.LogUtils;
import com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.Base.Item;
import com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.ButtonsAdapter;
import com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.SimpleAdapter;
import com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.TestItem;
import com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.TxtItem;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class ButtonsActivity extends AppCompatActivity {

    private XRecyclerView xRecyclerView = null;
    private Toastc toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttons);
        toast = new Toastc(this, Toast.LENGTH_LONG);
        xRecyclerView = (XRecyclerView) findViewById(R.id.xRecyclerView);
//        ButtonsAdapter buttonsAdapter = new ButtonsAdapter(messages,buttonsTitles,this);
//        buttonsAdapter.setOnItemListener(onItemListener);

        final SimpleAdapter simpleAdapter = new SimpleAdapter();
        TestItem testItem = new TestItem("TestItem");
        testItem.setOnItemListener(new Item.OnItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                simpleAdapter.add(new TxtItem("position:"+position));
            }
        });
        simpleAdapter.add(testItem);
        simpleAdapter.add(new TxtItem("ah"));

        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.setAdapter(simpleAdapter);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                LogUtils.wD("onRefresh");
                //xRecyclerView.loadMoreComplete();
                Observable.just(1)
                        .delay(1000, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                xRecyclerView.refreshComplete();
                            }
                        });
            }

            @Override
            public void onLoadMore() {
                LogUtils.wD("onLoadMore");
                //xRecyclerView.refreshComplete();
                Observable.just(1)
                        .delay(1000, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                xRecyclerView.loadMoreComplete();
                            }
                        });
            }
        });
    }

    private String[] messages = {
            "1",
            "2",
            "3",
            "4",
    };

    private String[] buttonsTitles = {
            "1",
            "2",
            "3",
            "4",
    };

    private ButtonsAdapter.OnItemListener onItemListener = new ButtonsAdapter.OnItemListener() {
        @Override
        public void onItemClick(View view, int position) {
            toast.setText("position:"+position).show();
            switch (position) {
                case 0:
                    break;
                case 1:
                    break;
            }
        }
    };
}
