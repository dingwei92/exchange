package com.topsci.db.service;

import java.util.*;
import java.util.concurrent.*;

import com.topsci.bean.*;
import com.topsci.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.topsci.db.dao.BusinessSystemDao;
import com.topsci.db.dao.CatalogDao;
import com.topsci.db.dao.ChangeTaskDao;
import com.topsci.db.dao.KafKaTopicDao;
import com.topsci.db.dao.MessageDao;
import com.topsci.db.dao.SubscribesDao;
import com.topsci.util.DateUtil;

/**
 * 数据处理service， 因业务模块区分不明显，暂时公用一个service
 * @author yanhan
 *
 */
public class YwDBService implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(YwDBService.class);


	private CatalogDao catalogDao;
	private SubscribesDao subscribesDao;
	private BusinessSystemDao businessSystemDao;
	private KafKaTopicDao kafKaTopicDao;
	private MessageDao messageDao;
	private ChangeTaskDao changeTaskDao;
	private ArrayBlockingQueue<MessageInfo> dataQueue;
	/**
	 */
	private static YwDBService ywDBService = null;
	private YwDBService(){
		this.catalogDao = new CatalogDao();
		this.subscribesDao = new SubscribesDao();
		this.businessSystemDao = new BusinessSystemDao();
		this.kafKaTopicDao = new KafKaTopicDao();
		this.messageDao = new MessageDao();
		this.changeTaskDao = new ChangeTaskDao();
		dataQueue = new ArrayBlockingQueue<>(2000);

	}
	public static YwDBService  getInstance(){
		if(ywDBService==null){
			synchronized(YwDBService.class){
				if(ywDBService==null)
					ywDBService = new YwDBService();
			}
		}
		return ywDBService;
	}

	@Override
	public void run() {
		//dealServerCatalogMessage();
	}

	/**
	 * 
	 * 对发布服务关联的kafka信息，业务系统信息，发布字段详情，进行查询
	 */
	public ServerCatalog creatCacheServerCatalogBean(ServerCatalog bean){
		List<ServerCatalogList> catalogMores = null;
		BusinessSystem businessSystemBean = null;
		/**
		 * 根据业务系统id查询对应的业务系统数据bean
		 */
		//businessSystemBean = getBusinessSystemBeanByID(bean.getBusinessSystemId());
		
		//if(businessSystemBean!=null){
			/**
			 * 业务系统英文简写，发布服务简写，统一修改成大写，这样可以忽略xml头信息的系统编号，服务编号 大小写，  topicname区分大小写，web端键入什么topicname，就发送什么topicname
			 *
			 */
			bean.setBusinessSystemBean(businessSystemBean);
			
			catalogMores = catalogDao.getServerCatalogMoreByID(bean.getTemplateId());
			if(catalogMores!=null&&catalogMores.size()>0){
				bean.setServerCatalogList(catalogMores); //属性集合作为发布服务对象的子属性
				return bean;
			}
		//}
		return null;
	}


	/**
	 * 根据服务名获取模板bean
	 * @param msgType
	 * @return
	 */
	/*public ServerCatalogTemplate getTemplateByCatalogShort(String msgType)
	{
		ServerCatalogTemplate tplbean = null;
		ServerCatalog bean = CatchAbout.getInstance().getServerCatalogBean(msgType);
		if(bean!=null)
		{
			tplbean = CatchAbout.getInstance().getCatalogTemplate(bean.getTemplateId());
		}
		return tplbean;
	}*/
	
	public ServerSubscribes creatCacheServerSubscribesBean(ServerSubscribes bean){
		
		List<ServerCatalogList> catalogMores = null;
		BusinessSystem businessSystemBean = null;
		ServerCatalog serverCatalogBean = null;
		/**
		 * 根据业务系统id查询对应的业务系统数据bean
		 */
		businessSystemBean = getBusinessSystemBeanByID(bean.getBusinessSystemId());
		/**
		 * 根据server_catalog_id查询订阅的分发服务bean
		 */
		serverCatalogBean = catalogDao.getServerCatalogByID(bean.getServerCatalogId());
		if(serverCatalogBean!=null){
			serverCatalogBean = creatCacheServerCatalogBean(serverCatalogBean);
		}
		if(businessSystemBean!=null&&serverCatalogBean!=null){
			
			bean.setBusinessSystemBean(businessSystemBean);
            bean.setServerCatalogBean(serverCatalogBean);
            
			catalogMores = subscribesDao.getServerSubscribesMoreByID(bean.getId());
			if(catalogMores!=null&&catalogMores.size()>0){
				bean.setServerCatalogList(catalogMores);
				return bean;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * 初始化发布服务的缓存， 启动应用时
	 */
	public Map<String, ServerCatalog> initServerCatalogCache(){
		Map<String, ServerCatalog> map = new ConcurrentHashMap<String, ServerCatalog>();
		List<ServerCatalog> list = catalogDao.getServerCatalogs();
		if(list!=null&&list.size()>0){
			map = new HashMap<String, ServerCatalog>();
			ServerCatalog bean = null;
			for(ServerCatalog s:list){
				bean = creatCacheServerCatalogBean(s);
				if(bean!=null){
					map.put(bean.getId()+"_"+bean.getServerShort(),bean);
				}
			}
		}
		return map;
	}

	/**
	 *
	 * 初始化发布服务的缓存， 启动应用时
	 */
	public Map<String, ServerCatalogTemplate> initServerCatalogTemplateCache(){
		Map<String, ServerCatalogTemplate> map = new ConcurrentHashMap<String, ServerCatalogTemplate>();
		List<ServerCatalogTemplate> list = catalogDao.getServerCatalogTemplates();
		if(list!=null&&list.size()>0){
			map = new HashMap<String, ServerCatalogTemplate>();
			for(ServerCatalogTemplate s:list){
				map.put(String.valueOf(s.getId()),s);
			}
		}
		return map;
	}
	
	
	/**
	 * 
	 * 初始化订阅服务的缓存， 启动应用吧
	 */
	public Map<String, ServerSubscribes> initServerSubscribesCache(){
		Map<String, ServerSubscribes> map = new ConcurrentHashMap<String, ServerSubscribes>();
		List<ServerSubscribes> list = subscribesDao.getServerSubscribes();
		if(list!=null&&list.size()>0){
			map = new HashMap<String, ServerSubscribes>();
			ServerSubscribes bean = null;
			for(ServerSubscribes s:list){
				bean = creatCacheServerSubscribesBean(s);
				if(bean!=null){
					
					/**
					 * 订阅信息放入缓存
					 */
					map.put(bean.getBusinessSystemBean().getSystemShort()+"_"+bean.getServerCatalogBean().getServerShort(),bean);
				}
			}
		}
		return map;
	}
	
	public Map<String, BusinessSystem> initBusinessSystemCache(){
		List<BusinessSystem> beans =  businessSystemDao.getBusinessSystemBeans();
		beans.forEach(businessSystemBean ->businessSystemBean.setKafkaTopicBean(kafKaTopicDao.getKafkaTopicBeanByID(businessSystemBean.getKafkaTopicId())));
		Map<String, BusinessSystem> map = new ConcurrentHashMap<>();
		if(beans != null){
			beans.forEach(b->map.put(b.getSystemShort(),b));
		}
		return map;
	}
	
	/**
	 * 保存错误的xml记录到数据库
	 */
	public void saveMessageError(ServerMessageError bean){
		messageDao.saveMessageError(bean);
	}
	

	public void saveServerCatalogMessage(ServerMessageDetail bean, List<ServerSubscribesMessage> sbmessage, ServerCatalog serverCatalogBean)
	{
		MessageInfo info = new MessageInfo(bean,sbmessage,serverCatalogBean);
		try {
			dataQueue.put(info);
		}
		catch (Exception ex)
		{
			logger.error("保存消息至数据库失败！");
		}
	}


	/**
	 * 保存分发记录
	 */

	public void dealServerCatalogMessage(){
		
		List<MessageInfo> lists = new ArrayList<>();
		dataQueue.drainTo(lists,500);
		if(lists != null && lists.size()>0) {
				List<Object[]> smdlist = new ArrayList<>();
				List<Object[]> scmlist = new ArrayList<>();
				List<Object[]> ssmlist = new ArrayList<>();
				for (MessageInfo mi : lists) {
					ServerMessageDetail bean = mi.getBean();
					List<ServerSubscribesMessage> sbmessage = mi.getSbmessage();
					ServerCatalog serverCatalogBean = mi.getServerCatalogBean();
					String uuid = StringUtil.getUUID();

					Object[] params1 = {uuid,bean.getMsgsn(), bean.getMsgsenttime(), bean.getFormsysid(), bean.getTosysid(), bean.getMsgoid(), bean.getMsgotime(), bean.getMsgtype(), bean.getMsgnum(), bean.getMsgcount(), bean.getMsgdata(), bean.getLastdate(), bean.getSessionid(), bean.getMsgcode()};
//					dbService.update("INSERT INTO server_message_detail (ID,MSGSN,MSGSENTTIME,FORMSYSID,TOSYSID,MSGOID,MSGOTIME,MSGTYPE,MSGNUM,MSGCOUNT,MSGDATA,LASTDATE,SESSIONID,MSGCODE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)", params1);
					smdlist.add(params1);
					Object[] params2 = {serverCatalogBean.getBusinessSystemId(), serverCatalogBean.getId(), uuid, DateUtil.getNow(), "Y"};
//					dbService.update("INSERT INTO server_catalog_message (BUSINESS_SYSTEM_ID,SERVER_CATALOG_ID,SERVER_MESSAGE_DETAIL_ID,LASTDATE,STATUS) VALUES (?,?,?,?,?)", params2);
					scmlist.add(params2);

					/**
					 * 增加发布信息记录
					 */
					if (sbmessage != null && sbmessage.size() > 0) {
						List<Object[]> list = new ArrayList<Object[]>();
						for (ServerSubscribesMessage b : sbmessage) {
							Object[] p = {b.getBusiness_system_id(), b.getServer_subscribes_id(), uuid, "Y", DateUtil.getNow()};
							list.add(p);
						}
//						dbService.updates("insert into server_subscribes_message (business_system_id,server_subscribes_id,server_message_detail_id,status,lastdate) values (?,?,?,?,?)", list);
						ssmlist.addAll(list);
					}
				}
				/*dbService.updates("INSERT INTO server_message_detail (ID,MSGSN,MSGSENTTIME,FORMSYSID,TOSYSID,MSGOID,MSGOTIME,MSGTYPE,MSGNUM,MSGCOUNT,MSGDATA,LASTDATE,SESSIONID,MSGCODE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)", smdlist);
				dbService.updates("INSERT INTO server_catalog_message (BUSINESS_SYSTEM_ID,SERVER_CATALOG_ID,SERVER_MESSAGE_DETAIL_ID,LASTDATE,STATUS) VALUES (?,?,?,?,?)", scmlist);
				dbService.updates("insert into server_subscribes_message (business_system_id,server_subscribes_id,server_message_detail_id,status,lastdate) values (?,?,?,?,?)", ssmlist);
				dbService.commit();*/
		}
	}
	
	/**
	 * 保存主动发送请求记录
	 */
	public boolean saveSubscribesCataLogMessage(
			ServerSubscribes serverSubscribesBean,
			ServerMessageDetail bean) {/*

		boolean flag = true;
		DBService dbService = null;
		try {
			dbService = new DBService();
			dbService.setAutoCommit(false);
			String uuid = StringUtil.getUUID();
			Object[] params1 = { uuid,bean.getMsgsn(), bean.getMsgsenttime(),
					bean.getFormsysid(), bean.getTosysid(), bean.getMsgoid(),
					bean.getMsgotime(), bean.getMsgtype(),
					 bean.getMsgnum(),
					bean.getMsgcount(), bean.getMsgdata(), bean.getLastdate(),
					bean.getSessionid(), bean.getMsgcode() };
			dbService.update("INSERT INTO server_message_detail (ID,MSGSN,MSGSENTTIME,FORMSYSID,TOSYSID,MSGOID,MSGOTIME,MSGTYPE,MSGNUM,MSGCOUNT,MSGDATA,LASTDATE,SESSIONID,MSGCODE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)",params1);

			Object[] params2 = { serverSubscribesBean.getBusiness_system_id(),
					serverSubscribesBean.getServer_catalog_id(),
					uuid, DateUtil.getNow(), "Y" };
			dbService
					.update("INSERT INTO subscribes_catalog_message (BUSINESS_SYSTEM_ID,SERVER_CATALOG_ID,SERVER_MESSAGE_DETAIL_ID,LASTDATE,STATUS) VALUES (?,?,?,?,?)",
							params2);

			dbService.commit();
		} catch (Exception e) {
			flag = false;
			try {
				dbService.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			logger.error("存储主动请求订阅XML记录数据时失败！", e);
		} finally {
			if (dbService != null) {
				try {
					dbService.close();
				} catch (SQLException e) {
					logger.error("关闭数据库连接错误！", e);
				}
			}
		}*/

		return true;
	}
	/**
	 * 查询尚未处理的变更记录
	 */
	public List<ServerChangeTask> getServerChangeTasks(){
		return changeTaskDao.getServerChangeTasks();
	}

	/**
	 * 查询尚未处理的历史订阅记录
	 */
	public List<ServerHistoryApply> getServerHistoryApplyTasks()
	{
		return changeTaskDao.getServerHistoryApplyBean();
	}

	/**
	 *
	 */
	public void updateServerHistoryApplyStatus(int historyid)
	{
		subscribesDao.updateServerHistoryApply(historyid);
	}

	/**
	 *
	 */
	public Map<String,String> getHistoryFromToParam(int historyid)
	{
		return subscribesDao.getHistoryRequestParams(historyid);
	}

	public List<String[]> getConditionedSubscribeListBean(int subid)
	{
		return subscribesDao.getConditionedServerSubscribeListBean(subid);
	}

	public void updateServerHistoryApply(int historyid)
	{
		subscribesDao.updateServerHistoryApply(historyid);
	}

	/**
	 * 根据ID修改状态
	 */
	public void updatechangeTaskStatusByID(int id){
		changeTaskDao.updateStatusByID(id);
	}
	
	/**
	 * 根据id查询订阅服务bean 
	 */
	public ServerSubscribes getServerSubscribesBeanByID(int id){
		return subscribesDao.getServerSubscribesBeanByID(id);
	}
	/**
	 * 根据id查询发布服务bean 
	 */
	public ServerCatalog getServerCatalogByID(int id){
		return catalogDao.getServerCatalogByID(id);
	}

	/**
	 * 根据模板id查询发布服务bean
	 */
	public List<ServerCatalog> getServerCatalogByTemplateID(int id){
		return catalogDao.getServerCatalogByTemplateID(id);
	}
	
	/**
	 * 验证msgSN是否存在
	 */
	public boolean checkMsgSN(String msgSN){
		return messageDao.checkMsgSN(msgSN);
	}
	
	/**
	 * 根据业务系统id查询bean
	 */
	public BusinessSystem getBusinessSystemBeanByID(int id){
		BusinessSystem businessSystemBean = businessSystemDao.getBusinessSystemBeanByID(id);
		if(businessSystemBean!=null){
			KafkaTopic kafkaTopicBean = kafKaTopicDao.getKafkaTopicBeanByID(businessSystemBean.getKafkaTopicId());
			if(kafkaTopicBean!=null){
				businessSystemBean.setKafkaTopicBean(kafkaTopicBean);
			}else{
				return null;
			}
		}
		return businessSystemBean;
	}
	
	/**
	 * 主动请求时，检查是否有相同的SessionID
	 */
	public boolean haveSessionID(String sessionid){
		return messageDao.haveSessionID(sessionid);
	}
	
	/**
	 * 主动请求时， 对主动请求请求次数进行限制,time分钟之内不能再次请求
	 */
	public boolean checkRequest(String formSysID,String msgType){
		return messageDao.checkRequest(formSysID, msgType);
	}

	/**
	 * 检查接口的模板是否在有效期内
	 * @param id
	 * @return
	 *//*
	public boolean checkCatalogTemplateValid(String id)
	{
		ServerCatalogTemplate bean = CatchAbout.getInstance().getCatalogTemplate(id);
		if(!"V".equals(bean.getStatus()) || StringUtils.isEmpty(bean.getInvalidTime()))
		{
			return true;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
		try {
			return sdf.parse(bean.getInvalidTime()).getTime() > new Date().getTime();
		}
		catch (Exception ex){
			return true;
		}
	}
*/
	/**
	 * 获取接口模板
	 * @param id
	 * @return
	 */
	public ServerCatalogTemplate getCatalogTemplate(String id)
	{
		return catalogDao.getServerCatalogTemplate(id);
	}

	public Map<String,Date> initMSGSNCache(){
		return messageDao.getValidMsgSN();
	}
}

class MessageInfo{
	private ServerMessageDetail bean;
	private List<ServerSubscribesMessage> sbmessage;
	private ServerCatalog serverCatalogBean;

	public MessageInfo(ServerMessageDetail bean, List<ServerSubscribesMessage> sbmessage, ServerCatalog serverCatalogBean){
		this.bean = bean;
		this.sbmessage = sbmessage;
		this.serverCatalogBean = serverCatalogBean;
	}

	public ServerMessageDetail getBean() {
		return bean;
	}

	public void setBean(ServerMessageDetail bean) {
		this.bean = bean;
	}

	public List<ServerSubscribesMessage> getSbmessage() {
		return sbmessage;
	}

	public void setSbmessage(List<ServerSubscribesMessage> sbmessage) {
		this.sbmessage = sbmessage;
	}

	public ServerCatalog getServerCatalogBean() {
		return serverCatalogBean;
	}

	public void setServerCatalogBean(ServerCatalog serverCatalogBean) {
		this.serverCatalogBean = serverCatalogBean;
	}
}
