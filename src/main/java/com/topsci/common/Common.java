package com.topsci.common;

import java.util.Date;

public class Common {
	/**
	 * 服务发布xml验证schema
	 */
	public final static String SCHEMA_RECEIVE = "schema_receive";
	
	/**
	 * 服务主动订阅xml验证schema
	 */
	public final static String SCHEMA_REQUEST = "schema_request";

	/**
	 * redis服务端运行方式
	 */
	public final static String REDIS_SENTINEL = "sentinel";
	public final static String REDIS_SHARD = "shard";
	
	/**
	 * 缓存相关key名称
	 */
	public final static String CACHE_MAP_CATALOG = "cache_map_catalog";
	public final static String CACHE_MAP_SUBSCRIBE = "cache_map_subscribe";
	public final static String CACHE_MAP_TEMPLATE = "cache_map_template";
	public final static String CACHE_MAP_BUSINESS = "cache_map_business";
	public final static String CACHE_MAP_SUBSCRIBE_LIST = "cache_map_subscribe_list";
	public final static String CACHE_MAP_MSGSN = "cache_map_msgsn";

	/**
	 * 缓存主动订阅session时，key为  "sessionid_"+session的值
	 */
	public final static String CATCH_SESSIONID_KEY_START = "sessionid_"; 
	/**
	 * 主动请求订阅sessionid失效时间，单位秒
	 */
	public final static int SESSIONID_EXPIRE_SECONDS = 60*60; //一小时 


	/**
	 * 协议相关字段
	 */
	public final static String HEADER = "Header";
	public final static String BODY = "Body";
	public final static String Envelope = "Envelope";
	public final static String Parameter = "Parameter";
	public final static String QueryType = "QueryType";
	public final static String ColumnType = "Type";

	/**
	 * 主动请求查询参数定义
	 */
	public final static String CONDITION_EQUAL = "1";
	public final static String CONDITION_RANGE = "2";
	public final static String CONDITION_MULTI = "3";
	public final static String CONDITION_LIKE = "4";

	/**
	 * 数据库类型
	 */
	public final static String DBCODE_ORACLE = "1";
	public final static String DBCODE_SQLSERVER = "2";
	public final static String DBCODE_MYSQL = "3";

	/**
	 * 协议头相关字段
	 */
	public final static String HEAD_MSGTYPE = "MsgType";
	public final static String HEAD_MSGSENTTIME = "MsgSentTime";
	public final static String HEAD_MSGSN = "MsgSN";
	public final static String HEAD_FORMSYSID = "FormSysID";
	public final static String HEAD_TOSYSID = "ToSysID";
	public final static String HEAD_MSGOID = "MsgOID";
	public final static String HEAD_MSGOTIME = "MsgOTime";
	public final static String HEAD_MSGNUM = "MsgNum";
	public final static String HEAD_MSGCOUNT = "MsgCount";
	public final static String HEAD_SESSIONID = "SessionID";
	public final static String HEAD_MSGCODE = "MsgCode";

	
	
	/**
	 * 协议body相关
	 */
	public final static String BODY_ErrorCode = "ErrorCode";
	public final static String BODY_LIST = "List";
	public final static String BODY_PARAMETER = "Parameter";

	
	/**
	 * 发布数据的数据集合限制
	 */
	public final static int HEAD_MSGCOUNT_SIZE = 100; 
	
	/**
	 * 主动请求，频率限制， 分钟数内不能有第二次请求
	 */
	public final static int REQUEST_MIN_MINUTE = 60;
	
	
	/**
	 * 数据发布服务协议  发送给发布订阅模块时，toSysID常量为 ALL，该值固定不变，即所以消息类型都是ALL
	 */
	public final static String SERVER_NAME = "ALL";
	
	
	
	
	/**
	 * 表序列名称定义
	 * 
	 * SEQ_表名称
	 */
	public final static String SEQ_SERVER_MESSAGE_DETAIL = "SEQ_SERVER_MESSAGE_DETAIL_PK";
	
	
	
