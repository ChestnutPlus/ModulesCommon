package com.chestnut.common.helper.def;


import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/1/15 14:02
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class SubscriptionHelper {

    private List<Subscription> subscriptionList = new ArrayList<>();

    public SubscriptionHelper add(Subscription subscription) {
        if (subscription!=null && !isExist(subscription))
            subscriptionList.add(subscription);
        return this;
    }

    public SubscriptionHelper remove(Subscription subscription) {
        if (subscription!=null && !isExist(subscription))
            subscriptionList.remove(subscription);
        return this;
    }

    public boolean isExist(Subscription subscription) {
        return subscription != null && subscriptionList.contains(subscription);
    }

    public int getSubscriptionSize() {
        return subscriptionList.size();
    }

    public void unSubscribe(Subscription subscription) {
        if (subscription!=null && !isExist(subscription))
            subscriptionList.remove(subscription);
        if (subscription!=null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
        subscription = null;
    }

    public void unSubscribeAll() {
        for (int i = 0; i < subscriptionList.size(); i++) {
            unSubscribe(subscriptionList.get(i));
        }
        subscriptionList.clear();
    }
}
