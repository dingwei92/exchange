package com.topsci.redis;



import com.topsci.common.Common;
import com.topsci.config.PropertiesBean;
import com.topsci.util.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;
import redis.clients.util.Pool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JedisDataSource {

	private final Logger logger = LoggerFactory.getLogger(JedisDataSource.class);
	private PropertiesBean propertiesBean = SpringUtil.getBean(PropertiesBean.class);
	    private static JedisDataSource instance;

	    private JedisSentinelPool jedisSentinelPool;

		private ShardedJedisPool shardedJedisPool;
	    /**
	     * 获取连接池唯一实例
	     * @return
	     */
	    public static synchronized JedisDataSource getInstance(){
	    	if(null == instance){
	    		try{
	    			instance = new JedisDataSource();
	    		}catch(Exception e){
	    			e.printStackTrace();
	    		}
	    	}
	    	return instance;
	    }


	    private JedisDataSource(){
	    	JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
	    	jedisPoolConfig.setMaxTotal(100);
	    	jedisPoolConfig.setMaxIdle(100);
	    	jedisPoolConfig.setMinIdle(100);
//	    	jedisPoolConfig.setTimeBetweenEvictionRunsMillis(60000L);

	    	String[] ips = propertiesBean.getRedisUrl().split(",");
	    	if(ips!=null&&ips.length>0){
	    		if(propertiesBean.getRedisType().equals(Common.REDIS_SENTINEL)) {
					Set<String> listInfo = new HashSet<>();
					for (String str : ips) {
						listInfo.add(str);
					}
					jedisSentinelPool = new JedisSentinelPool(propertiesBean.getRedisMasterName(), listInfo, jedisPoolConfig, 100000, propertiesBean.getRedisMasterPwd());
					jedisSentinelPool.getResource().select(propertiesBean.getRedisDB());
				}
				else if(propertiesBean.getRedisType().equals(Common.REDIS_SHARD))
				{
					List<JedisShardInfo> listInfo = new ArrayList<JedisShardInfo>();
					JedisShardInfo info = null;
					for(String str:ips){
						info = new TimeoutDBJedisShardInfo(str,100000);
						listInfo.add(info);
					}
					shardedJedisPool = new ShardedJedisPool(jedisPoolConfig, listInfo);
				}
	    	}

	    }

	    public Object getRedisClient() {
	        Object shardJedis = null;
			Pool pool = null;
			if(propertiesBean.getRedisType().equals(Common.REDIS_SHARD))
			{
				pool = shardedJedisPool;
			}
			else if(propertiesBean.getRedisType().equals(Common.REDIS_SENTINEL))
			{
				pool = jedisSentinelPool;
			}
	        try {
	            shardJedis =  pool.getResource();
	            return shardJedis;
	        } catch (Exception e) {
	        	logger.error("[JedisDS] getRedisClent error:" + e.getMessage(),e);
	            if (null != shardJedis)
					if(propertiesBean.getRedisType().equals(Common.REDIS_SHARD))
					{
						((ShardedJedis)shardJedis).close();
					}
					else if(propertiesBean.getRedisType().equals(Common.REDIS_SENTINEL))
					{
						((Jedis)shardJedis).close();
					}
	        }
	        return null;
	    }

	    public void returnResource(Object shardedJedis) {
			if(propertiesBean.getRedisType().equals(Common.REDIS_SHARD))
			{
				((ShardedJedis)shardedJedis).close();
			}
			else if(propertiesBean.getRedisType().equals(Common.REDIS_SENTINEL))
			{
				((Jedis)shardedJedis).close();
			}
	    }

	    public void returnResource(Object shardedJedis, boolean broken) {
			if(propertiesBean.getRedisType().equals(Common.REDIS_SHARD))
			{
				((ShardedJedis)shardedJedis).close();
			}
			else if(propertiesBean.getRedisType().equals(Common.REDIS_SENTINEL))
			{
				((Jedis)shardedJedis).close();
			}
	    }

}
