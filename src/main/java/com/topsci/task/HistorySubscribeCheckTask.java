package com.topsci.task;

import com.topsci.bean.ServerHistoryApply;
import com.topsci.bean.ServerMessageDetail;
import com.topsci.common.CatchAbout;
import com.topsci.common.Common;
import com.topsci.util.DateUtil;
import com.topsci.util.ZipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by lzw on 2016/12/26.
 */
public class HistorySubscribeCheckTask extends BaseTask {
    private final Logger logger = LoggerFactory.getLogger(HistorySubscribeCheckTask.class);
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private CatchAbout catchAbout = CatchAbout.getInstance();

    @Override
    public void run() {
        try {
            List<ServerHistoryApply> list = ywDBService.getServerHistoryApplyTasks();
            for (ServerHistoryApply applyBean : list) {
                Map<String, String> params = ywDBService.getHistoryFromToParam(applyBean.getId());
                ServerMessageDetail serverMessageDetailBean = new ServerMessageDetail();
                serverMessageDetailBean.setFormsysid(params.get("fromsysid"));
                serverMessageDetailBean.setMsgcount(1);
                serverMessageDetailBean.setMsgoid(Common.SERVER_NAME);
                serverMessageDetailBean.setMsgotime(dateFormat.format(new Date()));
                serverMessageDetailBean.setMsgsenttime(dateFormat.format(new Date()));
                serverMessageDetailBean.setMsgsn(UUID.randomUUID().toString().replace("-", ""));
                serverMessageDetailBean.setMsgtype(params.get("msgtype"));
                serverMessageDetailBean.setMsgnum(0);
                serverMessageDetailBean.setLastdate(DateUtil.getNow());
                serverMessageDetailBean.setSessionid(UUID.randomUUID().toString().replace("-", ""));
                serverMessageDetailBean.setMsgcode(Common.XML_DATATYPE_REQUEST);
                serverMessageDetailBean.setTosysid(params.get("tosysid"));
                StringBuffer sb = new StringBuffer();
                sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><Envelope><Header>");
                sb.append("<MsgSentTime>");
                sb.append(serverMessageDetailBean.getMsgsenttime());
                sb.append("</MsgSentTime><MsgSN>");
                sb.append(serverMessageDetailBean.getMsgsn());
                sb.append("</MsgSN><FormSysID>");
                sb.append(serverMessageDetailBean.getFormsysid());
                sb.append("</FormSysID><ToSysID>");
                sb.append(serverMessageDetailBean.getTosysid());
                sb.append("</ToSysID><MsgOID>");
                sb.append(serverMessageDetailBean.getMsgoid());
                sb.append("</MsgOID><MsgOTime>");
                sb.append(serverMessageDetailBean.getMsgotime());
                sb.append("</MsgOTime>");
                sb.append("<MsgNum>0</MsgNum><MsgNum>0</MsgNum>");
                sb.append("<MsgType>");
                sb.append(serverMessageDetailBean.getMsgtype());
                sb.append("</MsgType>");
                sb.append("<MsgCount>1</MsgCount><MsgCode>");
                sb.append(serverMessageDetailBean.getMsgcode());
                sb.append("</MsgCode><SessionID>");
                sb.append(serverMessageDetailBean.getSessionid());
                sb.append("</SessionID></Header><Body>");
                sb.append(applyBean.getParameter());
                sb.append("</Body></Envelope>");

                serverMessageDetailBean.setMsgdata(ZipUtil.zip(sb.toString()));
                catchAbout.saveMessageToCatch(serverMessageDetailBean);
                ywDBService.updateServerHistoryApply(applyBean.getId());
            }
            if(list!=null && list.size()>0) {
                logger.info("处理历史订阅请求任务结束，共处理了{}个请求",list.size());
            }
        }
        catch (Exception ex)
        {
            logger.error("更新历史订阅请求任务错误！",ex);
        }

    }
}
