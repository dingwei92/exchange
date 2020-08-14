package com.topsci.common;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.topsci.bean.*;
import com.topsci.config.PropertiesBean;
import com.topsci.db.service.YwDBService;
import com.topsci.redis.JedisClientTemplate;
import com.topsci.redis.SentinelJedisClientTemplate;
import com.topsci.redis.ShardedJedisClientTemplate;
import com.topsci.util.SpringUtil;
import com.topsci.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;

/**
 * @author yanhan
 *         订阅发布系统对缓存的操作
 */
public class CatchAbout {

    private final Logger logger = LoggerFactory.getLogger(CatchAbout.class);


    private JedisClientTemplate jedisClientTemplate;

    /**
     * 处理监听topic后处理xml的线程池
     */
    public static ExecutorService receivePool;

    /**
     * 处理xml分发，xml路由的线程池
     */
    public static ExecutorService sendPool;

    private static ArrayBlockingQueue<ServerMessageDetail> pushMessageQueue;
    public static ArrayBlockingQueue<ServerMessageDetail> requestMessageQueue;
    public static ArrayBlockingQueue<ServerMessageDetail> requestReturnMessageQueue;

    /**
     * 验证数据发布xml的schema
     * 注：项目启动时，把schema字节流放入缓存
     */
//	public static Map<String,byte[]> schemaCache = new ConcurrentHashMap<String, byte[]>();


    /**
     * 単例
     */
    private static CatchAbout instance;
    private Gson gson;
    private PropertiesBean propertiesBean = SpringUtil.getBean(PropertiesBean.class);
    
    private CatchAbout() {
        if(propertiesBean.getRedisType().equals(Common.REDIS_SHARD))
        {
            jedisClientTemplate = ShardedJedisClientTemplate.getInstance();
        }
        else if(propertiesBean.getRedisType().equals(Common.REDIS_SENTINEL))
        {
            jedisClientTemplate = SentinelJedisClientTemplate.getInstance();
        }
        pushMessageQueue = new ArrayBlockingQueue<>(propertiesBean.getMaxPushQueueSize());
        requestMessageQueue = new ArrayBlockingQueue<>(propertiesBean.getMaxPushQueueSize());
        requestReturnMessageQueue = new ArrayBlockingQueue<>(propertiesBean.getMaxPushQueueSize());
        gson = new Gson();
    }

