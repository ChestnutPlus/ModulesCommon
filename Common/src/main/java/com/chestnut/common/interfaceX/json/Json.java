package com.chestnut.common.interfaceX.json;

import android.support.annotation.NonNull;

import java.util.List;

public interface Json {
    <Bean> Bean getBean(@NonNull String json,@NonNull Class<Bean> beanClass);
    <Bean> List<Bean> getListBean(@NonNull String json,@NonNull Class<Bean> beanClass);
    <Bean> String toJson(@NonNull Bean bean);
}
