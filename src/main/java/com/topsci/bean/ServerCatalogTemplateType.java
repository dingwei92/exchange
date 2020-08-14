package com.topsci.bean;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class ServerCatalogTemplateType {
    @Id
    private String uuid;
    private String dbuuid;
    private String typename;
    private String remark;
    private LocalDateTime createtime;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDbuuid() {
        return dbuuid;
    }

    public void setDbuuid(String dbuuid) {
        this.dbuuid = dbuuid;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getCreatetime() {
        return createtime;
    }

    public void setCreatetime(LocalDateTime createtime) {
        this.createtime = createtime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServerCatalogTemplateType that = (ServerCatalogTemplateType) o;

        if (uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) return false;
        if (typename != null ? !typename.equals(that.typename) : that.typename != null) return false;
        if (remark != null ? !remark.equals(that.remark) : that.remark != null) return false;
        if (createtime != null ? !createtime.equals(that.createtime) : that.createtime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (typename != null ? typename.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        result = 31 * result + (createtime != null ? createtime.hashCode() : 0);
        return result;
    }
}
