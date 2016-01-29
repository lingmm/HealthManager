package com.shyling.healthmanager.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shy on 2015/11/8.
 */
public class CheckUp {
    private int id;
    @Expose
    @SerializedName("date")
    private String checkUpDate;//日期
    @Expose
    private float height;//身高
    @Expose
    private float weight;//体重
    @Expose
    private int sbp;//血压-高
    @Expose
    private int dbp;//血压-低
    @Expose
    private int pulse;//心率
    private int user;
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

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
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
}
