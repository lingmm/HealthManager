package com.shyling.healthmanager.model;

/**
 * Created by Mars on 2015/11/9.
 */
public class User {
    private String userName;
    private String passWd;
    private String uName;
    private String birthDay;
    private String cellPhone;

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWd() {
        return passWd;
    }

    public void setPassWd(String passWd) {
        this.passWd = passWd;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public User(String userName,  String uName, String birthDay, String cellPhone) {
        this.userName = userName;
        this.uName = uName;
        this.birthDay = birthDay;
        this.cellPhone = cellPhone;
    }

    public User() {
    }
}
