local request = KEYS[1]

local value = ARGV[1]

local expire_time = tonumber(ARGV[2])

local time_ex_px = ARGV[3]

if not redis.call('get', request) then 
     return redis.call('set', request, value, time_ex_px, expire_time)
end

return -1