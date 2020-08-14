package com.topsci;

import com.topsci.common.CatchAbout;
import com.topsci.common.InitParam;
import com.topsci.config.PropertiesBean;
import com.topsci.db.service.YwDBService;
import com.topsci.edgexfoundry.ProducerDataThread;
import com.topsci.edgexfoundry.ZeroMQEventSubscriber;
import com.topsci.service.DeviceInfoService;
import com.topsci.task.*;
import com.topsci.util.DateUtil;
import com.topsci.web.log.WebLogChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;


/**
* @author starry
 * @ClassName: SysInit
* @Description: 交换平台
*/
@Component
public class SysInit implements ApplicationRunner {
	private static final Logger logger = LoggerFactory.getLogger(SysInit.class);

	@Autowired
    private ZeroMQEventSubscriber zeroMQEventSubscriber;
	@Autowired
    private DeviceInfoService deviceService;
	@Autowired
	private PropertiesBean  propertiesBean;
	@Override
	public void run(ApplicationArguments args){
		/*BusinessSystem businessSystemBean = new BusinessSystem();
		businessSystemBean.setId(2);
		businessSystemBean.setRemark("1");
		businessSystemBean.setSystemName("a2a");
		MongodbUtils.save(businessSystemBean);

		ServerCatalogList serverCatalogListBean = new ServerCatalogList();
		serverCatalogListBean.setId(1);
		serverCatalogListBean.setServerElementName("1");
		MongodbUtils.save(serverCatalogListBean);
		//ServerCatalogListBean
*/
		/*LookupOperation lookupToLots = LookupOperation.newLookup().
				from("businessSystemBean").//关联表名 lots
				localField("remark").//关联字段
				foreignField("server_element_name").//主表关联字段对应的次表字段
				as("entity");//查询结果集合名

		Criteria criteriaToLots = Criteria.where("_id").is(1);
		AggregationOperation matchToLots = Aggregation.match(criteriaToLots);

		Aggregation counts = Aggregation.newAggregation(lookupToLots,matchToLots);

		List<ServerCatalogList> lsit111 = (List<ServerCatalogList>)MongodbUtils.aggregate(counts,"serverCatalogListBean" , ServerCatalogList.class);
*/
		/*LookupOperation lookupToLots = LookupOperation.newLookup().
				from("serverCatalogList").//关联表名 lots
				localField("serverCatalogListId").//关联字段
				foreignField("_id").//主表关联字段对应的次表字段
				as("entity");//查询结果集合名
		Criteria criteriaToLots = Criteria.where("serverSubscribesId").is(2).and("conType").is(0);
		AggregationOperation matchToLots = Aggregation.match(criteriaToLots);
		Aggregation counts = Aggregation.newAggregation(lookupToLots,matchToLots);
		MongodbUtils.aggregate(counts,"serverSubscribesList" , Map.class);*/



		logger.info("====启动服务开始====");
		InitParam init = new InitParam();
		//初始化配置文件
		if(init.initConfig()){
			CatchAbout catchAbout = CatchAbout.getInstance();
			//验证DB服务器是否正常
			if(init.checkDB()&&catchAbout.testRedis()){
//				//初始化schema缓存
				//if(init.initSchema()){}
				CatchAbout.receivePool = Executors.newFixedThreadPool(propertiesBean.getReceivePoolSize());
				CatchAbout.sendPool = Executors.newFixedThreadPool(propertiesBean.getSendPoolSize()*6);

				//初始化订阅，发布的服务自定义字段属性缓存
				logger.info("===初始化缓存开始===");
				catchAbout.initCache();
				logger.info("===初始化缓存结束===");


                //监控客户端状态
                //new Thread(new ConsumerDataThread(propertiesBean.getClientAliveTopic(), ClientAliveThread.class)).start();
                //new Thread(new StreamClientThread()).start();
                //ClientAliveCheckTask clientConnectCheck = new ClientAliveCheckTask();
                //ScheduledExecutorService threadPool = Executors.newSingleThreadScheduledExecutor();
                //threadPool.scheduleWithFixedDelay(clientConnectCheck,0,1, TimeUnit.MINUTES);


                //监控接收消息的zeromq
                new Thread(new ProducerDataThread(zeroMQEventSubscriber)).start();
                //监听日志请求的topic
                //new Thread(new ConsumerDataThread(propertiesBean.getLogRequestTopic(), WebLogListenerThread.class)).start();
                //new Thread(new StreamWebLogThread()).start();
                new Thread(new WebLogChecker()).start();

				/**
				 * 启动定时任务（分发服务，路由服务，缓存更新）
				 */
				ScheduledExecutorService taskThreadPool = Executors.newScheduledThreadPool(3);
				taskThreadPool.scheduleWithFixedDelay(new CacheInitTask(),0,60,TimeUnit.SECONDS);//更新发布订阅缓存任务  10秒轮询一次
				taskThreadPool.scheduleWithFixedDelay(new HistorySubscribeCheckTask(),0,60,TimeUnit.SECONDS);//更新历史订阅缓存任务  60秒轮询一次
//				taskThreadPool.scheduleWithFixedDelay(new RedisExpireTask(),30,30,TimeUnit.MINUTES);
				ScheduledExecutorService taskThreadPool1 = Executors.newScheduledThreadPool(propertiesBean.getDistributeThread());
				IntStream.range(1, propertiesBean.getDistributeThread()).forEach(o->taskThreadPool1.scheduleWithFixedDelay(YwDBService.getInstance(),2000, propertiesBean.getDistributeThreadDelay(),TimeUnit.MILLISECONDS));
				String[] times = propertiesBean.getDataStaticTaskTime().split(":");
				ScheduledExecutorService taskThreadPool2 = Executors.newScheduledThreadPool(1);
				taskThreadPool2.scheduleAtFixedRate(new DataStatisticTask(), DateUtil.getDelay(Integer.parseInt(times[0]),
						Integer.parseInt(times[1]),Integer.parseInt(times[2])),24 * 60 * 60 * 1000,TimeUnit.MILLISECONDS);

				/**
				 * 分发不采用定时轮询，而是不间断轮询
				 *
				 * 发布线程数为路由主动请求线程数的4倍
				 */
				int poolsize = propertiesBean.getSendPoolSize();
				if(poolsize==0){
					poolsize = 1;
				}
				try {
					for(int i=0;i<poolsize*4;i++){
						CatchAbout.sendPool.execute(new PushTask());
//						Thread.sleep(100);
					}
					for(int i=0;i<poolsize;i++){
						CatchAbout.sendPool.execute(new RequestTask());
//						Thread.sleep(100);
					}
					for(int i=0;i<poolsize;i++){
						CatchAbout.sendPool.execute(new RequestReturnTask());
//						Thread.sleep(100);
					}

				} catch (Exception e) {
					logger.error("启动推送线程异常！",e);
				}
				logger.info("====启动服务正常====");
			}
		}
	}
}