package com.topsci.task;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.topsci.bean.ServerMessageDetail;
import com.topsci.kafka.service.RequestDataSendService;

/**
 * 
 * @author yanhan
 * 把主动请求，发送到请求端 （轮询主动请求信息表subscribes_catalog_message）
 */
public class RequestTask extends BaseTask{
	private final Logger logger = LoggerFactory.getLogger(RequestTask.class);

	@Override
	public void run() {
		/**
		 * 从队列中获取要分发的数据
		 */
		ServerMessageDetail bean = null;
		RequestDataSendService requestDataSendService = new RequestDataSendService();
		
		while (true) {
			try {
				/**
				 * 查询需要分发的数据集
				 */
				bean = catchAbout.getRequestMessage();
				
				if(bean==null){
					Thread.sleep(2000);//如果得到队列为空，则暂停一段时间，防止空转
				}else{
					boolean flag = requestDataSendService.execute(bean);
//					if(!flag){
//						/**
//						 * 发送失败
//						 */
//						Thread.sleep(5000);
//					}
				}
			} catch (Exception e) {
				logger.error("路由主动请求异常！",e);
			}
		}
	}
	
}
