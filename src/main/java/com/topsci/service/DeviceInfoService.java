package com.topsci.service;

import com.topsci.bean.*;
import com.topsci.db.dao.CatalogDao;
import com.topsci.edgexfoundry.DeviceCache;
import com.topsci.util.DateUtil;
import com.topsci.util.MongodbUtils;
import com.topsci.util.UUIDGenerator;
import org.edgexfoundry.controller.DeviceClient;
import org.edgexfoundry.controller.DeviceProfileClient;
import org.edgexfoundry.domain.meta.Device;
import org.edgexfoundry.domain.meta.DeviceObject;
import org.edgexfoundry.domain.meta.DeviceProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by zlli on 2020/5/28.
 * Desc:
 * _        _     _
 * | |      | |   | |
 * ___| |_ __ _| |__ | | ___
 * / __| __/ _` | '_ \| |/ _ \
 * \__ \ || (_| | |_) | |  __/
 * |___/\__\__,_|_.__/|_|\___|
 */
@Service
public class DeviceInfoService {
    @Autowired
    private DeviceClient deviceClient;
    @Autowired
    private DeviceProfileClient deviceProfileClient;

    /**
     *缓存设备与对应设备profile关系
     */
    public void cacheDeviceInfo(){
        DeviceCache.getSingleton().cache().clear();
        List<Device> devices = deviceClient.devices();
        devices.stream().forEach(d->{
            String dName = d.getName();
            String profileName = d.getProfile().getName();
            DeviceCache.getSingleton().cache().put(dName,profileName);
        });
    }

    /**
     * profile与 exchage对于关系，没有的创建 ，iot删除的exchage禁用
     */
    public void profileInfo(){
        CatalogDao catalogDao = new CatalogDao();
        List<ServerCatalog> listServerCatalogBean = catalogDao.getServerCatalogs();
        if(listServerCatalogBean == null){
            return;
        }
        //查找没有创建DeviceProfile
        List<DeviceProfile> listProFiles = deviceProfileClient.deviceProfiles();
        List<String> listStrProFiles =listProFiles.stream().map(DeviceProfile::getName).collect(Collectors.toList());
        List<String> listStrServerCatalogBean =listServerCatalogBean.stream().map(ServerCatalog::getServerName).collect(Collectors.toList());
        listStrProFiles.removeAll(listStrServerCatalogBean);
        Map<String,DeviceProfile> mapDeviceProfile = listProFiles.stream().collect(Collectors.toMap(DeviceProfile::getName, entry -> entry));
        //生成exchage需要的数据
        String typeUuid = "db7b6aa07fc2686e8787127306d791f6";

        String[] key = {"uuid"};
        Object[] val = {typeUuid};
        int count =  MongodbUtils.count(ServerCatalogTemplateType.class,key,val);
            //类别
        if(count <= 0){
            ServerCatalogTemplateType serverCatalogTemplateType = new ServerCatalogTemplateType();
            serverCatalogTemplateType.setCreatetime(LocalDateTime.now());
            serverCatalogTemplateType.setDbuuid(UUIDGenerator.getNoCrossUUID());
            serverCatalogTemplateType.setTypename("IOT平台数据");
            serverCatalogTemplateType.setUuid(typeUuid);
            MongodbUtils.save(serverCatalogTemplateType);
        }
        Criteria criteria = new Criteria();
        criteria.and("id").is(1).and("deleted").is("N");
        int countBusiness =  MongodbUtils.count(BusinessSystem.class,criteria);
        if(countBusiness <= 0) {
            BusinessSystem businessSystem = new BusinessSystem();
        }


        listStrProFiles.forEach(s->{
            int idSeq =  MongodbUtils.count(ServerCatalogTemplate.class,new Criteria())+1;
            DeviceProfile df = mapDeviceProfile.get(s);
            //模板 SERVER_CATALOG_TEMPLATE
            ServerCatalogTemplate serverCatalogTemplate = new ServerCatalogTemplate();
            serverCatalogTemplate.setId(idSeq);
            serverCatalogTemplate.setName(df.getName());
            serverCatalogTemplate.setStatus("Y");
            serverCatalogTemplate.setCreateUser("0");
            serverCatalogTemplate.setCreateTime(LocalDateTime.now());
            serverCatalogTemplate.setInvalidTime(LocalDateTime.now());
            serverCatalogTemplate.setTypeuuid(typeUuid);
            serverCatalogTemplate.setTplcode(df.getName());
            MongodbUtils.save(serverCatalogTemplate);

            //模板字段 SERVER_CATALOG_LIST
            // 查找模板id
            int templateId = idSeq;

            for(DeviceObject dr : df.getDeviceResources()){
                /*StringBuilder sbCatalogEle = new StringBuilder();
                sbCatalogEle.append("Insert into SERVER_CATALOG_LIST (ID,SERVER_CATALOG_ID,SERVER_ELEMENT_NAME,SERVER_ELEMENT_DESCRIBE,LASTDATE,KEY_COL,DB_KEY) values ")
                        .append("(SEQ_SERVER_CATALOG_LIST_PK.nextval,")
                        .append(templateId).append(",")
                        .append("'").append(dr.getName()).append("',")
                        .append("'").append(dr.getDescription()).append("',")
                        .append("to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')").append(",")
                        .append("1").append(",")
                        .append("0").append(")");
                try{
                    finalDb.update(sbCatalogEle.toString());
                }catch (SQLException e){
                    e.printStackTrace();
                }*/
                int idSSL =  MongodbUtils.count(ServerCatalogList.class,new Criteria())+1;
                ServerCatalogList serverCatalogList = new ServerCatalogList();
                serverCatalogList.setId(idSSL);
                serverCatalogList.setServerCatalogId(templateId);
                serverCatalogList.setServerElementName(dr.getName());
                serverCatalogList.setElementDescribe(dr.getDescription());
                serverCatalogList.setUpddatetime(DateUtil.getNow());
                serverCatalogList.setKeycol(1);
                serverCatalogList.setDbkey(0);
                MongodbUtils.save(serverCatalogList);
            }
            //发布接口 SERVER_CATALOG
           /* StringBuilder sbCatalog = new StringBuilder();
            //sb.append("Insert into SERVER_CATALOG (SERVER_NAME,BUSINESS_SYSTEM_ID,EXAMPLES,SPECIFY_SYSTEM,SPECIFY_SYSTEM_ID_LIST,LASTDATE,REMARK,SERVER_SHORT,DELETED,TEMPLATE_ID,PUBLISH_TYPE,DB_ID,CATALOGUE)")
            sbCatalog.append("Insert into SERVER_CATALOG (ID,BUSINESS_SYSTEM_ID,SERVER_NAME,SPECIFY_SYSTEM,LASTDATE,SERVER_SHORT,DELETED,DB_ID,TEMPLATE_ID,PUBLISH_TYPE) values")
            .append("(SEQ_SERVER_CATALOG_PK.nextval,")
            .append("1,")
            .append("'").append(df.getName()).append("',")
            .append("'N',")
            .append("to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')").append(",")
            .append("'").append(df.getName()).append("',")
            .append("'N',")
            .append("'c5fd2a47d4884423b104fc2533b8a328',")
            .append(templateId).append(",")
            .append("'1')");*/
            int idSc =  MongodbUtils.count(ServerCatalog.class,new Criteria())+1;
            ServerCatalog serviceCatalog = new ServerCatalog();
            serviceCatalog.setId(idSc);
            serviceCatalog.setBusinessSystemId(1);
            serviceCatalog.setServerName(df.getName());
            serviceCatalog.setSpecifySystem("N");
            serviceCatalog.setUpddatetime(DateUtil.getNow());
            serviceCatalog.setServerShort(df.getName());
            serviceCatalog.setDeleted("N");
            serviceCatalog.setDbId("c5fd2a47d4884423b104fc2533b8a328");
            serviceCatalog.setTemplateId(templateId+"");
            serviceCatalog.setPublishType("1");
            MongodbUtils.save(serviceCatalog);
        });
    }
}
