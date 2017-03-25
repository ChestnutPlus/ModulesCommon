package com.chesnut.Common.Interface.json;

import java.util.List;

/**
 * Created by Chestnut on 2016/12/9.
 */

public interface Json {
    <Bean> Bean getBean(String json, Class<Bean> beanClass);
    <Bean> List<Bean> getListBean(String json, Class<Bean> beanClass);
}
