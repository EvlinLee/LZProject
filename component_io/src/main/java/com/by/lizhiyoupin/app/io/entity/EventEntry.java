package com.by.lizhiyoupin.app.io.entity;

/**
 * (Hangzhou) <br/>
 *
 * @author: wzm<br />
 * @date :  2018/7/31 20:25 </br>
 * Summary: EventBus 事件
 *  EventBus.getDefault().post(new EventEntry<PostFlagEvent>(EventEntry.EVENT_POWER_VERTICAL_CODE, new PostFlagEvent(true)));
 */
public class EventEntry<E> {
    public static final int EVENT_POWER_VERTICAL_CODE = 100;

    private int eventCode;
    private E e;


    public EventEntry(int eventCode, E e) {
        this.eventCode = eventCode;
        this.e = e;
    }

    public int getEventCode() {
        return eventCode;
    }

    public void setEventCode(int eventCode) {
        this.eventCode = eventCode;
    }

    public E getE() {
        return e;
    }

    public void setE(E e) {
        this.e = e;
    }
}
