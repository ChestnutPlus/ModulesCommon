package com.chestnut.Common.Interface.json;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by Chestnut on 2016/12/9.
 */

public interface Json {
    <Bean> Bean getBean(@NonNull String json,@NonNull Class<Bean> beanClass);
    <Bean> List<Bean> getListBean(@NonNull String json,@NonNull Class<Bean> beanClass);
}
