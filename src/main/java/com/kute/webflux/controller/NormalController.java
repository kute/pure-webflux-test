package com.kute.webflux.controller;

import io.vavr.control.Try;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * created by bailong001 on 2019/04/14 10:24
 */
@RestController
@RequestMapping("/normal")
public class NormalController {

    /**
     * 正常 阻塞请求
     *
     * @param seconds
     * @return
     */
    @GetMapping("/latency/{seconds}")
    public String latency(@PathVariable Long seconds) {
        Try.run(() -> TimeUnit.SECONDS.sleep(seconds));
        return "normal/latency";
    }
}
