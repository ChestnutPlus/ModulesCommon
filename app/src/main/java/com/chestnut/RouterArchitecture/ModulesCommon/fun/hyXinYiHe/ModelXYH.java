package com.chestnut.RouterArchitecture.ModulesCommon.fun.hyXinYiHe;

import android.app.Activity;
import android.content.Context;

import com.chestnut.RouterArchitecture.ModulesCommon.base.CommonContract;
import com.chestnut.RouterArchitecture.ModulesCommon.view.recyclerView.SimpleAdapter;
import com.chestnut.RouterArchitecture.ModulesCommon.view.recyclerView.item.TxtItem;
import com.chestnut.common.ui.XToast;
import com.chestnut.common.utils.LogUtils;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/2/6 15:11
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class ModelXYH implements CommonContract{
    @Override
    public void onModelTest(SimpleAdapter simpleAdapter, XToast toast, String TAG, Activity activity) {
        TxtItem t1 = new TxtItem("Hy-XinYiHe");
        t1.setCallback(s -> {
            toast.setText(s).show();
            LogUtils.i(TAG,s);
            ArrayList<Object> appBeanArrayList = GetMenuDynaInfoAppInAct(activity,null);
            if (appBeanArrayList!=null)
                LogUtils.i(TAG,appBeanArrayList.toString());
        });
        simpleAdapter.add(t1);
    }

    public ArrayList<Object> GetMenuDynaInfoAppInAct(Context context, String s) {
        File appConfig = new File("/data/appconfig.json");
        if (appConfig.exists() && appConfig.isFile()) {
            try {
                //读取文件内容
                String encoding="UTF-8";
                InputStreamReader read = new InputStreamReader(new FileInputStream(appConfig),encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                StringBuilder stringBuffer = new StringBuilder();
                String lineTxt;
                while((lineTxt = bufferedReader.readLine()) != null){
                    stringBuffer.append(lineTxt);
                }
                read.close();
                //JSON Mapping
                Gson gson = new Gson();
                AppConfigBean appConfigBean = gson.fromJson(stringBuffer.toString(),AppConfigBean.class);
                if (appConfigBean!=null && appConfigBean.data!=null && appConfigBean.data.size()>0) {
                    //转换成 HyBean
                    ArrayList<Object> hyAppBeanArrayList = new ArrayList<>();
                    for (AppConfigBean.DataBean dataBean : appConfigBean.data) {
                        hyAppBeanArrayList.add(HyAppBean.change(dataBean,context));
                    }
                    return hyAppBeanArrayList;
                }
                else
                    return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        else
            return null;
    }
}
