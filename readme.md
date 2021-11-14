## 一些问题的记录

lua处理两个事件
1. 每秒钟添加令牌
2. 减去请求到来拿走的令牌

于是考虑每当拿走令牌后开启一个协程去做事件1.
所使用的方法是os.execute("sleep 1")
但是出现的问题在于lua limit.lua是可以跑的。
但是拿到redis客户端就没办法跑了，redis-cli --eval limit.lua
问题在于如何解决redis客户端无法调用os函数的问题。
