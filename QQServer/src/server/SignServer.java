package server;


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import bean.Account;
import db.DBOper;

public class SignServer implements Runnable {

	ServerSocket server;

	public void setServer(int port) {
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Thread t = new Thread(this);
		t.start();
	}

	public void run() {
		while (true) {		
				Socket client;
				try {
					client = server.accept();
					OutputStream out =client.getOutputStream();
					InputStream is = client.getInputStream();
					ObjectInputStream ois = new ObjectInputStream(is);
					//接收用户并返回修改状态后的用户
					Account acc = (Account)ois.readObject();
										
							if(new DBOper().sign(acc)){
								out.write(1);
							}else {
								out.write(0);
							}		
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			
		}
		}

}


