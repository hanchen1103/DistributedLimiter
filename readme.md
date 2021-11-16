## 一些问题的记录

lua处理两个事件
1. 每秒钟添加令牌
2. 减去请求到来拿走的令牌

于是考虑每当拿走令牌后开启一个协程去做事件1.
所使用的方法是os.execute("sleep 1")
但是出现的问题在于lua limit.lua是可以跑的。
但是拿到redis客户端就没办法跑了，redis-cli --eval limit.lua
问题在于如何解决redis客户端无法调用os函数的问题。

-------------------------------------
redis-script不支持函数以及协程（猜测是为了原子性）
并且用该脚本只从redis参数和redis数据中获取输入，明显os不属于他们，所以错误了。

我的解决方式使用redis的time函数来获取microsecond(毫秒)值，记录每一次的毫秒值，用于下一次相减，并且用1/1000的每秒所放令牌计算累加，加上当前桶中的令牌数。

springboot 2+已经不支持直接创建JedisConnectionFactory,需要配合RedisStandaloneConfigurations使用更加。包括JedisPoolConfig。