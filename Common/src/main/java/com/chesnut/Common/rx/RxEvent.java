package com.chesnut.Common.rx;

/**
 * Created by Chestnut on 2016/8/24.
 */
public class RxEvent {

    private int receiveType;
    private int eventType;
    private String eventAction;  //
    private Object event;        //事件体

    public RxEvent() {
    }

    /**
     * RxBus 事件
     * @param receiveType   接收类型
     * @param eventType     事件类型
     * @param eventAction   事件Action
     * @param event         事件
     */
    public RxEvent(int receiveType, int eventType, String eventAction, Object event) {
        this.receiveType = receiveType;
        this.eventType = eventType;
        this.eventAction = eventAction;
        this.event = event;
    }

    public int getReceiveType() {
        return receiveType;
    }

    public int getEventType() {
        return eventType;
    }

    public String getEventAction() {
        return eventAction;
    }

    public Object getEvent() {
        return event;
    }

    @Override
    public String toString() {
        return "RxEvent{" +
                "receiveType=" + receiveType +
                ", eventType=" + eventType +
                ", eventAction='" + eventAction + '\'' +
                ", event=" + event +
                '}';
    }
}
