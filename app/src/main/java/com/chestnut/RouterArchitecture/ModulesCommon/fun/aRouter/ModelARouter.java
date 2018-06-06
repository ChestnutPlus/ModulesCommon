package com.chestnut.RouterArchitecture.ModulesCommon.fun.aRouter;

import android.app.Activity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chestnut.RouterArchitecture.ModulesCommon.base.CommonContract;
import com.chestnut.RouterArchitecture.ModulesCommon.view.recyclerView.SimpleAdapter;
import com.chestnut.RouterArchitecture.ModulesCommon.view.recyclerView.item.TxtItem;
import com.chestnut.common.ui.XToast;
import com.chestnut.common.utils.LogUtils;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/2/6 12:52
 *     desc  :
 *     thanks To:
 *          1.  https://blog.csdn.net/zhaoyanjun6/article/details/76165252
 *     dependent on:
 *     update log:
 * </pre>
 */
public class ModelARouter implements CommonContract{
    @Override
    public void onModelTest(SimpleAdapter simpleAdapter, XToast toast, String TAG, Activity activity) {
        TxtItem t1 = new TxtItem("ModelARouter");
        t1.setCallback(s -> {
            toast.setText(s).show();
            LogUtils.i(TAG,s);
            ARouter.openLog();                       // 打印日志
            ARouter.openDebug();                     // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.init(activity.getApplication()); // 尽可能早，推荐在Application中初始化

            //1. 无参数跳转
//            ARouter.getInstance().build("/router/ARouterOneActivity").navigation();

            //2. 监听跳转状态
//            ARouter.getInstance().build("/router/abc").navigation(activity, new NavCallback() {
//                @Override
//                public void onArrival(Postcard postcard) {
//                    LogUtils.i(TAG,"onArrival");
//                }
//
//                @Override
//                public void onFound(Postcard postcard) {
//                    LogUtils.i(TAG,"onFound");
//                }
//
//                @Override
//                public void onLost(Postcard postcard) {
//                    LogUtils.i(TAG,"onLost");
//                }
//
//                @Override
//                public void onInterrupt(Postcard postcard) {
//                    LogUtils.i(TAG,"postcard");
//                }
//            });

            //3. 带参数跳转
            ARouter.getInstance().build("/router/ARouterOneActivity")
                    //基本类型
                    .withString("name", "888")
                    .withInt("age", 11)
                    //复杂对象
                    .withParcelable("abc", new SomeBean())
                    .navigation();

        });
        simpleAdapter.add(t1);
    }
}
