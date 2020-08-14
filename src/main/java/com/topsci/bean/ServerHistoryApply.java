package com.topsci.bean;

import java.io.Serializable;

/**
 * Created by lzw on 2016/12/26.
 */
public class ServerHistoryApply implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8378596218361921359L;

    //id
    int id;
    //订阅id
    String serverSubscribesId;
    //订阅参数
    String parameter;
    //状态
    String status;
    private String area_code;
    private String organ_code;
    private String id_card;
    private String health_id;
    private String phr_id;
    private String start_time;
    private String end_time;

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public String getOrgan_code() {
        return organ_code;
    }

    public void setOrgan_code(String organ_code) {
        this.organ_code = organ_code;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getHealth_id() {
        return health_id;
    }

    public void setHealth_id(String health_id) {
        this.health_id = health_id;
    }

    public String getPhr_id() {
        return phr_id;
    }

    public void setPhr_id(String phr_id) {
        this.phr_id = phr_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServerSubscribesId() {
        return serverSubscribesId;
    }

    public void setServerSubscribesId(String serverSubscribesId) {
        this.serverSubscribesId = serverSubscribesId;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
