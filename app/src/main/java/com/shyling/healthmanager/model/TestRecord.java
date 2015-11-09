package com.shyling.healthmanager.model;

/**
 * Created by shy on 2015/11/8.
 */
public class TestRecord {
    private int id;
    private long time;//时间戳
    private float height;//身高
    private float weight;//体重
    private int hbp;//血压-高
    private int lbp;//血压-低
    private int pulse;//心率
    private int user;

    public float getHbp() {
        return hbp;
    }

    public void setHbp(int hbp) {
        this.hbp = hbp;
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

    public float getLbp() {
        return lbp;
    }

    public void setLbp(int lbp) {
        this.lbp = lbp;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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

    @Override
    public String toString() {
        return  "高压:" + hbp +
                ", 身高=" + height +
                ", 低压=" + lbp +
                ", 心率=" + pulse +
                ", 体重=" + weight;
    }
}
