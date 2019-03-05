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

背压： 当Subscriber请求的数据的访问超出它的处理能力时，Publisher限制数据发送速度的能力。本质上背压和TCP中的窗口限流机制比较类似，都是让消费者反馈请求数据的范围，生产者根据消费者的反馈提供一定量的数据来进行流控




