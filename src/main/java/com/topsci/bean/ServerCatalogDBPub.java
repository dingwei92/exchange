package com.topsci.bean;

public class ServerCatalogDBPub {
    private String uuid;
    private String dbid;
    private String table_name;
    private String table_where;

    //0：使用数据表  1：使用sql语句
    private String select_type;

    private String select_sql;
    private String date_col;
    private String date_col_type;
    private String cols;

    public String getTable_name() {
        return table_name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDbid() {
        return dbid;
    }

    public void setDbid(String dbid) {
        this.dbid = dbid;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getTable_where() {
        return table_where;
    }

    public void setTable_where(String table_where) {
        this.table_where = table_where;
    }

    public String getSelect_type() {
        return select_type;
    }

    public void setSelect_type(String select_type) {
        this.select_type = select_type;
    }

    public String getSelect_sql() {
        return select_sql;
    }

    public void setSelect_sql(String select_sql) {
        this.select_sql = select_sql;
    }

    public String getDate_col() {
        return date_col;
    }

    public void setDate_col(String date_col) {
        this.date_col = date_col;
    }

    public String getCols() {
        return cols;
    }

    public void setCols(String cols) {
        this.cols = cols;
    }

    public String getDate_col_type() {
        return date_col_type;
    }

    public void setDate_col_type(String date_col_type) {
        this.date_col_type = date_col_type;
    }
}
