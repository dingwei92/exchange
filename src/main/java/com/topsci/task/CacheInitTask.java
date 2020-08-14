package com.topsci.task;

import java.util.List;

import com.google.gson.Gson;
import com.topsci.bean.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.topsci.common.CatchAbout;
import com.topsci.common.Common;
import com.topsci.xml.XmlFactory;

/**
 * 
 * @author yanhan
 * 定时更新订阅，发布缓存
 * 
 * 注意：因为订阅与发布有关联关系， 如果删除，修改发布请先取消相关订阅，再进行操作。
 */
public class CacheInitTask extends BaseTask{
	private final Logger logger = LoggerFactory.getLogger(CacheInitTask.class);

	private CatchAbout catchAbout = CatchAbout.getInstance();
	private Gson gson;

	public CacheInitTask()
	{
		gson = new Gson();
	}

	@Override
	public void run() {
		
		try {
			List<ServerChangeTask> changes = ywDBService.getServerChangeTasks();
			if(changes!=null&&changes.size()>0){

				for(ServerChangeTask change:changes){
					
					//更新备注
					String bz = "";
					
					/**
					 * 发布服务
					 */
					if(Common.CHANGE_CATALOG_TYPE.equals(change.getChange_type())){
						ServerCatalog cbbean = catchAbout.getServerCatalogBean(change.getId()+"");
						//删除
						if(Common.TASK_CACHE_DELETE.equals(change.getDeal_state())){
							/**
							 * 注意，map的key为String，必须转换成字符串，否则int不能删除原来的值
							 */
							catchAbout.delCatch(Common.CACHE_MAP_CATALOG,String.valueOf(change.getServer_change_id()));
							catchAbout.delCatch(Common.CACHE_MAP_CATALOG,cbbean.getServerShort());

							bz = "删除缓存成功，id="+change.getServer_change_id();
						}else{//修改或新增
							ServerCatalog bean = ywDBService.getServerCatalogByID(change.getServer_change_id());
							if(bean!=null){
								bean = ywDBService.creatCacheServerCatalogBean(bean);
								if(bean!=null){
									//更新缓存
									catchAbout.updateCatch(Common.CACHE_MAP_CATALOG,String.valueOf(bean.getId()),gson.toJson(bean));
									catchAbout.updateCatch(Common.CACHE_MAP_CATALOG,bean.getServerShort(),gson.toJson(bean));
									bz = "更新成功";
								}else{
									bz = "新增或修改失败，查询后构建发布对象失败，id="+change.getServer_change_id();
								}
							}else{
								bz = "新增或修改失败，查询为空，id="+change.getServer_change_id();
							}
							if(bean==null){
								catchAbout.delCatch(Common.CACHE_MAP_CATALOG,String.valueOf(change.getServer_change_id()));
								catchAbout.delCatch(Common.CACHE_MAP_CATALOG,String.valueOf(cbbean.getServerShort()));
							}
						}
					}

					/**
					 * 模板变更
					 */
					if(Common.CHANGE_TEMPLATE_TYPE.equals(change.getChange_type())){
						//更新
						if(Common.TASK_CACHE_UPDATE.equals(change.getDeal_state())){
							//更新模板
							ServerCatalogTemplate tbean = ywDBService.getCatalogTemplate(change.getServer_change_id()+"");
							catchAbout.updateCatch(Common.CACHE_MAP_TEMPLATE,tbean.getId()+"",tbean);
							//更新模板对应的接口
							List<ServerCatalog> beans = ywDBService.getServerCatalogByTemplateID(change.getServer_change_id());
							if(beans!=null){
								for(ServerCatalog bean : beans) {
									bean = ywDBService.creatCacheServerCatalogBean(bean);
									if (bean != null) {
										//更新缓存
										catchAbout.updateCatch(Common.CACHE_MAP_TEMPLATE, String.valueOf(bean.getId()), bean);
										bz = "更新成功";
									} else {
										bz = "新增或修改失败，查询后构建发布对象失败，id=" + change.getServer_change_id();
									}
								}
							}else{
								bz = "新增或修改失败，查询为空，id="+change.getServer_change_id();
							}
						}
						//删除
						else if(Common.TASK_CACHE_DELETE.equals(change.getDeal_state()))
						{
							catchAbout.delCatch(Common.CACHE_MAP_TEMPLATE,change.getServer_change_id()+"");
						}
					}
				
					/**
					 * 订阅服务
					 */
					if(Common.CHANGE_SUBSCRIBES_TYPE.equals(change.getChange_type())){
						//删除
						if(Common.TASK_CACHE_DELETE.equals(change.getDeal_state())){
							ServerSubscribes bean = catchAbout.getServerSubscribesBean(change.getServer_change_id()+"");
							if(bean != null) {
								catchAbout.delCatch(Common.CACHE_MAP_SUBSCRIBE, String.valueOf(change.getServer_change_id()));
								catchAbout.delCatch(Common.CACHE_MAP_SUBSCRIBE, bean.getBusinessSystemBean().getSystemShort() + "_" + bean.getServerCatalogBean().getServerShort());
								catchAbout.serverSubscribeListRemove(bean.getServerCatalogBean().getServerShort(), bean.getId() + "");
								bz = "删除缓存成功，id=" + change.getServer_change_id();
							}
							else{
								bz = "订阅缓存"+change.getServer_change_id()+"不存在";
							}
						}else{//修改和添加
							ServerSubscribes bean = ywDBService.getServerSubscribesBeanByID(change.getServer_change_id());
							if(bean!=null){
								bean = ywDBService.creatCacheServerSubscribesBean(bean);
								if(bean!=null){
									catchAbout.updateCatch(Common.CACHE_MAP_SUBSCRIBE,String.valueOf(bean.getId()),bean);
									catchAbout.updateCatch(Common.CACHE_MAP_SUBSCRIBE,String.valueOf(
											bean.getBusinessSystemBean().getSystemShort()+"_"+bean.getServerCatalogBean().getServerShort()),bean);
									catchAbout.serverSubscribeListAdd(bean.getServerCatalogBean().getServerShort(),bean.getId()+"");
									bz = "更新成功";
									if(Common.TASK_CACHE_ADD.equals(change.getDeal_state())){
										/**
										 * 判断订阅是否需要初始化全量获取,注意：只在初始时候判断是否全量获取，在修改订阅时，就不主动请求数据了， 所以不在creatCacheServerSubscribesBean方法中判断
										 */
										if("Y".equals(bean.getIsFull())){
											ServerMessageDetail serverMessageDetailBean = XmlFactory.getInstance().createXmlRequest(bean);
											if(serverMessageDetailBean!=null){
												/**
												 * 存储 待主动请求订阅缓存队列
												 */
												catchAbout.saveMessageToCatch(serverMessageDetailBean);
											}
										}
									}
								}else{
									bz = "新增或修改失败，查询后构建发布对象失败，id="+change.getServer_change_id();
								}
							}else{
								bz = "新增或修改失败，查询为空，id="+change.getServer_change_id();
							}
							
							if(bean==null){
								catchAbout.delCatch(Common.CACHE_MAP_SUBSCRIBE,String.valueOf(change.getServer_change_id()));
							}
						}
					}
					ywDBService.updatechangeTaskStatusByID(change.getId()); //更新changelog状态
					/**
					 * 对更新日志进行输出
					 */
					StringBuffer sb = new StringBuffer("更新缓存   === ");
					sb.append("ChangeType: ")
					.append(change.getChange_type())
					.append("  ServerChangeId: ")
					.append(change.getServer_change_id())
					.append("  DealState: ")
					.append(change.getDeal_state())
					.append("  更新结果：")
					.append(bz);
					
					logger.info(sb.toString());
				}
			}
			
		} catch (Exception e) {
			logger.error("定时更新缓存异常！", e);
		}
		
		
	}
	

	
}
