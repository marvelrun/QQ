package bean;

import java.io.Serializable;
//对应Friend表，�?个字段对应一个成员变�?
public class Friend implements Serializable{
	private int id;
	private int selfAccount;
	private int firendAccount;
	private String groupname;
	private int invalid;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSelfAccount() {
		return selfAccount;
	}
	public void setSelfAccount(int selfAccount) {
		this.selfAccount = selfAccount;
	}
	public int getFirendAccount() {
		return firendAccount;
	}
	public void setFirendAccount(int firendAccount) {
		this.firendAccount = firendAccount;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public int getInvalid() {
		return invalid;
	}
	public void setInvalid(int invalid) {
		this.invalid = invalid;
	}
	
	
}
