package com.topsci.task;

import com.topsci.kafka.service.PushDataSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.topsci.bean.ServerMessageDetail;

/**
 * @author yanhan
 * 消息分发主线程，定时执行
 */
public class PushTask extends BaseTask{

	private final Logger logger = LoggerFactory.getLogger(PushTask.class);

	
	@Override
	public void run() {

		/**
		 * 从队列中获取要分发的数据
		 */
		ServerMessageDetail bean = null;
		int emptycount = 1;
		PushDataSendService pushDataSendService = new PushDataSendService();
		
		while (true) {

			try {
				/**
				 * 查询需要分发的数据集
				 */
				bean = catchAbout.getPushMessage();
				if(bean==null){
					Thread.sleep(emptycount*50);//如果得到队列为空，则暂停一段时间，防止空转
					emptycount ++;
					if(emptycount > 10)emptycount = 10;
				}else{
					emptycount = 1;
					boolean flag = pushDataSendService.execute(bean);
//					if(!flag){
//						/**
//						 * 发送失败
//						 */
//						Thread.sleep(5000);
//					}
				}
				
				
			} catch (Exception e) {
				logger.error("推送分发异常！",e);
			}
			
			
		}
	}
	
	
	

}
