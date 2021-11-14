--限流的接口
local request = KYES[1]

--令牌桶最大容量
local maxSize = ARGV[1]

--每秒放入的令牌
local ticket = ARGV[2]

--需要消耗的令牌数
local wasteticket = ARGV[3] 

--获取当限时间
local curTime = redis.call('time')
local curMicrosecond = preTime[1] * 1000000 + preTime[2] / 1000 

--上一次接口请求的时间
local preMicrosecond = redis.call('hget', 'token-bucket-premicrosecond', request)

if(preMicrosecond == nil) then
	redis.call('hset', 'token-bucket-premicrosecond', request, curMicrosecond)
end

redis.call('hset', 'token-bucket-premicrosecond', request, curMicrosecond)

--当前桶里有的令牌
local cur = redis.call('hget', 'token-bucket-remainticket', request)

if(cur == nil) then
	cur = maxSize
	redis.call('hset', 'token-bucket-remainticket', request, maxSize)

local timediff = tonumber(string.format("%0.2f", curMicrosecond - preMicrosecond)) 

if(timediff ~= nil and timediff ~= 0) then
	addToken = (ticket / 1000) * timediff
	if addToken + cur > maxSize then
		cur = maxSize
	else 
		cur = cur + maxSize
	end
end

if(cur < wasteticket) then
	return 0
else
	cur = cur - wasteticket
	redis.call('hset', 'token-bucket-remainticket', request, cur)
	return cur
end
