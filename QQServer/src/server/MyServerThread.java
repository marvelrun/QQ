package server;

import java.io.IOException;
//import java.io.ObjectInputStream
//import java.io.ObjectOutputStream;
import cmd.Cmd;
import db.DBOper;
import io.ObjectInputStream;
import io.ObjectOutputStream;
import send.SendMsg;

public class MyServerThread extends Thread {
	ObjectInputStream ois;
	ObjectOutputStream oos;

	public MyServerThread(ObjectInputStream ois, ObjectOutputStream oos) {
		this.ois = ois;
		this.oos = oos;
	}

	public void run() {
		try {
			service();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void service() throws IOException, ClassNotFoundException {
		while (true) {
			SendMsg msg = (SendMsg) ois.readObject();			
//			System.out.println("收到客户端消息"+msg.Cmd);
			switch (msg.Cmd) {
			case Cmd.getFace:// 获取头像
				oos.writeObject(new DBOper().getFace(msg.selfAccount.getQqCode()));
				break;
			case Cmd.find:
				SendMsg inMsg1 = new SendMsg();
				inMsg1.Cmd = Cmd.find;
				inMsg1.vector = new DBOper().find(msg.friendAccount);
				oos.writeObject(inMsg1);
				break;
			case Cmd.getAllInfo:
				SendMsg inMsg = new SendMsg();
				inMsg.Cmd = Cmd.getAllInfo;
				inMsg.vector = new DBOper().getAllInfo(msg.selfAccount);
				oos.writeObject(inMsg);
				break;
			case Cmd.addFriend:
				new DBOper().addFriend(msg.selfAccount, msg.friendAccount.getQqCode());
				break;
			case Cmd.delFriend:
				new DBOper().delFriend(msg.selfAccount, msg.friendAccount.getQqCode());
				break;
			// case Cmd.modifyStatus:
			// oos.writeObject(new DBOper().modifyStatus(msg.selfAccount, status));
			// break;

			case Cmd.findByQQcode:
				SendMsg fMsg = new SendMsg();
				fMsg.Cmd = Cmd.findByQQcode;
				fMsg.friendAccount = new DBOper().findByQQcode(msg.friendAccount.getQqCode());
				oos.writeObject(fMsg);
				break;
			default:// 转发聊天信息
//				System.out.println("转发消息");			
				ObjectOutputStream fOos =  ClientManager.out.get(msg.friendAccount.getQqCode());
				fOos.writeObject(msg);
				fOos.flush();
				break;
			}
		}

	}

}
