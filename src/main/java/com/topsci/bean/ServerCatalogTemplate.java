package com.topsci.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * 接口模板类
 */

@Document
public class ServerCatalogTemplate {
    @Id
    private int id;
    private String name;
    private String remark;
    private String status;
    private String createUser;
    private String typeuuid;
    private LocalDateTime createTime;
    private LocalDateTime invalidTime;
    private String invalidRemark;
    private String tplcode;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getTypeuuid() {
        return typeuuid;
    }

    public void setTypeuuid(String typeuuid) {
        this.typeuuid = typeuuid;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(LocalDateTime invalidTime) {
        this.invalidTime = invalidTime;
    }

    public String getInvalidRemark() {
        return invalidRemark;
    }

    public void setInvalidRemark(String invalidRemark) {
        this.invalidRemark = invalidRemark;
    }

    public String getTplcode() {
        return tplcode;
    }

    public void setTplcode(String tplcode) {
        this.tplcode = tplcode;
    }
}
