package com.topsci.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SentinelJedisClientTemplate implements JedisClientTemplate {

	private static final Logger logger = LoggerFactory.getLogger(SentinelJedisClientTemplate.class);


		private static SentinelJedisClientTemplate instance;

	    private JedisDataSource jedisDataSource;
	    /**
	     * 単例实现
	     */
	    public static synchronized SentinelJedisClientTemplate getInstance(){
	    	if(null == instance){
	    		try{
	    			instance = new SentinelJedisClientTemplate();
	    		}catch(Exception e){
	    			e.printStackTrace();
	    		}
	    	}
	    	return instance;
	    }
	    private SentinelJedisClientTemplate(){
	    	jedisDataSource = JedisDataSource.getInstance();
	    }
	    

	    @Override
		public void disconnect() {
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        shardedJedis.disconnect();
	    }

	    /**
	     * 设置单个值
	     * 
	     * @param key
	     * @param value
	     * @return
	     */
	    @Override
		public String set(String key, String value) {
	        String result = null;

	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.set(key, value);
	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    /**
	     * 获取单个值
	     * 
	     * @param key
	     * @return
	     */
	    @Override
		public String get(String key) {
	        String result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }

	        boolean broken = false;
	        try {
	            result = shardedJedis.get(key);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Boolean exists(String key) {
	        Boolean result = false;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.exists(key);
	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public String type(String key) {
	        String result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.type(key);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    /**
	     * 在某段时间后实现
	     * 
	     * @param key
	     * @param unixTime
	     * @return
	     */
	    @Override
		public Long expire(String key, int seconds) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.expire(key, seconds);
	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    /**
	     * 在某个时间点失效
	     * 
	     * @param key
	     * @param unixTime
	     * @return
	     */
	    @Override
		public Long expireAt(String key, long unixTime) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.expireAt(key, unixTime);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long ttl(String key) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.ttl(key);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public boolean setbit(String key, long offset, boolean value) {

	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        boolean result = false;
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.setbit(key, offset, value);
	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public boolean getbit(String key, long offset) {
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        boolean result = false;
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;

	        try {
	            result = shardedJedis.getbit(key, offset);
	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public long setrange(String key, long offset, String value) {
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        long result = 0;
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.setrange(key, offset, value);
	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public String getrange(String key, long startOffset, long endOffset) {
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        String result = null;
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.getrange(key, startOffset, endOffset);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public String getSet(String key, String value) {
	        String result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.getSet(key, value);
	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long setnx(String key, String value) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.setnx(key, value);
	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public String setex(String key, int seconds, String value) {
	        String result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.setex(key, seconds, value);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long decrBy(String key, long integer) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.decrBy(key, integer);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long decr(String key) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.decr(key);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long incrBy(String key, long integer) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.incrBy(key, integer);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long incr(String key) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.incr(key);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long append(String key, String value) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.append(key, value);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public String substr(String key, int start, int end) {
	        String result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.substr(key, start, end);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long hset(String key, String field, String value) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.hset(key, field, value);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public String hget(String key, String field) {
	        String result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.hget(key, field);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long hsetnx(String key, String field, String value) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.hsetnx(key, field, value);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public String hmset(String key, Map<String, String> hash) {
	        String result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.hmset(key, hash);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public List<String> hmget(String key, String... fields) {
	        List<String> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.hmget(key, fields);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long hincrBy(String key, String field, long value) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.hincrBy(key, field, value);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Boolean hexists(String key, String field) {
	        Boolean result = false;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.hexists(key, field);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long del(String key) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.del(key);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long hdel(String key, String field) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.hdel(key, field);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long hlen(String key) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.hlen(key);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Set<String> hkeys(String key) {
	        Set<String> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.hkeys(key);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public List<String> hvals(String key) {
	        List<String> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.hvals(key);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Map<String, String> hgetAll(String key) {
	        Map<String, String> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.hgetAll(key);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    // ================list ====== l表示 list或 left, r表示right====================
	    @Override
		public Long rpush(String key, String string) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.rpush(key, string);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long lpush(String key, String string) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.lpush(key, string);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long llen(String key) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.llen(key);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public List<String> lrange(String key, long start, long end) {
	        List<String> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.lrange(key, start, end);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public String ltrim(String key, long start, long end) {
	        String result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.ltrim(key, start, end);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public String lindex(String key, long index) {
	        String result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.lindex(key, index);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public String lset(String key, long index, String value) {
	        String result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.lset(key, index, value);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long lrem(String key, long count, String value) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.lrem(key, count, value);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public String lpop(String key) {
	        String result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.lpop(key);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public String rpop(String key) {
	        String result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.rpop(key);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    //return 1 add a not exist value ,
	    //return 0 add a exist value
	    @Override
		public Long sadd(String key, String member) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.sadd(key, member);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Set<String> smembers(String key) {
	        Set<String> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.smembers(key);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long srem(String key, String member) {
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();

	        Long result = null;
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.srem(key, member);
	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public String spop(String key) {
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        String result = null;
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.spop(key);
	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long scard(String key) {
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        Long result = null;
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.scard(key);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Boolean sismember(String key, String member) {
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        Boolean result = null;
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.sismember(key, member);
	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public String srandmember(String key) {
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        String result = null;
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.srandmember(key);
	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long zadd(String key, double score, String member) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.zadd(key, score, member);
	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Set<String> zrange(String key, int start, int end) {
	        Set<String> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.zrange(key, start, end);
	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long zrem(String key, String member) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.zrem(key, member);
	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Double zincrby(String key, double score, String member) {
	        Double result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zincrby(key, score, member);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long zrank(String key, String member) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zrank(key, member);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long zrevrank(String key, String member) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zrevrank(key, member);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Set<String> zrevrange(String key, int start, int end) {
	        Set<String> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zrevrange(key, start, end);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Set<Tuple> zrangeWithScores(String key, int start, int end) {
	        Set<Tuple> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zrangeWithScores(key, start, end);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Set<Tuple> zrevrangeWithScores(String key, int start, int end) {
	        Set<Tuple> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zrevrangeWithScores(key, start, end);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long zcard(String key) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zcard(key);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Double zscore(String key, String member) {
	        Double result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zscore(key, member);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public List<String> sort(String key) {
	        List<String> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.sort(key);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public List<String> sort(String key, SortingParams sortingParameters) {
	        List<String> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.sort(key, sortingParameters);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long zcount(String key, double min, double max) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zcount(key, min, max);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Set<String> zrangeByScore(String key, double min, double max) {
	        Set<String> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zrangeByScore(key, min, max);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Set<String> zrevrangeByScore(String key, double max, double min) {
	        Set<String> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zrevrangeByScore(key, max, min);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
	        Set<String> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zrangeByScore(key, min, max, offset, count);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
	        Set<String> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zrevrangeByScore(key, max, min, offset, count);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
	        Set<Tuple> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zrangeByScoreWithScores(key, min, max);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
	        Set<Tuple> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zrevrangeByScoreWithScores(key, max, min);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
	        Set<Tuple> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zrangeByScoreWithScores(key, min, max, offset, count);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
	        Set<Tuple> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zrevrangeByScoreWithScores(key, max, min, offset, count);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long zremrangeByRank(String key, int start, int end) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zremrangeByRank(key, start, end);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long zremrangeByScore(String key, double start, double end) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zremrangeByScore(key, start, end);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long linsert(String key, LIST_POSITION where, String pivot, String value) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.linsert(key, where, pivot, value);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public String set(byte[] key, byte[] value) {
	        String result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.set(key, value);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public byte[] get(byte[] key) {
	        byte[] result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.get(key);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Boolean exists(byte[] key) {
	        Boolean result = false;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.exists(key);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public String type(byte[] key) {
	        String result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.type(key);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long expire(byte[] key, int seconds) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.expire(key, seconds);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long expireAt(byte[] key, long unixTime) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.expireAt(key, unixTime);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long ttl(byte[] key) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.ttl(key);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public byte[] getSet(byte[] key, byte[] value) {
	        byte[] result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.getSet(key, value);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long setnx(byte[] key, byte[] value) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.setnx(key, value);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public String setex(byte[] key, int seconds, byte[] value) {
	        String result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.setex(key, seconds, value);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long decrBy(byte[] key, long integer) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.decrBy(key, integer);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long decr(byte[] key) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.decr(key);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long incrBy(byte[] key, long integer) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.incrBy(key, integer);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long incr(byte[] key) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.incr(key);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long append(byte[] key, byte[] value) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.append(key, value);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public byte[] substr(byte[] key, int start, int end) {
	        byte[] result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.substr(key, start, end);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long hset(byte[] key, byte[] field, byte[] value) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.hset(key, field, value);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public byte[] hget(byte[] key, byte[] field) {
	        byte[] result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.hget(key, field);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long hsetnx(byte[] key, byte[] field, byte[] value) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.hsetnx(key, field, value);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public String hmset(byte[] key, Map<byte[], byte[]> hash) {
	        String result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.hmset(key, hash);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public List<byte[]> hmget(byte[] key, byte[]... fields) {
	        List<byte[]> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.hmget(key, fields);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long hincrBy(byte[] key, byte[] field, long value) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.hincrBy(key, field, value);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Boolean hexists(byte[] key, byte[] field) {
	        Boolean result = false;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.hexists(key, field);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long hdel(byte[] key, byte[] field) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.hdel(key, field);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long hlen(byte[] key) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.hlen(key);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Set<byte[]> hkeys(byte[] key) {
	        Set<byte[]> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.hkeys(key);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Collection<byte[]> hvals(byte[] key) {
	        Collection<byte[]> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.hvals(key);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Map<byte[], byte[]> hgetAll(byte[] key) {
	        Map<byte[], byte[]> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.hgetAll(key);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long rpush(byte[] key, byte[] string) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.rpush(key, string);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long lpush(byte[] key, byte[] string) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.lpush(key, string);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long llen(byte[] key) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.llen(key);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public List<byte[]> lrange(byte[] key, int start, int end) {
	        List<byte[]> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.lrange(key, start, end);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public String ltrim(byte[] key, int start, int end) {
	        String result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.ltrim(key, start, end);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public byte[] lindex(byte[] key, int index) {
	        byte[] result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.lindex(key, index);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public String lset(byte[] key, int index, byte[] value) {
	        String result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.lset(key, index, value);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long lrem(byte[] key, int count, byte[] value) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.lrem(key, count, value);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public byte[] lpop(byte[] key) {
	        byte[] result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.lpop(key);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public byte[] rpop(byte[] key) {
	        byte[] result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.rpop(key);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long sadd(byte[] key, byte[] member) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.sadd(key, member);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Set<byte[]> smembers(byte[] key) {
	        Set<byte[]> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.smembers(key);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long srem(byte[] key, byte[] member) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.srem(key, member);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public byte[] spop(byte[] key) {
	        byte[] result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.spop(key);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long scard(byte[] key) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.scard(key);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Boolean sismember(byte[] key, byte[] member) {
	        Boolean result = false;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.sismember(key, member);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public byte[] srandmember(byte[] key) {
	        byte[] result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.srandmember(key);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long zadd(byte[] key, double score, byte[] member) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zadd(key, score, member);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Set<byte[]> zrange(byte[] key, int start, int end) {
	        Set<byte[]> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zrange(key, start, end);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long zrem(byte[] key, byte[] member) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zrem(key, member);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Double zincrby(byte[] key, double score, byte[] member) {
	        Double result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zincrby(key, score, member);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long zrank(byte[] key, byte[] member) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zrank(key, member);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long zrevrank(byte[] key, byte[] member) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zrevrank(key, member);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Set<byte[]> zrevrange(byte[] key, int start, int end) {
	        Set<byte[]> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zrevrange(key, start, end);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Set<Tuple> zrangeWithScores(byte[] key, int start, int end) {
	        Set<Tuple> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zrangeWithScores(key, start, end);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Set<Tuple> zrevrangeWithScores(byte[] key, int start, int end) {
	        Set<Tuple> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zrevrangeWithScores(key, start, end);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long zcard(byte[] key) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zcard(key);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Double zscore(byte[] key, byte[] member) {
	        Double result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zscore(key, member);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public List<byte[]> sort(byte[] key) {
	        List<byte[]> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.sort(key);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public List<byte[]> sort(byte[] key, SortingParams sortingParameters) {
	        List<byte[]> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.sort(key, sortingParameters);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long zcount(byte[] key, double min, double max) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zcount(key, min, max);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
	        Set<byte[]> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zrangeByScore(key, min, max);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Set<byte[]> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
	        Set<byte[]> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zrangeByScore(key, min, max, offset, count);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
	        Set<Tuple> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zrangeByScoreWithScores(key, min, max);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max, int offset, int count) {
	        Set<Tuple> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zrangeByScoreWithScores(key, min, max, offset, count);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
	        Set<byte[]> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zrevrangeByScore(key, max, min);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
	        Set<byte[]> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zrevrangeByScore(key, max, min, offset, count);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
	        Set<Tuple> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zrevrangeByScoreWithScores(key, max, min);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min, int offset, int count) {
	        Set<Tuple> result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zrevrangeByScoreWithScores(key, max, min, offset, count);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long zremrangeByRank(byte[] key, int start, int end) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zremrangeByRank(key, start, end);

	        } catch (Exception e) {

	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long zremrangeByScore(byte[] key, double start, double end) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zremrangeByScore(key, start, end);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    @Override
		public Long linsert(byte[] key, LIST_POSITION where, byte[] pivot, byte[] value) {
	        Long result = null;
	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.linsert(key, where, pivot, value);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            jedisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

//	    public List<Object> pipelined(ShardedJedisPipeline shardedJedisPipeline) {
//			Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
//	        List<Object> result = null;
//	        if (shardedJedis == null) {
//	            return result;
//	        }
//	        boolean broken = false;
//	        try {
//	            result = shardedJedis.pipelined(shardedJedisPipeline);
//	        } catch (Exception e) {
//	            logger.error(e.getMessage(), e);
//	            broken = true;
//	        } finally {
//	            jedisDataSource.returnResource(shardedJedis, broken);
//	        }
//	        return result;
//	    }
//
//	    public Jedis getShard(byte[] key) {
//	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
//	        Jedis result = null;
//	        if (shardedJedis == null) {
//	            return result;
//	        }
//	        boolean broken = false;
//	        try {
//	            result = shardedJedis.getShard(key);
//	        } catch (Exception e) {
//	            logger.error(e.getMessage(), e);
//	            broken = true;
//	        } finally {
//	            jedisDataSource.returnResource(shardedJedis, broken);
//	        }
//	        return result;
//	    }
//
//	    public Jedis getShard(String key) {
//	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
//	        Jedis result = null;
//	        if (shardedJedis == null) {
//	            return result;
//	        }
//	        boolean broken = false;
//	        try {
//	            result = shardedJedis.getShard(key);
//	        } catch (Exception e) {
//	            logger.error(e.getMessage(), e);
//	            broken = true;
//	        } finally {
//	            jedisDataSource.returnResource(shardedJedis, broken);
//	        }
//	        return result;
//	    }
//
//	    public JedisShardInfo getShardInfo(byte[] key) {
//	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
//	        JedisShardInfo result = null;
//	        if (shardedJedis == null) {
//	            return result;
//	        }
//	        boolean broken = false;
//	        try {
//	            result = shardedJedis.getShardInfo(key);
//	        } catch (Exception e) {
//	            logger.error(e.getMessage(), e);
//	            broken = true;
//	        } finally {
//	            jedisDataSource.returnResource(shardedJedis, broken);
//	        }
//	        return result;
//	    }
//
//	    public JedisShardInfo getShardInfo(String key) {
//	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
//	        JedisShardInfo result = null;
//	        if (shardedJedis == null) {
//	            return result;
//	        }
//	        boolean broken = false;
//	        try {
//	            result = shardedJedis.getShardInfo(key);
//	        } catch (Exception e) {
//	            logger.error(e.getMessage(), e);
//	            broken = true;
//	        } finally {
//	            jedisDataSource.returnResource(shardedJedis, broken);
//	        }
//	        return result;
//	    }
//
//	    public String getKeyTag(String key) {
//	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
//	        String result = null;
//	        if (shardedJedis == null) {
//	            return result;
//	        }
//	        boolean broken = false;
//	        try {
//	            result = shardedJedis.getKeyTag(key);
//	        } catch (Exception e) {
//	            logger.error(e.getMessage(), e);
//	            broken = true;
//	        } finally {
//	            jedisDataSource.returnResource(shardedJedis, broken);
//	        }
//	        return result;
//	    }
//
//	    public Collection<JedisShardInfo> getAllShardInfo() {
//	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
//	        Collection<JedisShardInfo> result = null;
//	        if (shardedJedis == null) {
//	            return result;
//	        }
//	        boolean broken = false;
//	        try {
//	            result = shardedJedis.getAllShardInfo();
//
//	        } catch (Exception e) {
//	            logger.error(e.getMessage(), e);
//	            broken = true;
//	        } finally {
//	            jedisDataSource.returnResource(shardedJedis, broken);
//	        }
//	        return result;
//	    }
//
//	    public Collection<Jedis> getAllShards() {
//	        Jedis shardedJedis = (Jedis)jedisDataSource.getRedisClient();
//	        Collection<Jedis> result = null;
//	        if (shardedJedis == null) {
//	            return result;
//	        }
//	        boolean broken = false;
//	        try {
//	            result = shardedJedis.getAllShards();
//
//	        } catch (Exception e) {
//	            logger.error(e.getMessage(), e);
//	            broken = true;
//	        } finally {
//	            jedisDataSource.returnResource(shardedJedis, broken);
//	        }
//	        return result;
//	    }

}
