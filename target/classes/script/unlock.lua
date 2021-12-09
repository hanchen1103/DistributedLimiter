local request = KEYS[1]

local value = ARGV[1]

if redis.call('get', request) == value then
	return redis.call('del', request)
else
	return -1
end