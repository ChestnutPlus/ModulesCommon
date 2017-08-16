package com.chestnut.RouterArchitecture.ModulesCommon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chestnut.Common.rx.RxUtils;
import com.chestnut.Common.utils.LogUtils;
import com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.SimpleAdapter;
import com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.item.Item1;
import com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.item.Item2;

import java.util.concurrent.TimeUnit;

public class TimeLineActivity extends AppCompatActivity {

    private boolean OpenLog = true;
    private String TAG = "TimeLineActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SimpleAdapter simpleAdapter = new SimpleAdapter();
        recyclerView.setAdapter(simpleAdapter);

        for (int i = 0; i < 10; i++) {
            Item1 item1 = new Item1(null);
            item1.setOnItemListener((view, position) -> {
                LogUtils.e(true,"position:"+position);
            });
            simpleAdapter.add(item1);
        }
        simpleAdapter.add(new Item1(null));
        simpleAdapter.add(new Item2(null));
        simpleAdapter.add(new Item2(null));
        simpleAdapter.add(new Item2(null));
        simpleAdapter.add(new Item2(null));
        simpleAdapter.add(new Item2(null));
        simpleAdapter.add(new Item2(null));
        simpleAdapter.add(new Item2(null));
        simpleAdapter.add(new Item2(null));
        simpleAdapter.add(new Item2(null));


        RxUtils.countClickNum(findViewById(R.id.txt_more),1000)
                .subscribe(integer -> {
                    LogUtils.i(OpenLog,TAG,"num:"+integer);
                });

        RxUtils.filterClick(findViewById(R.id.img_arrow_back),1500, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    LogUtils.i(OpenLog,TAG,"..2");
                });
    }
}
