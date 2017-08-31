package com.chestnut.RouterArchitecture.ModulesCommon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.SimpleAdapter;
import com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.helper.CardScaleHelper;
import com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.item.GalleryItem;

public class TimeLineActivity extends AppCompatActivity {

    private boolean OpenLog = true;
    private String TAG = "TimeLineActivity";

    private int mCurrentItemOffset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        //Item 滑动最终居中停止
        //new LinearSnapHelper().attachToRecyclerView(recyclerView);

        SimpleAdapter simpleAdapter = new SimpleAdapter();
        recyclerView.setAdapter(simpleAdapter);

        simpleAdapter.add(new GalleryItem(""));
        simpleAdapter.add(new GalleryItem(""));
        simpleAdapter.add(new GalleryItem(""));
        simpleAdapter.add(new GalleryItem(""));

        CardScaleHelper mCardScaleHelper = new CardScaleHelper();
        mCardScaleHelper.setScale(0.8f);
        mCardScaleHelper.setPagePadding(200);
        mCardScaleHelper.attachToRecyclerView(recyclerView);
    }
}
