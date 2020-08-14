package com.topsci.db.dao;

import java.util.*;
import java.util.stream.Collectors;

import com.topsci.bean.*;
import com.topsci.util.MongodbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;

@SuppressWarnings("unchecked")
public class SubscribesDao {
	private static final Logger logger = LoggerFactory.getLogger(SubscribesDao.class);

	/**
	 * 查询订阅服务集合
	 */
	public List<ServerSubscribes> getServerSubscribes(){
		List<ServerSubscribes> list = null;
		try {
			String[] keys = {"deleted"};
			Object[] vals = {"N"};
			list = (List<ServerSubscribes>) MongodbUtils.find(ServerSubscribes.class,keys,vals);
		} catch (Exception e) {
			logger.error("查询订阅服务集合出错！",e);
		}
		return list;
	}
	
	
	/**
	 * 根据订阅服务id，查询订阅的详细字段
	 */
	public List<ServerCatalogList> getServerSubscribesMoreByID(int id){
		List<ServerCatalogList> list = null;
		try {
		/*	String sql = "select b.ID,b.SERVER_CATALOG_ID,b.SERVER_ELEMENT_NAME,b.SERVER_ELEMENT_DESCRIBE,b.ELEMENT_DESCRIBE,b.LASTDATE,b.KEY_COL,b.DB_KEY " +
					"from server_subscribes_list a,server_catalog_list b where a.server_catalog_list_id=b.id and a.server_subscribes_id=?";
			String[] beanArray = new String[]{"id","server_catalog_id","server_element_name","server_element_describe","element_describe","lastdate","key_col","db_col"};
			Object[] params = {id};
			list =  DBTools.getListBean(ServerCatalogListBean.class, sql, params, beanArray);*/


			String[] keys = {"serverSubscribesId"};
			Object[] vals = {id};
			List<ServerSubscribesList> subscribesLists = (List<ServerSubscribesList>)MongodbUtils.find(ServerSubscribesList.class,keys,vals);
			List<Integer> listids = subscribesLists.stream().map(l -> l.getServerCatalogListId()).collect(Collectors.toList());
			Criteria criteria = new Criteria();
			criteria.and("id").in(listids);
			list = (List<ServerCatalogList>)MongodbUtils.find(ServerCatalogList.class,criteria);
		} catch (Exception e) {
			logger.error("根据订阅服务id，查询订阅的详细字段出错！",e);
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 根据id查询订阅服务bean 
	 */
	public ServerSubscribes getServerSubscribesBeanByID(int id){
		ServerSubscribes bean = null;
		try {
			String[] keys = {"deleted","id"};
			Object[] vals = {"N",id};
			bean = (ServerSubscribes) MongodbUtils.findOne(ServerSubscribes.class,keys,vals);
		} catch (Exception e) {
			logger.error("根据ID查询订阅服务Bean错误！",e);
			e.printStackTrace();
		}
		return bean;
	}

	/**
	 * 根据历史订阅表id查询请求相关参数
	 * map的key包括fromsysid,tosysid,msgtype
	 * @param historyid
	 * @return
	 */
	public Map<String,String> getHistoryRequestParams(int historyid)
	{
		Map<String,String> result = new HashMap<>();
		try
		{
			/*String sql = "select bs.SYSTEM_SHORT,sc.SERVER_SHORT,BS2.SYSTEM_SHORT
			from BUSINESS_SYSTEM bs,SERVER_CATALOG sc,SERVER_SUBSCRIBES ss,BUSINESS_SYSTEM bs2,SERVER_HISTORY_APPLY sh " +
					"where bs.ID=SS.BUSINESS_SYSTEM_ID and sh.ID="+historyid+" and sh.server_subscribes_id = ss.ID  and ss.SERVER_CATALOG_ID = SC.ID and BS2.ID=sc.BUSINESS_SYSTEM_ID";
			String[] params = DBTools.getStringArray(sql);
			result.put("fromsysid",params[0]);
			result.put("tosysid",params[2]);
			result.put("msgtype",params[1]);*/


			/*LookupOperation lookupToLots = LookupOperation.newLookup().
					from("serverSubscribesList").//关联表名 lots
					localField("server_catalog_list_id").//关联字段
					foreignField("id").//主表关联字段对应的次表字段
					as("entity");//查询结果集合名
			Criteria criteriaToLots = Criteria.where("entity.server_subscribes_id").is(subid).and("entity.con_type").nin(0);
			AggregationOperation matchToLots = Aggregation.match(criteriaToLots);
			Aggregation counts = Aggregation.newAggregation(lookupToLots,matchToLots);*/

		}catch (Exception ex)
		{
			logger.error("查询历史订阅{}的from，to和msgtype错误！",historyid,ex);
		}
		return result;
	}

	/**
	 * 更新历史订阅
	 * @param historyid
	 */
	public void updateServerHistoryApply(int historyid)
	{
		try
		{
			String[] key1 = {"status"};
			Object[] val = {"Y"};
			MongodbUtils.updateFirst("id",historyid,key1,val,"serverHistoryApply");
		}catch (Exception ex)
		{
			logger.error("更新历史订阅{}状态为Y错误！",historyid,ex);
		}
	}

	public List<String[]> getConditionedServerSubscribeListBean(int subid)
	{
		List<String[]> beans = new ArrayList<>();
		try
		{
			/*String sql = "select c.SERVER_ELEMENT_NAME, s.CON_TYPE,s.CON_VALUE,c.ELEMENT_DESCRIBE from
			SERVER_SUBSCRIBES_LIST s,SERVER_CATALOG_LIST c" +
					" WHERE SERVER_SUBSCRIBES_ID = "+subid+" and c.ID = s.SERVER_CATALOG_LIST_ID and CON_TYPE <> 0";
			beans = DBTools.getListArray(sql);*/

			LookupOperation lookupToLots = LookupOperation.newLookup().
					from("serverCatalogList").//关联表名 lots
					localField("serverCatalogListId").//关联字段
					foreignField("_id").//主表关联字段对应的次表字段
					as("entity");//查询结果集合名
			Criteria criteriaToLots = Criteria.where("serverSubscribesId").is(subid).and("conType").ne(0);
			AggregationOperation matchToLots = Aggregation.match(criteriaToLots);
			Aggregation counts = Aggregation.newAggregation(lookupToLots,matchToLots);
			List<Map> list = (List<Map>)MongodbUtils.aggregate(counts,"serverSubscribesList" , Map.class);
			list.forEach(m ->{
				String serverElementName =((Map)((List)m.get("entity")).get(0)).get("serverElementName").toString();
				String conType = m.get("conType").toString();
				String conValue = m.get("conValue").toString();
				String elementDescribe =((Map)((List)m.get("entity")).get(0)).get("elementDescribe").toString();
				String[] ss = {serverElementName,conType,conValue,elementDescribe};
				beans.add(ss);
			});
		}
		catch (Exception ex)
		{
			logger.error("获取订阅{}字段列表错误！",subid,ex);
		}
		return beans;
	}

}
