package server;

import java.io.IOException;
import java.io.InputStream;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import bean.Account;
import db.DBOper;
import io.ObjectInputStream;
import io.ObjectOutputStream;
import send.SendMsg;

public class MyServer extends Thread {
	public static void main(String[] args) {
		new ServerUI().initServerUI();
	}
HashMap<Account, Socket> member =new HashMap<Account, Socket>();
	ServerSocket server;
	// 设置服务器
	public void setServer(int port) {
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("服务器" + port + "创建成功！");
		this.start();
	}

	public void run() {
		while (true) {
			try {
				Socket client = server.accept();		
			InputStream is = client.getInputStream();
			
			OutputStream os = client.getOutputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			ObjectOutputStream oos =new ObjectOutputStream(os);
			SendMsg msg = null;
//			try {
				msg = (SendMsg)ois.readObject();
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			}
				oos.writeObject(new DBOper().login(msg.selfAccount));	
				client.setKeepAlive(true);
		
//			ClientManager.addMember(msg.selfAccount.getQqCode(), client);
				ClientManager.out.put(msg.selfAccount.getQqCode(), oos);
				ClientManager.ois.put(msg.selfAccount.getQqCode(), ois);
			//启动线程处理信息收发
			MyServerThread myt = new MyServerThread(ois,oos);
			myt.start();
			System.out.println(msg.selfAccount.getNickName() + "上线啦！在线人数：" + ClientManager.out.size());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
