package com.kute.webflux;

import com.google.common.base.Function;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.*;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * created by bailong001 on 2019/02/19 14:38
 */
public class FluxTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FluxTest.class);

    @Before
    public void before() {
        Hooks.onOperatorDebug();
    }

    @Test
    public void test() {
        Integer[] ary = new Integer[]{1, 2, 3, 4, 5};
        List<Integer> list = Lists.newArrayList(ary);

        Flux.just(5);

        Flux.just(1, 2, 3, 4, 5);

        Flux.fromArray(ary);

        Flux.fromIterable(list);

        Flux.fromStream(list.stream());

        Flux.empty();

        // 产生从0L开始的事件，相隔一段事件
        Flux.interval(Duration.of(10, ChronoUnit.SECONDS));

        // 注意：第一个参数 是起始，第二个是 产生的个数
        Flux.range(1, 5);

        // 自定义发布
        /**
         * 第一个参数：初始状态
         * 第二个参数：生成器
         * 第三个参数：停止生成时，接受的最后一个即将来到的状态
         */
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Flux.generate(() -> 0, (state, sink) -> {
            sink.next("the state:" + state + ", next value:" + atomicInteger.getAndAdd(10));
            if (state == 10) {
                sink.complete();
            }
            return state + 1;
        }, data -> System.out.println("这是最后一个即将来到的状态：" + data)).subscribe(System.out::println);

        Mono.just(5);

        Mono.justOrEmpty(5);

        Mono.justOrEmpty(Optional.ofNullable(5));

        Mono<String> mono = Mono.empty();

        Mono.fromCallable(RandomUtils::nextInt);

    }

    @Test
    public void test1() throws InterruptedException {

        // 当有 subscribe时才产生数据
//        Flux<Integer> flux = Flux.range(6, 5)
//                .map(i -> {
//                    if(RandomUtils.nextInt(1, 10) > 1) {
//                        return i;
//                    }
//                    throw new IllegalStateException("IllegalStateException");
//                });
//
//        Disposable disposable = flux.subscribe(i -> {
//            if (RandomUtils.nextInt(1, 10) > 9) {
//                // 遇异常停止消费
//                throw new RuntimeException("RuntimeException");
//            }
//            LOGGER.info("Consume:{}", i);
//        }, throwable -> {
//            LOGGER.error("Consumer error", throwable);
//        }, () -> {
//            // 当全部成功后调用，遇异常不调用
//            LOGGER.info("Consumer complete");
//        }, subscription -> {
//            // 只消费 n个元素
//            subscription.request(5);
//            // 取消消费
////            subscription.cancel();
//        });
//
//        LOGGER.info("是否被处理过：{}", disposable.isDisposed()); // true

        // 每2s发送一个事件，从 0L 开始递增
//        Flux.interval(Duration.of(2, ChronoUnit.SECONDS)).take(20).subscribe(System.out::println);

        // 先 延迟 2s 再发送，后以每 1s 发送一个事件
//        Disposable disposable1 = Flux.interval(Duration.of(2, ChronoUnit.SECONDS), Duration.of(1, ChronoUnit.SECONDS))
//                .subscribe(System.out::println);

        // 5s后 停止消费
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                LOGGER.info("5s后停止消费");
//                disposable1.dispose();
//            }
//        }, 5000);

        // 取消所有消费
//        Disposables.composite(disposable, disposable1).dispose();

//        Mono.just(1)
//                .concatWith(Mono.just(2))
//                // 延迟消费
//                .delaySubscription(Duration.of(2, ChronoUnit.SECONDS))
//                .subscribe(System.out::println);
//
//        Flux<Integer> flux = Mono.just(1).concatWith(Mono.just(2));

        // toStream 阻塞
//        flux.toStream().forEach(System.out::println);
//        flux.toIterable().forEach(System.out::println);
//        System.out.println(flux.blockFirst());

        // 同步
//        Flux.generate(
//                AtomicInteger::new,
        // 自己维护状态
//                (state, sink) -> {
//                    long i = state.getAndIncrement();
//                    sink.next(i);
//                    if (i == 10) {
//                        sink.complete();
//                    }
//                    return state;
//                }).subscribe(System.out::println);

//        Flux.range(1, 100)
//                .log()
//                // 最大能请求 10 次
//                .limitRequest(10)
//                .subscribe(this::consumer);

//        Flux.interval(Duration.ofMillis(500))
//                .log()
//                // 最大每次只能请求2个
//                .limitRate(2)
//                .subscribe(this::consumer);

//        Flux.range(1, 100).log().limitRate(7, 7).subscribe(this::consumer);

//        Flux<Integer> flux = Flux.range(1, 10).log().concatWith(Flux.error(new RuntimeException("ErrorMessage")));
//
        // 异步
//        StepVerifier.create(flux, StepVerifierOptions.create().initialRequest(10).checkUnderRequesting(true))
//                .expectNextCount(10)
//                .expectError(RuntimeException.class)
//                .verify();

//        Flux.range(1, 100)
//                .map(String::valueOf)
//                // 转换为带index的tuple
//                .index()
//                .collectMap((Function<Tuple2<Long, String>, Long>) Tuple2::getT1, (Function<Tuple2<Long, String>, String>) Tuple2::getT2)
//                .log()
//                .subscribe();

//        CountDownLatch countDownLatch = new CountDownLatch(1);
//        countDownLatch.await();

    }

    @Test
    public void testOnError() {
        Flux.range(1, 8)
                .map(i -> i / (i - 3))
                // 遇到异常时 跳过
//                .onErrorContinue(Exception.class, (throwable, errorElement) -> LOGGER.info("onErrorContinue throwable={}, errorElement={}", throwable.getClass().getName(), errorElement))
                // 异常转换
                .onErrorMap(Exception.class, (Function<Exception, Throwable>) input -> {
                    LOGGER.info("onErrorMap exception={}", input.getClass().getName());
                    if (input instanceof ArithmeticException) {
                        return new IllegalStateException();
                    }
                    return input;
                })
//                .onErrorReturn(99)
                // 遇到异常 提供新的流
                .onErrorResume(Exception.class, (Function<Exception, Publisher<? extends Integer>>) input -> {
                    // 若 onErrorMap 打开，此时的异常是 IllegalStateException
                    LOGGER.info("onErrorResume exception={}", input.getClass().getName());
                    return Flux.range(2, 5);
                })
                .log()
                .subscribe();
    }

    @Test
    public void testRetryRepeat() {
        Flux.range(1, 6)
                .map(i -> i / (i - 3))
//        retry对于上游Flux是采取的重订阅（re-subscribing）的方式，因此重试之后实际上已经一个不同的序列了， 发出错误信号的序列仍然是终止了的
//                .retry()
//                .retry(3)
//                .retry(throwable -> throwable instanceof ArithmeticException)
                .retry(2, throwable -> throwable instanceof ArithmeticException)
//                .log()
//                .repeat()
                .doOnError(throwable -> LOGGER.info("{}", throwable.getClass().getName()))
                .subscribe();
    }

    /**
     * 用于编程方式自定义生成数据流的create和generate等及其变体方法；
     * 用于“无副作用的peek”场景的doOnNext、doOnError、doOncomplete、doOnSubscribe、doOnCancel等及其变体方法；
     * 用于数据流转换的when、and/or、merge、concat、collect、count、repeat等及其变体方法；
     * 用于过滤/拣选的take、first、last、sample、skip、limitRequest等及其变体方法；
     * 用于错误处理的timeout、onErrorReturn、onErrorResume、doFinally、retryWhen等及其变体方法；
     * 用于分批的window、buffer、group等及其变体方法；
     * 用于线程调度的publishOn和subscribeOn方法。
     *
     * @throws InterruptedException
     */
    @Test
    public void testOperator() throws InterruptedException {
        // zip 若两边元素个数不等，则以 少的为主（即 直至 某一个 publisher 先完成）
        Flux.zip(Flux.just(1, 2, 3), Flux.just("a", "b", "c"))
                .log().subscribe();


        CountDownLatch countDownLatch = new CountDownLatch(1);
        Flux.zip(
                Flux.just("a", 'b', "c"),
                Flux.interval(Duration.ofMillis(1000))).subscribe(t -> System.out.println(t.getT1()), null, countDownLatch::countDown);
        countDownLatch.await(10, TimeUnit.SECONDS);

    }

    /**
     * 调度器与线程模型
     * 当前线程（Schedulers.immediate()）；
     * 可重用的单线程（Schedulers.single()）。注意，这个方法对所有调用者都提供同一个线程来使用， 直到该调度器被废弃。如果你想使用独占的线程，请使用Schedulers.newSingle()；
     * 弹性线程池（Schedulers.elastic()）。它根据需要创建一个线程池，重用空闲线程。线程池如果空闲时间过长 （默认为 60s）就会被废弃。对于 I/O 阻塞的场景比较适用。Schedulers.elastic()能够方便地给一个阻塞 的任务分配它自己的线程，从而不会妨碍其他任务和资源；
     * 固定大小线程池（Schedulers.parallel()），所创建线程池的大小与CPU个数等同；
     * 自定义线程池（Schedulers.fromExecutorService(ExecutorService)）基于自定义的ExecutorService创建 Scheduler（虽然不太建议，不过你也可以使用Executor来创建）
     */
    @Test
    public void testSchedule() throws InterruptedException {

        LOGGER.info("main thread[{}]", Thread.currentThread().getName());
        Flux.just("a", "b")
                // 默认在 主线程中
                .publishOn(Schedulers.immediate())
//                 自定义线程池
                .publishOn(Schedulers.fromExecutorService(Executors.newFixedThreadPool(5)))
                .map(v -> {
                    LOGGER.info("range-map-1 thread[{}] with {}", Thread.currentThread().getName(), v);
                    return v;
                }).subscribe();

        /**
         * 切换调度线程池
         */
        Flux.range(1, 10)
                .map(i -> {
                    LOGGER.debug("range-map-1 thread[{}] with {}", Thread.currentThread().getName(), i);
                    return i * 2;
                })
                // 调整 publishOn 后的map操作的线程池为 elastic
                .publishOn(Schedulers.newElastic("kute-elastic"))
                .map(i -> {
                    LOGGER.debug("publishOn-1 thread[{}] with {}", Thread.currentThread().getName(), i);
                    return i;
                })
                // 调整 publishOn 后的map操作的线程池为 parallel
                .publishOn(Schedulers.newParallel("kute-parallel"))
                .map(i -> {
                    LOGGER.debug("publishOn-2 thread[{}] with {}", Thread.currentThread().getName(), i);
                    return i + "a";
                })
                // subscribeOn无论出现在什么位置，都只影响源头的执行环境，故 range 以及 range后的map都是在 single-kute 线程池中，直到publishOn切换
                .subscribeOn(Schedulers.newSingle("single-kute"))
                .subscribe();

        countdown(5);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        Mono.fromCallable(() -> getStringSync(2))
                // subscribeOn 更改 源头 getStringSync 的执行线程，使得同步成为异步（在其他线程执行）
                .subscribeOn(Schedulers.elastic())
                .subscribe(System.out::println, null, countDownLatch::countDown);
        LOGGER.info("main thread:{}", Thread.currentThread().getName());
        countDownLatch.await(10, TimeUnit.SECONDS);

    }

    /**
     * 高级操作 主要用来监控
     */
    @Test
    public void testDoOn() {
        Flux.range(1, 100)
                .limitRate(9)
                .doOnEach(integerSignal -> {
                    LOGGER.info("每分发一个事件时触发，主要用来监控，signal=={}", integerSignal.getType().name());
                })
                .doOnRequest(value -> {

                })
                .doOnNext(value -> {
                    LOGGER.info("doOnNext value:{}", value);
                    if (value == 4) {
                        throw new RuntimeException("");
                    }
                })
                .doOnSubscribe(subscription -> {

                })
                .doOnCancel(() -> {

                })
                .doOnComplete(() -> {

                })
                .doOnError(IllegalStateException.class, e -> {

                })
                .doOnError(Exception.class, e -> {

                })
                .doOnTerminate(() -> {

                }).subscribe();
    }

    @Test
    public void testBaseSubScriber() {
        Flux.range(1, 10)
                .map(i -> {
                    if (i == 2) {
                        throw new RuntimeException("");
                    }
                    return i;
                })
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        LOGGER.info("初始化订阅，并消费3个元素（会调用hookOnNext 3次消费），即发布者会产生3个元素");
                        request(3);
                        //            requestUnbounded(); // 等价于 request(Long.MAX_VALUE);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        LOGGER.info("正在消费的需求是:{}", value);
                        // 若 hookOnSubscribe 初始化消费后 还有 需求 在这里可以继续调用 request表明继续生产更多元素
                        if (Integer.parseInt(value.toString()) >= 8) { // 大于 8 之后 就取消生产
                            cancel();
                        } else {
                            request(1);
                        }
                    }

                    @Override
                    protected void hookOnComplete() {
                        LOGGER.info("全部消费完成调用");
                    }

                    @Override
                    protected void hookOnError(Throwable throwable) {
                        LOGGER.info("hookOnError:{}", throwable.getClass().getSimpleName());
                        // 异常后怎么继续分发事件呢 ？
                    }

                    @Override
                    protected void hookOnCancel() {
                        LOGGER.info("hookOnCancel");
                    }

                    @Override
                    protected void hookFinally(SignalType type) {
                        LOGGER.info("hookFinally");
                    }
                });
    }

    private void countdown(int n) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await(n, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String getStringSync(int n) {
        try {
            LOGGER.info("thread[{}] sleep {} seconds", Thread.currentThread().getName(), n);
            TimeUnit.SECONDS.sleep(n);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Hello, Reactor!";
    }

    @Test
    public void testBackPressure() {

        Flux.range(1, 6)    // 1
                .doOnRequest(n -> LOGGER.info("Request " + n + " values..."))    // 2
                .subscribe(new BaseSubscriber<Integer>() {  // 3
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) { // 4
                        LOGGER.info("Subscribed and make a request...");
                        request(1); // 5
                    }

                    @Override
                    protected void hookOnNext(Integer value) {  // 6
                        try {
                            TimeUnit.SECONDS.sleep(2);  // 7
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        LOGGER.info("Get value [" + value + "]");    // 8
                        request(1); // 9
                    }
                });
    }

}
