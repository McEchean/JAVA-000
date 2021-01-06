if tonumber(redis.call("get",KEYS[1])) >= tonumber(ARGV[1]) then
    return redis.call("decrby",KEYS[1],ARGV[1])
else
    return -1
end