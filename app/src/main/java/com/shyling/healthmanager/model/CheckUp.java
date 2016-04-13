package com.shyling.healthmanager.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by shy on 2015/11/8.
 */
public class CheckUp {
    @JSONField(serialize = false, deserialize = false)
    private int id;
    @JSONField(name = "date")
    private String checkUpDate;//日期
    private float height;//身高
    private float weight;//体重
    private int sbp;//血压-高
    private int dbp;//血压-低
    private int pulse;//心率
    @JSONField(serialize = false, deserialize = false)
    private String user;
    @JSONField(serialize = false, deserialize = false)
    private long sent;//是否已经发送到服务器,0=>未发送,时间戳=>发送的时间

    public float getSbp() {
        return sbp;
    }

    public void setSbp(int sbp) {
        this.sbp = sbp;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getDbp() {
        return dbp;
    }

    public void setDbp(int dbp) {
        this.dbp = dbp;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public String getCheckUpDate() {
        return checkUpDate;
    }

    public void setCheckUpDate(String checkUpDate) {
        this.checkUpDate = checkUpDate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public long getSent() {
        return this.sent;
    }

    public void setSent(long sent) {
        this.sent = sent;
    }

    @Override
    public String toString() {
        return "高压:" + sbp +
                ", 低压=" + dbp +
                ", 心率=" + pulse +
                ", 身高=" + height +
                ", 体重=" + weight;
    }

    public CheckUp(int id, String checkUpDate, float height, float weight, int sbp, int dbp, int pulse, String user, long sent) {
        this.id = id;
        this.checkUpDate = checkUpDate;
        this.height = height;
        this.weight = weight;
        this.sbp = sbp;
        this.dbp = dbp;
        this.pulse = pulse;
        this.user = user;
        this.sent = sent;
    }

    public CheckUp() {
    }
}
