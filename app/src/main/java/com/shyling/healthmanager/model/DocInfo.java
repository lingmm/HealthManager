package com.shyling.healthmanager.model;

import java.io.Serializable;
/**
 * JavaBean
 * 对应数据库表tb_DocInfo
 * 
 * @author 刘帅斐
 *  2015年7月21日
 *
 */
public class DocInfo implements Serializable {
	
	private int docId;
	private String userName;
	private String gender;
	private String photo;
	private String passWd;
	private String question;
	private String answer;
	private String email;
	private String docName;
	private String title;
	private String strongPoint;
	private String workUnit;
	private String contact;
	private String intro;
	private String status;
	private String type;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getDocId() {
		return docId;
	}
	public void setDocId(int docId) {
		this.docId = docId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStrongPoint() {
		return strongPoint;
	}
	public void setStrongPoint(String strongPoint) {
		this.strongPoint = strongPoint;
	}
	public String getWorkUnit() {
		return workUnit;
	}
	public void setWorkUnit(String workUnit) {
		this.workUnit = workUnit;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String Contact) {
		this.contact = Contact;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "DocInfo [docId=" + docId + ", userName=" + userName
				+ ", gender=" + gender + ", photo=" + photo + ", passWd="
				+ passWd + ", question=" + question + ", answer=" + answer
				+ ", email=" + email + ", docName=" + docName + ", title="
				+ title + ", strongPoint=" + strongPoint + ", workUnit="
				+ workUnit + ", contact=" + contact + ", intro=" + intro
				+ ", status=" + status + ", type=" + type + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((answer == null) ? 0 : answer.hashCode());
		result = prime * result + ((contact == null) ? 0 : contact.hashCode());
		result = prime * result + docId;
		result = prime * result + ((docName == null) ? 0 : docName.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + ((intro == null) ? 0 : intro.hashCode());
		result = prime * result + ((passWd == null) ? 0 : passWd.hashCode());
		result = prime * result + ((photo == null) ? 0 : photo.hashCode());
		result = prime * result
				+ ((question == null) ? 0 : question.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result
				+ ((strongPoint == null) ? 0 : strongPoint.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		result = prime * result
				+ ((workUnit == null) ? 0 : workUnit.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocInfo other = (DocInfo) obj;
		if (answer == null) {
			if (other.answer != null)
				return false;
		} else if (!answer.equals(other.answer))
			return false;
		if (contact == null) {
			if (other.contact != null)
				return false;
		} else if (!contact.equals(other.contact))
			return false;
		if (docId != other.docId)
			return false;
		if (docName == null) {
			if (other.docName != null)
				return false;
		} else if (!docName.equals(other.docName))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (intro == null) {
			if (other.intro != null)
				return false;
		} else if (!intro.equals(other.intro))
			return false;
		if (passWd == null) {
			if (other.passWd != null)
				return false;
		} else if (!passWd.equals(other.passWd))
			return false;
		if (photo == null) {
			if (other.photo != null)
				return false;
		} else if (!photo.equals(other.photo))
			return false;
		if (question == null) {
			if (other.question != null)
				return false;
		} else if (!question.equals(other.question))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (strongPoint == null) {
			if (other.strongPoint != null)
				return false;
		} else if (!strongPoint.equals(other.strongPoint))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		if (workUnit == null) {
			if (other.workUnit != null)
				return false;
		} else if (!workUnit.equals(other.workUnit))
			return false;
		return true;
	}
	
	

}
