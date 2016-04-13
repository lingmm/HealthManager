package com.shyling.healthmanager.model;

import java.io.Serializable;
/**
 * JavaBean
 * 对应数据库表tb_UserInfo
 * 
 * @author 刘帅斐
 *  2015年7月21日
 *
 */
public class UserInfo implements Serializable {
	
	private String userName;
	private String passWd;
	private String question;
	private String answer;
	private String email;
	private String uname;
	private String cellphone;
	private String gender;
	private String photo;
	private String birthDay;
	private String idCard;
	private String status;
	private String type;
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getCellphone() {
		return cellphone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getBirthDay() {
		return birthDay;
	}
	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "UserInfo [userName=" + userName + ", passWd=" + passWd
				+ ", question=" + question + ", answer=" + answer + ", email="
				+ email + ", uname=" + uname + ", cellphone=" + cellphone
				+ ", gender=" + gender + ", photo=" + photo + ", birthDay="
				+ birthDay + ", idCard=" + idCard + ", status=" + status
				+ ", type=" + type + "]";
	}
	
}
