package com.kute.webflux.listener;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * created by kute on 2019/04/30 11:16
 */
public interface MyListener {

    void onEvent(MyEvent myEvent);

    void onError(Throwable throwable);

    void onCompleted();

    @Data
    @Accessors(chain = true)
    class MyEvent {
        String data;
    }

}
