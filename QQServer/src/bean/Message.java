package bean;

import java.io.Serializable;
//瀵瑰簲Message琛紝涓�涓瓧娈靛搴斾竴涓垚鍛樺彉閲�
public class Message implements Serializable{
	
	private int id;
	private int selfAccount;
	private int firendAccount;
	private int cmd;
	private String msgcontent;	
	
	
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
	public int getCmd() {
		return cmd;
	}
	public void setCmd(int cmd) {
		this.cmd = cmd;
	}
	public String getMsgcontent() {
		return msgcontent;
	}
	public void setMsgcontent(String msgcontent) {
		this.msgcontent = msgcontent;
	}
	
}
