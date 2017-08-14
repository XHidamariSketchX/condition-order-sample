# condition-order-sample

DDD sample, a minimized condition order system.

## Technologies

- spring-boot
- guava(event bus)/gson
- spring-jpa(using hibernate)/spring-data-redis/spring-rabbitmq
- mysql/redis/rabbitmq

## TODO

- use life circle field instead of TriggerOnce tag interface
- monitor context(delay sync, trigger lock)
- trigger context
- entrust orders
- turn up buy order
- grid trade order
- batching order (abort state)
- new shares purchase order
- mock entrust interface
- move dto package out
- pause/resume
- using dubbo(split api layer and biz layer)