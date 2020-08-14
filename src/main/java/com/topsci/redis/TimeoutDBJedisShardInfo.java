package com.topsci.redis;

import redis.clients.jedis.JedisShardInfo;

public class TimeoutDBJedisShardInfo extends JedisShardInfo {
    public TimeoutDBJedisShardInfo(String host,int timeout)
    {
        super(host);
        setConnectionTimeout(timeout);
        setSoTimeout(timeout);
    }
}
