package com.kute.webflux.listener;

import org.assertj.core.util.Lists;

import java.util.List;

/**
 * created by bailong001 on 2019/04/30 11:14
 */
public class MyListenerContainer {

    List<MyListener> listenerList;

    public MyListenerContainer() {
        this.listenerList = Lists.newArrayList();
    }

    public void registListener(MyListener myListener) {
        listenerList.add(myListener);
    }

    public void consumeEvent(MyListener.MyEvent myEvent) {
        listenerList.forEach(myListener -> {
            try{
                myListener.onEvent(myEvent);
            } catch(Throwable throwable){
                myListener.onError(throwable);
            }
        });
    }
    public void stoped() {
        listenerList.forEach(MyListener::onCompleted);
    }
}
