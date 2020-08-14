package com.topsci.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataStatisticTask extends BaseTask{

    private static Logger logger = LoggerFactory.getLogger(DataStatisticTask.class);

    private static String TYPE_TPL = "TPL";
    private static String TYPE_DB = "DB";
    private static String TYPE_PCA = "PCA";
    private static String TYPE_BS = "BS";
    private static String TYPE_SCA = "SCA";
    private static int SAVE_DAY = 31;

    @Override
    public void run(){
        /*Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH,-1);
        int month = c.get(Calendar.MONTH) + 1;
        String curmonth;
        if (month < 10) {
            curmonth = "0" + month;
        } else {
            curmonth = month + "";
        }
        int day = c.get(Calendar.DAY_OF_MONTH);
        String curday;
        if (day < 10) {
            curday = "0" + day;
        } else {
            curday = day + "";
        }
        String starttime = c.get(Calendar.YEAR)+"-"+curmonth+"-"+curday+" 00:00:00";
        String endtime = c.get(Calendar.YEAR)+"-"+curmonth+"-"+curday+" 23:59:59";
        String curdate = c.get(Calendar.YEAR)+"-"+curmonth+"-"+curday;

        String catalog_tpl_tab = "insert into SERVER_DATA_STATISTIC_TAB(BUUID,CT,SM,SDATE,STYPE,UUID) " +
                "select sct.TYPEUUID,nvl(sum(scdp.LAST_COUNT),0),nvl(sum(scdp.ALL_COUNT),0),'"+curdate+"','"+TYPE_TPL+"',sys_guid() " +
                "from SERVER_CATALOG_DB_PUB scdp " +
                "left join SERVER_CATALOG sc on sc.DB_ID = scdp.UUID " +
                "left join SERVER_CATALOG_TEMPLATE sct on sc.TEMPLATE_ID = sct.ID " +
                "group by sct.TYPEUUID " +
                "order by sct.TYPEUUID";

        String catalog_db_tab = "insert into SERVER_DATA_STATISTIC_TAB(BUUID,CT,SM,SDATE,STYPE,UUID) " +
                "select scd.UUID,nvl(sum(scdp.LAST_COUNT),0),nvl(sum(scdp.ALL_COUNT),0),'"+curdate+"','"+TYPE_DB+"',sys_guid() " +
                "from SERVER_CATALOG_DB_PUB scdp " +
                "left join SERVER_CATALOG_DBINFO scd on scdp.DB_ID = scd.UUID " +
                "group by scd.UUID " +
                "order by scd.UUID";

        String catalog_ca_tab = "insert into SERVER_DATA_STATISTIC_TAB(BUUID,CT,SM,SDATE,STYPE,UUID) " +
                "select sc.ID,nvl(scdp.LAST_COUNT,0),nvl(scdp.ALL_COUNT,0),'"+curdate+"','"+TYPE_PCA+"',sys_guid() " +
                "from SERVER_CATALOG_DB_PUB scdp " +
                "left join SERVER_CATALOG sc on sc.DB_ID = scdp.UUID " +
                "order by sc.ID";

        String catalog_tpl = "insert into SERVER_DATA_STATISTIC_MESG(BUUID,CT,SM,SDATE,STYPE,UUID) " +
                "select t1.typeuuid,t1.ct,t1.sm,'"+curdate+"','"+TYPE_TPL+"',sys_guid() " +
                "from (SELECT sct.TYPEUUID typeuuid, COUNT(*) ct, SUM(MSGCOUNT) sm " +
                "      FROM SERVER_CATALOG_MESSAGE scm " +
                "             LEFT JOIN SERVER_MESSAGE_DETAIL smd ON scm.SERVER_MESSAGE_DETAIL_ID = smd.ID " +
                "             LEFT JOIN SERVER_CATALOG sc on sc.id = scm.SERVER_CATALOG_ID " +
                "             LEFT JOIN SERVER_CATALOG_TEMPLATE sct on sct.ID = sc.TEMPLATE_ID " +
                "      WHERE SCM.STATUS = 'Y' " +
                "        and TO_DATE(smd.MSGSENTTIME, 'yyyymmddhh24miss') BETWEEN TO_DATE('"+starttime+"', 'yyyy-mm-dd hh24:mi:ss') and TO_DATE('"+endtime+"', 'yyyy-mm-dd hh24:mi:ss') " +
                "      GROUP BY sct.TYPEUUID " +
                "      ORDER BY sct.TYPEUUID) t1 " +
                "union ALL " +
                "select distinct sctt.UUID, 0 as message, 0 as datacount,'"+curdate+"','"+TYPE_TPL+"',sys_guid() " +
                "from SERVER_CATALOG_TEMPLATE_TYPE sctt " +
                "where not exists (SELECT 1 " +
                "                        FROM SERVER_CATALOG_MESSAGE scm " +
                "                               LEFT JOIN SERVER_MESSAGE_DETAIL smd ON scm.SERVER_MESSAGE_DETAIL_ID = smd.ID " +
                "                               LEFT JOIN SERVER_CATALOG sc on sc.id = scm.SERVER_CATALOG_ID " +
                "                               LEFT JOIN SERVER_CATALOG_TEMPLATE sct on sct.ID = sc.TEMPLATE_ID " +
                "                        WHERE scm.STATUS = 'Y' and sctt.uuid = sct.TYPEUUID " +
                "                          AND TO_DATE(smd.MSGSENTTIME, 'yyyymmddhh24miss') BETWEEN TO_DATE('"+starttime+"', 'yyyy-mm-dd hh24:mi:ss') AND TO_DATE('"+endtime+"', 'yyyy-mm-dd hh24:mi:ss'))";

        String catalog_db = "insert into SERVER_DATA_STATISTIC_MESG(BUUID,CT,SM,SDATE,STYPE,UUID) " +
                "select t1.dbid,t1.ct,t1.sm,'"+curdate+"','"+TYPE_DB+"',sys_guid() " +
                "from (SELECT scdp.DB_ID dbid, COUNT(*) ct, SUM(MSGCOUNT) sm " +
                "      FROM SERVER_CATALOG_MESSAGE scm " +
                "             LEFT JOIN SERVER_MESSAGE_DETAIL smd ON scm.SERVER_MESSAGE_DETAIL_ID = smd.ID " +
                "             LEFT JOIN SERVER_CATALOG sc on sc.ID = scm.SERVER_CATALOG_ID " +
                "             LEFT JOIN SERVER_CATALOG_TEMPLATE sct on sct.ID = sc.TEMPLATE_ID " +
                "             LEFT JOIN SERVER_CATALOG_DB_PUB scdp on scdp.UUID = sc.DB_ID " +
                "      WHERE SCM.STATUS = 'Y' " +
                "        and TO_DATE(smd.MSGSENTTIME, 'yyyymmddhh24miss') BETWEEN TO_DATE('"+starttime+"', 'yyyy-mm-dd hh24:mi:ss') and TO_DATE('"+endtime+"', 'yyyy-mm-dd hh24:mi:ss') " +
                "      GROUP BY scdp.DB_ID " +
                "      ORDER BY scdp.DB_ID) t1 " +
                "union ALL " +
                "select distinct scd.UUID, 0 as message, 0 as datacount,'"+curdate+"','"+TYPE_DB+"',sys_guid() " +
                "from SERVER_CATALOG_DBINFO scd " +
                "where not exists (SELECT 1 " +
                "                       FROM SERVER_CATALOG_MESSAGE scm " +
                "                              LEFT JOIN SERVER_MESSAGE_DETAIL smd ON scm.SERVER_MESSAGE_DETAIL_ID = smd.ID " +
                "                              LEFT JOIN SERVER_CATALOG sc on sc.ID = scm.SERVER_CATALOG_ID " +
                "                              LEFT JOIN SERVER_CATALOG_TEMPLATE sct on sct.ID = sc.TEMPLATE_ID " +
                "                              LEFT JOIN SERVER_CATALOG_DB_PUB scdp on scdp.UUID = sc.DB_ID " +
                "                       WHERE scm.STATUS = 'Y' and scd.UUID = scdp.DB_ID " +
                "                         AND TO_DATE(smd.MSGSENTTIME, 'yyyymmddhh24miss') BETWEEN TO_DATE('"+starttime+"', 'yyyy-mm-dd hh24:mi:ss') AND TO_DATE('"+endtime+"', 'yyyy-mm-dd hh24:mi:ss'))";

        String catalog_ca = "insert into SERVER_DATA_STATISTIC_MESG(BUUID,CT,SM,SDATE,STYPE,UUID) " +
                "select t1.cid,t1.ct,t1.sm,'"+curdate+"','"+TYPE_PCA+"',sys_guid() " +
                "from (SELECT scm.BUSINESS_SYSTEM_ID bsid, SERVER_CATALOG_ID cid, COUNT(*) ct, SUM(MSGCOUNT) sm " +
                "      FROM SERVER_CATALOG_MESSAGE scm " +
                "             LEFT JOIN SERVER_MESSAGE_DETAIL smd ON scm.SERVER_MESSAGE_DETAIL_ID = smd.ID " +
                "             LEFT JOIN SERVER_CATALOG sc on sc.id = scm.SERVER_CATALOG_ID " +
                "             LEFT JOIN SERVER_CATALOG_DB_PUB scdp on sc.DB_ID = scdp.UUID " +
                "      WHERE SCM.STATUS = 'Y' " +
                "        and TO_DATE(smd.MSGSENTTIME, 'yyyymmddhh24miss') BETWEEN TO_DATE('"+starttime+"', 'yyyy-mm-dd hh24:mi:ss') and TO_DATE('"+endtime+"', 'yyyy-mm-dd hh24:mi:ss') " +
                "      GROUP BY scm.BUSINESS_SYSTEM_ID, SERVER_CATALOG_ID " +
                "      ORDER BY SERVER_CATALOG_ID) t1 " +
                "union ALL " +
                "select ID as SERVER_CATALOG_ID,  0 as message, 0 as datacount,'"+curdate+"','"+TYPE_PCA+"',sys_guid() " +
                "from SERVER_CATALOG sc " +
                "       LEFT JOIN SERVER_CATALOG_DB_PUB scdp on sc.DB_ID = scdp.UUID " +
                "where sc.DELETED = 'N' " +
                "  and not exists (SELECT distinct scm.SERVER_CATALOG_ID " +
                "                    FROM SERVER_CATALOG_MESSAGE scm " +
                "                           LEFT JOIN SERVER_MESSAGE_DETAIL smd ON scm.SERVER_MESSAGE_DETAIL_ID = smd.ID " +
                "                    WHERE scm.STATUS = 'Y' and scm.SERVER_CATALOG_ID = sc.id " +
                "                      AND TO_DATE(smd.MSGSENTTIME, 'yyyymmddhh24miss') BETWEEN TO_DATE('"+starttime+"', 'yyyy-mm-dd hh24:mi:ss') AND TO_DATE('"+endtime+"', 'yyyy-mm-dd hh24:mi:ss'))";

        String subscribe_bs = "insert into SERVER_DATA_STATISTIC_MESG(BUUID,CT,SM,SDATE,STYPE,UUID) " +
                "select t.bsi,t.ct,t.sm,'"+curdate+"','"+TYPE_BS+"',sys_guid() " +
                "from ( " +
                "SELECT BUSINESS_SYSTEM_ID as bsi, COUNT(*) ct, SUM(MSGCOUNT) sm " +
                "FROM SERVER_SUBSCRIBES_MESSAGE scm " +
                "       LEFT JOIN SERVER_MESSAGE_DETAIL smd ON scm.SERVER_MESSAGE_DETAIL_ID = smd.ID " +
                "WHERE SCM.STATUS = 'Y' " +
                "  and TO_DATE(smd.MSGSENTTIME, 'yyyymmddhh24miss') BETWEEN TO_DATE(' "+starttime+"', 'yyyy-mm-dd hh24:mi:ss') and TO_DATE(' "+endtime+"', 'yyyy-mm-dd hh24:mi:ss') " +
                "GROUP BY BUSINESS_SYSTEM_ID " +
                "order by BUSINESS_SYSTEM_ID) t " +
                "union all " +
                "select ID,0,0,'"+curdate+"','"+TYPE_BS+"',sys_guid() from BUSINESS_SYSTEM bs " +
                "where not exists( " +
                "    SELECT BUSINESS_SYSTEM_ID " +
                "FROM SERVER_SUBSCRIBES_MESSAGE scm " +
                "       LEFT JOIN SERVER_MESSAGE_DETAIL smd ON scm.SERVER_MESSAGE_DETAIL_ID = smd.ID " +
                "WHERE SCM.STATUS = 'Y' and BUSINESS_SYSTEM_ID = bs.id " +
                "  and TO_DATE(smd.MSGSENTTIME, 'yyyymmddhh24miss') BETWEEN TO_DATE(' "+starttime+"', 'yyyy-mm-dd hh24:mi:ss') and TO_DATE(' "+endtime+"', 'yyyy-mm-dd hh24:mi:ss') " +
                "GROUP BY BUSINESS_SYSTEM_ID " +
                "    )";

        String subscribe_ca = "insert into SERVER_DATA_STATISTIC_MESG(BUUID,CT,SM,SDATE,STYPE,UUID) " +
                "select t.sid,t.ct,t.sm,'"+curdate+"','"+TYPE_SCA+"',sys_guid() from ( " +
                "SELECT BUSINESS_SYSTEM_ID bid, SERVER_SUBSCRIBES_ID sid, COUNT(*) ct, SUM(MSGCOUNT) sm " +
                "FROM SERVER_SUBSCRIBES_MESSAGE scm " +
                "       LEFT JOIN SERVER_MESSAGE_DETAIL smd ON scm.SERVER_MESSAGE_DETAIL_ID = smd.ID " +
                "WHERE SCM.STATUS = 'Y' " +
                "  and TO_DATE(smd.MSGSENTTIME, 'yyyymmddhh24miss') BETWEEN TO_DATE('"+starttime+"', 'yyyy-mm-dd hh24:mi:ss') and TO_DATE('"+endtime+"', 'yyyy-mm-dd hh24:mi:ss') " +
                "GROUP BY BUSINESS_SYSTEM_ID, SERVER_SUBSCRIBES_ID " +
                "order by SERVER_SUBSCRIBES_ID) t " +
                "union all " +
                "select ss.ID,0,0,'"+curdate+"','"+TYPE_SCA+"',sys_guid() from SERVER_SUBSCRIBES ss " +
                "where not exists ( " +
                "    SELECT SERVER_SUBSCRIBES_ID " +
                "FROM SERVER_SUBSCRIBES_MESSAGE scm " +
                "       LEFT JOIN SERVER_MESSAGE_DETAIL smd ON scm.SERVER_MESSAGE_DETAIL_ID = smd.ID " +
                "WHERE SCM.STATUS = 'Y' and scm.SERVER_SUBSCRIBES_ID = ss.id " +
                "  and TO_DATE(smd.MSGSENTTIME, 'yyyymmddhh24miss') BETWEEN TO_DATE('"+starttime+"', 'yyyy-mm-dd hh24:mi:ss') and TO_DATE('"+endtime+"', 'yyyy-mm-dd hh24:mi:ss') " +
                "GROUP BY BUSINESS_SYSTEM_ID, SERVER_SUBSCRIBES_ID " +
                "    )";

        String delete = "delete from SERVER_DATA_STATISTIC_MESG where to_date(SDATE,'YYYY-MM-DD') < (sysdate-"+SAVE_DAY+")";

        logger.info("统计接口消息任务开始");
        try {
            DBTools.update(catalog_tpl_tab);
        } catch (Exception ex) {
            logger.error("数据库发布统计_按模板类型统计错误！", ex);
        }
        try {
            DBTools.update(catalog_db_tab);
        } catch (Exception ex) {
            logger.error("数据库发布统计_按数据源统计错误！", ex);
        }
        try {
            DBTools.update(catalog_ca_tab);
        } catch (Exception ex) {
            logger.error("数据库发布统计_按接口统计错误！", ex);
        }
        try {
            DBTools.update(catalog_tpl);
        } catch (Exception ex) {
            logger.error("接口发布消息统计_按模板类型统计错误！", ex);
        }
        try {
            DBTools.update(catalog_db);
        } catch (Exception ex) {
            logger.error("接口发布消息统计_按数据源统计错误！", ex);
        }
        try {
            DBTools.update(catalog_ca);
        } catch (Exception ex) {
            logger.error("接口发布消息统计_按接口统计错误！", ex);
        }
        try {
            DBTools.update(subscribe_bs);
        } catch (Exception ex) {
            logger.error("接口订阅消息统计_按系统统计错误！", ex);
        }
        try {
            DBTools.update(subscribe_ca);
        } catch (Exception ex) {
            logger.error("接口订阅消息统计_按订阅统计错误！", ex);
        }
        try {
            DBTools.update(delete);
        } catch (Exception ex) {
            logger.error("接口数据统计_删除历史数据错误！", ex);
        }
        logger.info("统计接口消息任务结束");*/
    }
}
