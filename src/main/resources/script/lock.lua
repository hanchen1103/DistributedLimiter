
local request = KEYS[1]

local value = ARGV[1]

local expire_time = tonumber(ARGV[2])

if not redis.call('get', request) then
     return redis.call('set', request, value, 'px', expire_time)