package com.topsci.db.dao;

import com.topsci.bean.ServerMessageDetail;
import com.topsci.util.MongodbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.topsci.bean.ServerMessageError;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageDao {
	
	private static final Logger logger = LoggerFactory.getLogger(MessageDao.class);

	/**
	 * 主动请求时，检查是否有相同的SessionID
	 */
	public boolean haveSessionID(String sessionid){
		boolean flag = false;
		try {
			String[] keys = {"sessionid"};
			Object[] vals = {sessionid};
			List<ServerMessageDetail> list = (List<ServerMessageDetail>) MongodbUtils.find(ServerMessageDetail.class,keys,vals);

			if(list != null && list.size() > 0){
				flag = true;
			}
		} catch (Exception e) {
			logger.error("查询sessionID是否存在出错！", e);
		}
		return flag;
	}

	/**
	 * 主动请求时， 对主动请求请求次数进行限制,time分钟之内不能再次请求
	 */
	public boolean checkRequest(String formSysID,String msgType){
		boolean flag = false;
		/*try {  IOT 不需要
			String sql  = "select 1 from server_message_detail where formsysid=? and msgtype=? and ceil((sysdate-to_date(LASTDATE,'yyyy-mm-dd,hh24:mi:ss'))* 24 * 60)<?";
			Object[] params = {formSysID,msgType,Common.REQUEST_MIN_MINUTE};
			String str = DBTools.getString(sql, params);
			if(str!=null&&"1".equals(str)){
				flag = true;
			}
		} catch (Exception e) {
			logger.error("查询主动请求是否频繁出错！", e);
		}*/
		
		return flag;
	}
	
	/**
	 * 保存错误的xml信息
	 */
	public void saveMessageError(ServerMessageError bean){
		/*try {
			Object[] params = {bean.getStatus(),bean.getLastdate(),bean.getMsgdata(),bean.getTopic_name(),bean.getMsgsn(),bean.getMsgsenttime(),bean.getFormsysid(),bean.getTosysid(),bean.getMsgoid(),bean.getMsgotime(),bean.getMsgtype(),bean.getMsgnum(),bean.getMsgcount(),bean.getSessionid(),bean.getMsgcode(),bean.getError_code()};
			DBTools.update("INSERT INTO SERVER_MESSAGE_ERROR (ID,STATUS,LASTDATE,MSGDATA,TOPIC_NAME,MSGSN,MSGSENTTIME,FORMSYSID,TOSYSID,MSGOID,MSGOTIME,MSGTYPE,MSGNUM,MSGCOUNT,SESSIONID,MSGCODE,ERROR_CODE) VALUES (SEQ_MESSAGE_ERROR_PK.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",params);
		} catch (Exception e) {
			logger.error("存储错误的xml消息失败！", e);
		}*/
	}

	//TODO
	public Map<String, Date> getValidMsgSN(){
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Map<String,Date> map = new HashMap<>();
		/*try{
			String sql = "select MSGSN,LASTDATE from server_message_detail where (sysdate - to_date(LASTDATE,'yyyy-mm-dd hh24:mi:ss'))*24<4";
			List<String[]> list = DBTools.getListArray(sql);
			if(list != null && !list.isEmpty())
			{
				list.forEach(l -> {
					try {
						map.put(l[0], sdf.parse(l[1]));
					}
					catch (Exception e){
						logger.debug("获取MSGSN：{}错误！",l[0]);
					}
				});
			}
		}
		catch (Exception e) {
			logger.error("验证msgSN是否已经存在失败！", e);
		}*/
		return map;
	}
	
	/**
	 * 验证msgSN是否存在
	 */
	public boolean checkMsgSN(String msgSN) {
		boolean flag = false;
		/*try {
			String sql = "select 1 from server_message_detail where msgsn=?";
			Object[] params = {msgSN};
			String rs = DBTools.getString(sql, params);
			if (!StringUtil.isNull(rs) && "1".equals(rs)) {
				flag = true;
			}
		} catch (Exception e) {
			logger.error("验证msgSN是否已经存在失败！", e);
		}*/
		return flag;
	}
	
}
