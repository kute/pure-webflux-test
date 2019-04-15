-- check_and_set.lua
local current = redis.call('GET', KEYS[1])
if (current == ARGV[1])
then
    redis.call('SET', KEYS[1], ARGV[2])
    return true
else
    return false
end