	/**
	 * 对订阅，发布的动态操作，对应SERVER_CHANGE_TASK表的DEAL_STATE
	 */
	public final static String TASK_CACHE_ADD = "A";     //新增
	public final static String TASK_CACHE_UPDATE = "U";  //修改
	public final static String TASK_CACHE_DELETE = "D";  //删除
	
	/**
	 * 处理状态
	 */
	public final static String MESSAGE_DELL_STATUS_SUCCESS = "Y";//处理成功
	public final static String MESSAGE_DELL_STATUS_ERROR = "E";  //处理失败
	public final static String MESSAGE_DELL_STATUS_NORMAL = "N"; //待处理
	public final static String MESSAGE_DELL_STATUS_IN = "R"; //处理中



	
	/**
	 * 服务变更记录表，变更类型
	 */
	public final static String CHANGE_CATALOG_TYPE = "C"; //发布
	public final static String CHANGE_SUBSCRIBES_TYPE = "S"; //订阅
	public final static String CHANGE_TEMPLATE_TYPE = "T"; //模板

	/**
	 * xml MsgCode
	 */
	public final static String XML_DATATYPE_FB = "0001"; //服务订阅的数据
	public final static String XML_DATATYPE_REQUEST = "0002"; //主动订阅请求


	
	public final static String XML_ERROR_CODE_HEAD_FORMAT = "1001"; //XML格式错误
	public final static String XML_ERROR_CODE_HEAD_MISS_NUMCOUNT = "1012"; //XML格式错误
	public final static String XML_ERROR_CODE_HEAD_NOTNUM_NUMCOUNT = "1013"; //XML格式错误
	public final static String XML_ERROR_CODE_HEAD_MISS_HEAD = "1014"; //XML格式错误
	public final static String XML_ERROR_CODE_DATA_NULL = "1002";   //数据为空
	public final static String XML_ERROR_CODE_NUM_MATCH = "1003";   //上传数据大小与头信息描述不付
	public final static String XML_ERROR_CODE_NUM_MAX = "1004";     //上传数据大于上限设置
	public final static String XML_ERROR_CODE_NO_SENDERTOPIC = "1005";  //业务系统不合法
	public final static String XML_ERROR_CODE_CATALOGILL = "1015";  //发布服务不合法
	public final static String XML_ERROR_CODE_SUBILL = "1016";  //订阅服务不合法
	public final static String XML_ERROR_CODE_DATA_FORMAT = "1006";  //body数据验证不通过
	public final static String XML_ERROR_CODE_DATA_MSGSN = "1007";  //msgSN（全局唯一）已经存在
	public final static String XML_ERROR_CODE_SESSIONID = "1008";   //主动请求，重复的 sessionid
	public final static String XML_ERROR_CODE_DOUBLE_REQUEST = "1009";   //一小时之内,只能请求相同服务一次
	public final static String XML_ERROR_CODE_SUBSCRIBE_CONDITION = "1009";   //一小时之内,只能请求相同服务一次
	public final static String XML_ERROR_CODE_TEMPLATE_INVALID = "1010";   //接口模板失效
	public final static String XML_ERROR_CODE_INDEX_MISS = "1011";   //主键标识错误
	public final static String XML_DATATYPE_THREEERROR = "2001"; //第三方错误消息

	public final static String XML_ERROR_CODE_BODY_DEFAULT = "0000";
	public final static String XML_ERROR_CODE_BODY_DATAERROR = "1010";   //body数据错误

	//集中平台请求日志的最后更新时间
	public static Date webRequestLastUpdate;
	//向集中平台发送日志的开关
	public static boolean sendWebLog= false;
	//订阅筛选条件：等于
	public static String SUBSCRIBE_CON_EQUAL="1";
	//订阅筛选条件：范围
	public static String SUBSCRIBE_CON_RANGE="2";
	//订阅筛选条件：多个
	public static String SUBSCRIBE_CON_MULTIPLE="3";
	//订阅筛选条件：模糊匹配
	public static String SUBSCRIBE_CON_LIKE="4";

	public static String SUBSCRIBE_CON_QUERYTYPE="QueryType";
	public static String SUBSCRIBE_CON_VALUE="Value";
	public static String SUBSCRIBE_CON_FROM="From";
	public static String SUBSCRIBE_CON_TO="To";

}
