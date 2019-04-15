package com.kute.webflux.rxjava;

import io.reactivex.Flowable;
import org.junit.Test;

/**
 * created by bailong001 on 2019/04/10 14:04
 */
public class RxJavaTest {

    @Test
    public void basic() {

        Flowable.just(1, "a")
                .subscribe(System.out::println);

    }

}