    public static synchronized CatchAbout getInstance() {
        if (null == instance) {
            try {
                instance = new CatchAbout();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }


    public boolean testRedis() {
        boolean flag = false;
        try {
            String key = StringUtil.getUUID();
            jedisClientTemplate.set(key, key);
            String value = jedisClientTemplate.get(key);
            if (key.equals(value)) {
                flag = true;
            }
            jedisClientTemplate.del(key);
        } catch (Exception e) {
            logger.error("测试redis异常！", e);
        }

        return flag;
    }

    /**
     * 获取当前待发布，待路由主动请求队列的大小
     */
    public String getQueueSize() {
        StringBuffer log = new StringBuffer();

        /**
         * 待分发队列大小
         */
        long size1 = pushMessageQueue.size();
        /**
         * 待路由队列大小
         */
        long size2 = requestMessageQueue.size();
        /**
         * 待分发主动请求的回复队列大小
         */
        long size3 = requestReturnMessageQueue.size();

        log.append("等待分发增量：");
        log.append(size1);
        log.append("等待分发请求回复：");
        log.append(size3);
        log.append("等待路由请求：");
        log.append(size2);
        return log.toString();
    }

    /**
     * 初始化服务（分发和订阅）自定义字段缓存
     */
    public void initCache() {

        YwDBService ywDBService = YwDBService.getInstance();
        Map<String,Date> msgsnCache = ywDBService.initMSGSNCache();
        if(msgsnCache.size()>0) {
            Map<String, String> msgsnstr = new HashMap<>();
            msgsnCache.forEach((k, v) -> msgsnstr.put(k, gson.toJson(v)));
            jedisClientTemplate.hmset(Common.CACHE_MAP_MSGSN, msgsnstr);
        }

        /**
         * 分发服务列表，以及其提供的字段的缓存
         * key为ServerCatalogBean的id
         */
        Map<String, ServerCatalog> serverCatalogCache = ywDBService.initServerCatalogCache();
        if (serverCatalogCache != null && serverCatalogCache.size() > 0) {
            /**
             * 更新缓存中在值
             */
            Map<String,String> msgtypemap = new HashMap<>();
            Map<String,String> idmap = new HashMap<>();
            serverCatalogCache.forEach((k,v)->{
                msgtypemap.put(v.getServerShort(),gson.toJson(v));
                idmap.put(v.getId()+"",gson.toJson(v));
            });
            jedisClientTemplate.hmset(Common.CACHE_MAP_CATALOG,msgtypemap);
            jedisClientTemplate.hmset(Common.CACHE_MAP_CATALOG,idmap);
            /**
             * 删除缓存中多余的
             */
            Set<String> re = jedisClientTemplate.hkeys(Common.CACHE_MAP_CATALOG);
            re.forEach(k->{
                long count = serverCatalogCache.values().stream().filter(sscbe -> (sscbe.getId()+"").equals(k) || (sscbe.getServerShort()).equals(k))
                        .count();
                if(count == 0)
                {
                    jedisClientTemplate.hdel(Common.CACHE_MAP_CATALOG, k);
                }
            });
        }

        /**
         * 模板列表
         */
        Map<String, ServerCatalogTemplate> serverCatalogTemplateCache = ywDBService.initServerCatalogTemplateCache();
        if(serverCatalogTemplateCache != null && !serverCatalogTemplateCache.isEmpty())
        {
            /**
             * 更新缓存中在值
             */
            Map<String,String> templatemap = new HashMap<>();
            serverCatalogTemplateCache.forEach((k,v)->templatemap.put(k,gson.toJson(v)));
            jedisClientTemplate.hmset(Common.CACHE_MAP_TEMPLATE,templatemap);
            /**
             * 删除缓存中多余的
             */
            Set<String> re = jedisClientTemplate.hkeys(Common.CACHE_MAP_TEMPLATE);
            re.forEach(k->{
                if (serverCatalogTemplateCache.get(k) == null) {
                    jedisClientTemplate.hdel(Common.CACHE_MAP_TEMPLATE, k);
                }
            });
        }

        /**
         * 业务详情列表
         * key为ServerSubscribesBean的id
         */
        Map<String, BusinessSystem> serverBusinessCache = ywDBService.initBusinessSystemCache();
        if (serverBusinessCache != null && serverBusinessCache.size() > 0) {
            Map<String,String> bsmap = new HashMap<>();
            serverBusinessCache.forEach((k,v)->bsmap.put(k,gson.toJson(v)));
            jedisClientTemplate.hmset(Common.CACHE_MAP_BUSINESS,bsmap);
            /**
             * 删除缓存中多余的
             */
            Set<String> re = jedisClientTemplate.hkeys(Common.CACHE_MAP_BUSINESS);
            re.forEach(k->{
                if (serverBusinessCache.get(k) == null) {
                    jedisClientTemplate.hdel(Common.CACHE_MAP_BUSINESS, k);
                }
            });

        }

        /**
         * 订阅服务列表，以及其订阅字段的缓存
         * key为ServerSubscribesBean的id
         */
        Map<String, ServerSubscribes> serverSubscribesCache = ywDBService.initServerSubscribesCache();
        if (serverSubscribesCache != null && serverSubscribesCache.size() > 0) {
            Map<String,String> keymap = new HashMap<>();
            Map<String,String> idmap = new HashMap<>();
            serverSubscribesCache.forEach((k,v)->{
                keymap.put(k,gson.toJson(v));
                idmap.put(v.getId()+"",gson.toJson(v));
            });
            jedisClientTemplate.hmset(Common.CACHE_MAP_SUBSCRIBE,keymap);
            jedisClientTemplate.hmset(Common.CACHE_MAP_SUBSCRIBE,idmap);
            Map<String,String> listmap = jedisClientTemplate.hgetAll(Common.CACHE_MAP_SUBSCRIBE_LIST);
            if(listmap == null){
                listmap = new HashMap<>();
            }
            for (Map.Entry<String, ServerSubscribes> entry : serverSubscribesCache.entrySet()) {
                String str = listmap.get(entry.getValue().getServerCatalogBean().getServerShort());
                List<String> ids = new ArrayList<>();
                if(StringUtils.isNotEmpty(str)){
                    ids = JSON.parseArray(str,String.class);
                }
                if(!ids.contains(entry.getValue().getId()+"")) {
                    ids.add(entry.getValue().getId() + "");
                }
                listmap.put(entry.getValue().getServerCatalogBean().getServerShort(),gson.toJson(ids));
            }
            jedisClientTemplate.hmset(Common.CACHE_MAP_SUBSCRIBE_LIST,listmap);
            /**
             * 删除缓存中多余的
             */
            Set<String> keys = jedisClientTemplate.hkeys(Common.CACHE_MAP_SUBSCRIBE);
            keys.forEach(k->{
                long re = serverSubscribesCache.values().stream().filter(sssbe -> (sssbe.getId()+"").equals(k) ||
                        (sssbe.getBusinessSystemBean().getSystemShort()+"_"+sssbe.getServerCatalogBean().getServerShort()).equals(k)).count();
                if(re == 0)
                {
                    jedisClientTemplate.hdel(Common.CACHE_MAP_SUBSCRIBE, k);
                }
            });

            Map<String,String> vals = jedisClientTemplate.hgetAll(Common.CACHE_MAP_SUBSCRIBE_LIST);
            vals.forEach((k,v)->{
                if(StringUtils.isNotEmpty(v)){
                    List<String> ids = JSON.parseArray(v,String.class);
                    List<String> delete = new ArrayList<>();
                    for(String id : ids){
                        long count = serverSubscribesCache.values().stream().filter(
                                sssbe -> sssbe.getServerCatalogBean().getServerShort().equals(k) && (sssbe.getId()+"").equals(id)).count();
                        if(count == 0){
                            delete.add(id);
                        }
                    }
                    ids.removeAll(delete);
                }
            });
        }
    }


    /**
     * 通过msgcode,formSysID获取数据发送方的topic
     */
    public String getTopic(String msgCode, String formSysID) {
        String topic = null;
        String str = jedisClientTemplate.hget(Common.CACHE_MAP_BUSINESS,formSysID);
        if(StringUtils.isNotEmpty(str))
        {
            BusinessSystem bean = JSON.parseObject(str, BusinessSystem.class);
            topic = bean.getKafkaTopicBean().getTopicName();
        }
        return topic;
    }

    /**
     * 缓存获取ServerCatalogBean
     * @return
     */
    public List<ServerCatalog> getServerCatalogBeansFromCache(){
        Map<String,String> map = jedisClientTemplate.hgetAll(Common.CACHE_MAP_CATALOG);
        List<ServerCatalog> list = new ArrayList<>();
        map.values().stream().forEach(s->{
            ServerCatalog bean = JSON.parseObject(s, ServerCatalog.class);
            list.add(bean);
        });
        return list;
    }

    /**
     * 发布数据时，通过msgType,formSysID获取对应的ServerCatalogBean
     * 根据msgType,formSysID两个字段查询发布服务，如果查询不到，说明参数错误
     */
    public ServerCatalog getServerCatalogBean(String msgType) {
        ServerCatalog serverCatalogBean = null;
        String str = jedisClientTemplate.hget(Common.CACHE_MAP_CATALOG,msgType);
        if(StringUtils.isNotEmpty(str)){
            serverCatalogBean = JSON.parseObject(str, ServerCatalog.class);
        }
        return serverCatalogBean;
    }

    public ServerSubscribes getServerSubscribesBean(String id){
        ServerSubscribes serverSubscribesBean = null;
        String str = jedisClientTemplate.hget(Common.CACHE_MAP_SUBSCRIBE,id);
        if(StringUtils.isNotEmpty(str)){
            serverSubscribesBean = JSON.parseObject(str, ServerSubscribes.class);
        }
        return serverSubscribesBean;
    }


    /**
     * 主动请求第三方数据时，验证web端是否订阅了第三方的数据服务，如果没有订阅，返回null
     */
    public ServerSubscribes getServerSubscribesBean(String formSysID, String msgType) {

        ServerSubscribes bean = null;
        String str = jedisClientTemplate.hget(Common.CACHE_MAP_SUBSCRIBE,formSysID+"_"+msgType);
        if(StringUtils.isNotEmpty(str)){
            bean = JSON.parseObject(str, ServerSubscribes.class);
        }
        return bean;
    }

    public boolean checkMsgSN(String msgsn){
        return jedisClientTemplate.get(Common.CACHE_MAP_MSGSN+msgsn)!=null;
    }

    public void recordMsgSN(String msgsn){
        jedisClientTemplate.set(Common.CACHE_MAP_MSGSN+msgsn,"");
        jedisClientTemplate.expire(Common.CACHE_MAP_MSGSN+msgsn,60 * 30);
    }

    public void disableInvalidMsgSN(){
        Map<String,String> re = jedisClientTemplate.hgetAll(Common.CACHE_MAP_MSGSN);
        if(re!=null && re.size()>0) {
            re.forEach((k, v) -> {
                Date old = gson.fromJson(v, Date.class);
                if ((System.currentTimeMillis() - old.getTime()) > (4 * 60 * 60 * 1000)) {
                    jedisClientTemplate.hdel(Common.CACHE_MAP_MSGSN, k);
                }
            });
        }
    }

    /**
     * 把待发布和待主动请求的数据保存到第三方缓存队列
     */
    public void saveMessageToCatch(ServerMessageDetail serverMessageDetailBean) {
        /**
         * 如果是发布数据 ，则存入待发布队列
         */
        boolean result;
        if (Common.XML_DATATYPE_FB.equals(serverMessageDetailBean.getMsgcode())
                || Common.XML_DATATYPE_THREEERROR.equals(serverMessageDetailBean.getMsgcode())) {
            ServerSubscribes sessionidBean = existsSessionID(serverMessageDetailBean.getSessionid());
            if (sessionidBean == null) {
                result = pushMessageQueue.offer(serverMessageDetailBean);
                while(!result)
                {
                    result = pushMessageQueue.offer(serverMessageDetailBean);
                }
            } else {
                result = requestReturnMessageQueue.offer(serverMessageDetailBean);
                while (!result) {
                    requestReturnMessageQueue.offer(serverMessageDetailBean);
                }
            }
        }
        /**
         * 如果是主动订阅数据 ，则存入待主动订阅数据
         */
        if (Common.XML_DATATYPE_REQUEST.equals(serverMessageDetailBean.getMsgcode())) {
            result = requestMessageQueue.offer(serverMessageDetailBean);
            while(!result)
            {
                result = requestMessageQueue.offer(serverMessageDetailBean);
            }
        }
    }

    /**
     * 保存或修改sessionid缓存
     * 参数说明： id为订阅bean的id
     */
    public void saveOrUpdateSessionid(String sessionid, int id) {
        if (!StringUtil.isNull(sessionid)) {
            /**
             * 主动请求时，需要把sessionid存入缓存， key 为sessionid_拼接session的值，以session_开头的目的是，方便以通配符进行查询所有的sessionid缓存
             */
            String key = Common.CATCH_SESSIONID_KEY_START + sessionid;
            jedisClientTemplate.set(key, String.valueOf(id));
            /**
             * 设置过期时间
             */
            jedisClientTemplate.expire(key, Common.SESSIONID_EXPIRE_SECONDS);
        }
    }


    /**
     * 验证sessionid是否存在与缓存中
     */
    public ServerSubscribes existsSessionID(String sessionid) {
        ServerSubscribes bean = null;
        String key = Common.CATCH_SESSIONID_KEY_START + sessionid;
        String id = jedisClientTemplate.get(key);
        /**
         * 根据订阅bean的id，从缓存中获取bean，只所以在sessionid缓存是，只把id缓存，而不是ServerSubscribesBean对象，目的是防止缓存后，订阅信息发生变化
         */
        if (!StringUtil.isNull(id)) {
            String str = jedisClientTemplate.hget(Common.CACHE_MAP_SUBSCRIBE, id);
            if (StringUtils.isNotEmpty(str)) {
                bean = JSON.parseObject(str, ServerSubscribes.class);
            }
        }
        return bean;
    }

    /**
     * 根据ID获取接口模板
     * @param id
     * @return
     */
    public ServerCatalogTemplate getCatalogTemplate(String id)
    {
        ServerCatalogTemplate bean = null;
        String str = jedisClientTemplate.hget(Common.CACHE_MAP_TEMPLATE,id);
        if(StringUtils.isNotEmpty(str))
        {
            bean = JSON.parseObject(str, ServerCatalogTemplate.class);
        }
        return bean;
    }

    /**
     * 获取待发布队列中增量信息
     */
    public ServerMessageDetail getPushMessage() {
        return pushMessageQueue.poll();
    }


    /**
     * 获取待路由主动请求队列中的信息
     */
    public ServerMessageDetail getRequestMessage() {
        return requestMessageQueue.poll();
    }

    /**
     * 获取待路由主动请求的回复队列中的信息
     */
    public ServerMessageDetail getRequestReturnMessage() {
        return requestReturnMessageQueue.poll();
    }


//    public void test() throws InterruptedException{
//    	
//    	jedisClientTemplate.set("name", "1");
//    	jedisClientTemplate.expire("name",10);
//    	while (true) {
//			Thread.sleep(5000);
//    	   System.err.println(jedisClientTemplate.exists("name")); 
//       	   jedisClientTemplate.set("name", "1");
//           jedisClientTemplate.expire("name",10);
//
//			
//		}
//    	
//    }


    /**
     * 查询增量分发数据的订阅者的集合
     */
    public List<ServerSubscribes> getServerSubscribesList(ServerMessageDetail messageBean) {

        List<ServerSubscribes> list = new ArrayList<ServerSubscribes>();
        if (messageBean != null) {
            String str = jedisClientTemplate.hget(Common.CACHE_MAP_SUBSCRIBE_LIST,messageBean.getMsgtype());
            if(StringUtils.isNotEmpty(str)){
                List<String> ids = JSON.parseArray(str,String.class);
                ids.forEach(id->{
                    String substr = jedisClientTemplate.hget(Common.CACHE_MAP_SUBSCRIBE,id);
                    if(StringUtils.isNotEmpty(substr)){
                        try {
                            ServerSubscribes sbean = JSON.parseObject(substr, ServerSubscribes.class);
                            list.add(sbean);
                        }
                        catch (Exception ex)
                        {
                            logger.error("获取订阅者集合错误！msgType:"+messageBean.getMsgtype()+", id:"+id+", str:"+substr);
                        }
                    }
                 });
            }

        }
        return list;
    }

    public void serverSubscribeListAdd(String msgType,String id){
        String str = jedisClientTemplate.hget(Common.CACHE_MAP_SUBSCRIBE_LIST,msgType);
        List<String> ids = null;
        if(StringUtils.isNotEmpty(str)){
            ids = JSON.parseArray(str,String.class);
        }
        else{
            ids = new ArrayList<>();
        }
        if(ids.stream().noneMatch(i->i.equals(id))) {
            ids.add(id);
        }
        jedisClientTemplate.hset(Common.CACHE_MAP_SUBSCRIBE_LIST,msgType,gson.toJson(ids));
    }

    public void serverSubscribeListRemove(String msgType,String id){
        String str = jedisClientTemplate.hget(Common.CACHE_MAP_SUBSCRIBE_LIST,msgType);
        List<String> ids = null;
        if(StringUtils.isNotEmpty(str)){
            ids = JSON.parseArray(str,String.class);
            ids.remove(id);
        }
        jedisClientTemplate.hset(Common.CACHE_MAP_SUBSCRIBE_LIST,msgType,gson.toJson(ids));
    }


    /**
     * 删除发布，订阅某缓存
     */
    public void delCatch(String key, String field) {
        jedisClientTemplate.hdel(key, field);
    }


    /**
     * 添加或者覆盖发布，订阅
     */
    public void updateCatch(String key, String field, Object object) {
        jedisClientTemplate.hset(key, field, gson.toJson(object));
    }
}
