package com.topsci.db.dao;

import java.time.LocalDateTime;
import java.util.List;

import com.topsci.bean.ServerHistoryApply;
import com.topsci.util.MongodbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.topsci.bean.ServerChangeTask;

/**
 * 
 * @author yanhan
 * 发布，订阅变更记录dao
 */
@SuppressWarnings("unchecked")
public class ChangeTaskDao {
	
	private static final Logger logger = LoggerFactory.getLogger(ChangeTaskDao.class);


	/**
	 * 查询尚未处理的变更记录
	 * 
	 * 说明： 缓存轮询，无需像信息表处理增加R： 处理中 状态， 即使定时任务叠加，缓存多次更新不影响任何结果
	 */
	public List<ServerChangeTask> getServerChangeTasks(){
		List<ServerChangeTask> list = null;
		try {
			String[] keys = {"status"};
			Object[] vals = {"N"};
			list = (List<ServerChangeTask>) MongodbUtils.find(ServerChangeTask.class,keys,vals);
		} catch (Exception e) {
			logger.error("查询发布，订阅服务变更记录出错！", e);
		}
		return list;
	}
	
	/**
	 * 根据ID修改状态
	 */
	public void updateStatusByID(int id){
		try {
			String[] key1 = {"status","lastdate"};
			Object[] val = {"Y", LocalDateTime.now()};
			MongodbUtils.updateFirst("id",id,key1,val,"serverChangeTask");
		} catch (Exception e) {
			logger.error("修改服务变更记录出错！", e);
		}
	}

	/**
	 * 处理尚未处理的历史请阅请求  状态为N的请求表示未处理
	 * @return
	 */
	public List<ServerHistoryApply> getServerHistoryApplyBean()
	{
		List<ServerHistoryApply> list = null;
		try
		{
			String[] keys = {"status"};
			Object[] vals = {"N"};
			list = (List<ServerHistoryApply>) MongodbUtils.find(ServerHistoryApply.class,keys,vals);
		}catch (Exception ex)
		{
			logger.error("查询历史订阅记录出错！",ex);
		}
		return list;
	}
}
