package com.chestnut.RouterArchitecture.ModulesCommon.fun.aRouter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.chestnut.RouterArchitecture.ModulesCommon.R;
import com.chestnut.common.utils.LogUtils;

@Route(path = "/router/ARouterOneActivity")
public class ARouterOneActivity extends AppCompatActivity {

    @Autowired
    public String name;

    @Autowired(name = "age")
    public int agedfsfd = -111;

//    @Autowired(name = "abc")
//    public SomeBean someBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arouter_one);

        LogUtils.i("ARouterOneActivity", name+","+agedfsfd+",");
    }
}
