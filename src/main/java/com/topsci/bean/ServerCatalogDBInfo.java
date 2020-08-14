package com.topsci.bean;

import java.io.Serializable;

public class ServerCatalogDBInfo implements Serializable {

    private String uuid;

    //1:ORACLE  2:SQLSERVER  3:MYSQL
    private String dbtype;

    private String dbip;
    private String dbport;
    private String dbname;
    private String username;

    //需要使用EncryptUtil解密
    private String password;



    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDbtype() {
        return dbtype;
    }

    public void setDbtype(String dbtype) {
        this.dbtype = dbtype;
    }

    public String getDbip() {
        return dbip;
    }

    public void setDbip(String dbip) {
        this.dbip = dbip;
    }

    public String getDbport() {
        return dbport;
    }

    public void setDbport(String dbport) {
        this.dbport = dbport;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
