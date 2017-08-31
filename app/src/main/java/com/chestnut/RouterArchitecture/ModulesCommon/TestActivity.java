package com.chestnut.RouterArchitecture.ModulesCommon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.SimpleAdapter;
import com.chestnut.RouterArchitecture.ModulesCommon.RecyclerView.item.GalleryItem;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        DiscreteScrollView scrollView = (DiscreteScrollView) findViewById(R.id.picker);
        SimpleAdapter simpleAdapter = new SimpleAdapter();
        simpleAdapter.add(new GalleryItem(""));
        simpleAdapter.add(new GalleryItem(""));
        simpleAdapter.add(new GalleryItem(""));
        simpleAdapter.add(new GalleryItem(""));
        scrollView.setAdapter(simpleAdapter);
        scrollView.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.0f)
                .setMinScale(0.7f)
                .build());
    }
}
