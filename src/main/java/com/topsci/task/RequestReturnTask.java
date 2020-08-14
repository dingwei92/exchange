package com.topsci.task;

import com.topsci.bean.ServerMessageDetail;
import com.topsci.kafka.service.PushDataSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yanhan
 * 主动请求回复消息分发主线程，定时执行
 */
public class RequestReturnTask extends BaseTask{

	private final Logger logger = LoggerFactory.getLogger(RequestReturnTask.class);

	
	@Override
	public void run() {
		/**
		 * 从队列中获取要分发的数据
		 */
		ServerMessageDetail bean = null;
		PushDataSendService pushDataSendService = new PushDataSendService();
		
		while (true) {
			try {
				/**
				 * 查询需要分发的数据集
				 */
				bean = catchAbout.getRequestReturnMessage();
				if(bean==null){
					Thread.sleep(5000);//如果得到队列为空，则暂停一段时间，防止空转
				}else{
					boolean flag = pushDataSendService.execute(bean);
				}
				
				
			} catch (Exception e) {
				logger.error("推送主动请求回复异常！",e);
			}
			
			
		}
	}
	
	
	

}
