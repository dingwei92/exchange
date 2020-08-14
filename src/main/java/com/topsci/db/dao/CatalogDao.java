package com.topsci.db.dao;

import java.time.LocalDateTime;
import java.util.List;

import com.topsci.bean.*;
import com.topsci.util.MongodbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.mongodb.core.query.Criteria;


@SuppressWarnings("unchecked")
public class CatalogDao {
	
	private static final Logger logger = LoggerFactory.getLogger(CatalogDao.class);

	/**
	 * 根据id查询发布服务bean 
	 */
	public ServerCatalog getServerCatalogByID(int id){
		ServerCatalog bean = null;
		try {
			String[] keys = {"id","deleted"};
			Object[] vals = {id,"N"};
			bean = (ServerCatalog) MongodbUtils.findOne(ServerCatalog.class,keys,vals);
		} catch (Exception e) {
			logger.error("根据ID查询分发服务Bean错误！",e);
		}
		return bean;
	}

	/**
	 * 根据接口名称查询发布服务bean
	 */
	public ServerCatalog getServerCatalogByServerShort(String name){
		ServerCatalog bean = null;
		try {
			String[] keys = {"server_short","deleted"};
			Object[] vals = {name,"N"};
			bean = (ServerCatalog) MongodbUtils.findOne(ServerCatalog.class,keys,vals);
		} catch (Exception e) {
			logger.error("根据接口名称查询分发服务Bean错误！",e);
		}
		return bean;
	}

	/**
	 * 根据模板id查询发布服务bean
	 */
	public List<ServerCatalog> getServerCatalogByTemplateID(int id){
		List<ServerCatalog> beans = null;
		try {
			String[] keys = {"template_id","deleted"};
			Object[] vals = {id,"N"};
			beans = (List<ServerCatalog>)MongodbUtils.find(ServerCatalog.class,keys,vals);
		} catch (Exception e) {
			logger.error("根据ID查询分发服务Bean错误！",e);
		}
		return beans;
	}
	
	/**
	 * 查询分发服务集合
	 */
	
	public List<ServerCatalog> getServerCatalogs(){
		List<ServerCatalog> list = null;
		try {
			String[] keys = {"deleted"};
			Object[] vals = {"N"};
			list = (List<ServerCatalog>)MongodbUtils.find(ServerCatalog.class,keys,vals);
		} catch (Exception e) {
			logger.error("查询发布服务集合出错！",e);
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 根据分发服务id，查询该分发服务详细字段集合
	 */
	public List<ServerCatalogList> getServerCatalogMoreByID(String id){
		List<ServerCatalogList> list = null;
		try {
			String[] keys = {"serverCatalogId"};
			Object[] vals = {Integer.parseInt(id)};
			list = (List<ServerCatalogList>)MongodbUtils.find(ServerCatalogList.class,keys,vals);
		} catch (Exception e) {
			logger.error("根据分发服务id，查询该分发服务详细字段集合出错！",e);
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 根据接口发布方式为数据库的查询接口集合
	 * @return
	 */
	public List<ServerCatalog> getServerCatalogsForDB(){
		List<ServerCatalog> list = null;
		try {
			String[] keys = {"publish_type","deleted"};
			Object[] vals = {"2","N"};
			list = (List<ServerCatalog>)MongodbUtils.find(ServerCatalog.class,keys,vals);
		} catch (Exception e) {
			logger.error("查询发布服务集合出错！",e);
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 获取接口的数据库信息
	 * @param uuid
	 * @return
	 */
	public ServerCatalogDBInfo getServerCatalogDBInfo(String uuid)
	{
		ServerCatalogDBInfo bean = null;
		try {
			String[] keys = {"uuid"};
			Object[] vals = {uuid};
			bean = (ServerCatalogDBInfo) MongodbUtils.findOne(ServerCatalogDBInfo.class,keys,vals);
		} catch (Exception e) {
			logger.error("根据ID查询分发服务Bean错误！",e);
		}
		return bean;
	}

	/**
	 * 获取接口的数据库详细信息
	 * @param uuid
	 * @return
	 */
	public ServerCatalogDBPub getServerCatalogDBPub(String uuid)
	{
		ServerCatalogDBPub bean = null;
		try {
			String[] keys = {"uuid"};
			Object[] vals = {uuid};
			bean = (ServerCatalogDBPub) MongodbUtils.findOne(ServerCatalogDBPub.class,keys,vals);
		} catch (Exception e) {
			logger.error("根据ID查询分发服务Bean错误！",e);
		}
		return bean;
	}

	/**
	 * 获取接口模板信息
	 * @param id
	 * @return
	 */
	public ServerCatalogTemplate getServerCatalogTemplate(String id)
	{
		ServerCatalogTemplate bean = null;
		try {
			String[] keys = {"id"};
			Object[] vals = {Integer.parseInt(id)};
			bean = (ServerCatalogTemplate) MongodbUtils.findOne(ServerCatalogTemplate.class,keys,vals);
		} catch (Exception e) {
			logger.error("根据ID查询分发服务Bean错误！",e);
		}
		return bean;
	}

	/**
	 * 获取所有接口模板信息
	 * @return
	 */
	public List<ServerCatalogTemplate> getServerCatalogTemplates()
	{
		List<ServerCatalogTemplate> beans = null;
		try {
			Criteria criteria = new Criteria();
			criteria.orOperator(Criteria.where("status").is("Y"),Criteria.where("status").is("A"),Criteria.where("status").is("V"));
			beans = (List<ServerCatalogTemplate>)MongodbUtils.find(ServerCatalogTemplate.class,criteria);
		} catch (Exception e) {
			logger.error("根据ID查询分发服务Bean错误！",e);
		}
		return beans;
	}

	/**
	 * 更新接口数据库发布任务执行完成时间
	 * @param id
	 */
	public void updateCatalogDBPublishTime(String id)
	{
		try {
			String[] key1 = {"last_run"};
			Object[] val = {LocalDateTime.now()};
			MongodbUtils.updateFirst("uuid",id,key1,val,"serverCatalogDbPub");
		}catch (Exception ex){
			logger.error("保存接口"+id+"数据库发布任务执行完成时间错误！",ex);
		}
	}

	/**
	 * 获取未处理的数据库发布接口的主动任务
	 * @return
	 */
	public List<ServerCatalogDBACPub> getUndealtACPubBeans()
	{
		List<ServerCatalogDBACPub> beans = null;
		try
		{

			String[] keys = {"status"};
			Object[] vals = {"N"};
			beans = (List<ServerCatalogDBACPub>)MongodbUtils.find(ServerCatalogDBACPub.class,keys,vals);
		}
		catch (Exception ex)
		{
			logger.error("获取未处理的数据库发布接口的主动任务失败",ex);
		}
		return beans;
	}

	public void updateDealtACPubBean(String uuid)
	{
		try {
			String[] key1 = {"status"};
			Object[] val = {"D"};
			MongodbUtils.updateFirst("uuid",uuid,key1,val,"serverCatalogDbAcpub");
		}catch (Exception ex){
			logger.error("保存接口"+uuid+"主动发布任务执行完成错误！",ex);
		}
	}

}
