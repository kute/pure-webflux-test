# pure-webflux-test


# Getting Started

### Guides
The following guides illustrates how to use certain features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

https://docs.spring.io/spring-framework/docs/5.0.0.BUILD-SNAPSHOT/spring-framework-reference/html/web-reactive.html
https://github.com/bclozel/spring-boot-web-reactive
https://github.com/spring-projects/spring-boot/tree/master/spring-boot-samples/spring-boot-sample-secure-webflux
https://github.com/spring-projects/spring-boot/tree/master/spring-boot-samples/spring-boot-sample-session-webflux


### Reactor

1、site
https://projectreactor.io/docs

reactor core:
- https://projectreactor.io/docs/core/release/reference/
- https://projectreactor.io/docs/core/release/api/
- https://github.com/reactor/reactor-core

reactor-by-example:

- https://www.infoq.com/articles/reactor-by-example
- https://github.com/reactor/lite-rx-api-hands-on
- https://blog.csdn.net/get_set/article/category/7484996
- https://blog.51cto.com/liukang/2090191

背压： 当Subscriber请求的数据的访问超出它的处理能力时，Publisher限制数据发送速度的能力。
本质上背压和TCP中的窗口限流机制比较类似，都是让消费者反馈请求数据的范围，生产者根据消费者的反馈提供一定量的数据来进行流控

回压的处理有以下几种策略，这几种策略定义在枚举类型OverflowStrategy中，不过还有一个IGNORE类型，即完全忽略下游背压请求，这可能会在下游队列积满的时候导致 IllegalStateException

- ERROR： 当下游跟不上节奏的时候发出一个错误信号。
- DROP：当下游没有准备好接收新的元素的时候抛弃这个元素。
- LATEST：让下游只得到上游最新的元素。
- BUFFER：缓存下游没有来得及处理的元素（如果缓存不限大小的可能导致OutOfMemoryError）。

content-type

- text/event-stream：HTML5 server-send-event（SSE），允许服务端推送数据到客户端，与websocket比较：http://javascript.ruanyifeng.com/htmlapi/eventsource.html
- application/stream+json：持续地json数据流


### rxjava

1、https://github.com/ReactiveX/RxJava


### gatling

压测工具
1、https://gatling.io/

mvn gatling:test -Dgatling.simulationClass=com.kute.webflux.LoadTestSimulation -Dbase.url=http://localhost:8090/ -Dtest.path=flux/latency/10 -Dsim.users=300
mvn gatling:test -Dgatling.simulationClass=com.kute.webflux.LoadTestSimulation -Dbase.url=http://localhost:8090/ -Dtest.path=normal/latency/10 -Dsim.users=300

### reactive redis

1、https://spring.io/guides/gs/spring-data-reactive-redis/


### reactive mongo
