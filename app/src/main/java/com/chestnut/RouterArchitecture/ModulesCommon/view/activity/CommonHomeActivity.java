package com.chestnut.RouterArchitecture.ModulesCommon.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.chestnut.RouterArchitecture.ModulesCommon.R;
import com.chestnut.RouterArchitecture.ModulesCommon.base.ViewConfig;
import com.chestnut.RouterArchitecture.ModulesCommon.view.recyclerView.SimpleAdapter;
import com.chestnut.RouterArchitecture.ModulesCommon.view.recyclerView.item.TxtItem;
import com.chestnut.common.helper.si.XFontHelper;
import com.chestnut.common.ui.XToast;

public class CommonHomeActivity extends AppCompatActivity {

    private String TAG = "CommonHomeActivity";
    private XToast toast;
    private RecyclerView recyclerView;
    private SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_home);
        toast = new XToast(this, Toast.LENGTH_LONG);
        toast.setTextTypeface(XFontHelper.getInstance().get(ViewConfig.TypeFace_Cao_Ni_Ma));
        toast.setTextSize(18);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        simpleAdapter = new SimpleAdapter();

        for (int i = 0; i < 10; i++) {
            simpleAdapter.add(new TxtItem("王尼玛"));
        }

        recyclerView.setAdapter(simpleAdapter);
    }
}
