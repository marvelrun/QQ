package qq.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import bean.Account;
import cmd.Cmd;
import io.ObjectInputStream;
import io.ObjectOutputStream;
import send.SendMsg;


public class Login extends JFrame implements ActionListener,MouseListener{
	JComboBox <String>cbqqCode;//下拉框
	JPasswordField txtpwd;
	JLabel lblface,lblfindpwd,lblReg,lblhead;
	JButton btnReg,btnLogin,btnClose;
	JCheckBox cbmemory,cbautologin; 
	ObjectOutputStream oot;
	ObjectInputStream ois ;
	public Login()
	{	
		this.setUndecorated(true);//消除边框
		Image icon =new ImageIcon("face/qe.jpg").getImage();	
		setIconImage(icon);
		lblhead=new JLabel(new ImageIcon("images/newLogin.jpg"));
		add(lblhead);
		
		//读取之前登录的QQ号码 存入下拉窗
		String qqcodes[]=readQQCode();
		if(qqcodes!=null)
		{
			cbqqCode=new JComboBox<String>(qqcodes);
			cbqqCode.setSelectedIndex(1);
		}else
		{
			cbqqCode=new JComboBox<String>();
		}		
		txtpwd=new JPasswordField();
		lblfindpwd=new JLabel("找回密码");
		lblReg=new JLabel("注册用户");
		
		cbmemory=new JCheckBox("记住密码");
		cbautologin=new JCheckBox("自动登录");
		
		cbqqCode.setBounds(120, 190, 182, 35);
		cbqqCode.setEditable(true);
		
		
		lblReg.setBounds(320, 190, 182, 30);
		lblReg.setForeground(Color.BLUE);
		lblReg.setFont(new Font("微软雅黑",Font.PLAIN,12));	
		txtpwd=new JPasswordField("1");
		txtpwd.setBounds(120, 230, 182, 30);
		
		lblfindpwd.setBounds(320, 230, 182, 30);
		lblfindpwd.setForeground(Color.BLUE);
		lblfindpwd.setFont(new Font("微软雅黑",Font.PLAIN,12));
		cbmemory.setBounds(130, 260, 80, 30);
		cbmemory.setForeground(Color.BLUE);
		cbmemory.setFont(new Font("微软雅黑",Font.PLAIN,12));
		cbautologin.setBounds(210, 260, 80, 30);
		cbautologin.setForeground(Color.BLUE);
		cbautologin.setFont(new Font("微软雅黑",Font.PLAIN,12));
		btnLogin=new JButton("登录");
		btnLogin.setBounds(135, 290, 150, 30);
		
		
		
		lblhead.add(cbqqCode);
		lblhead.add(lblReg);
		lblhead.add(txtpwd);
		lblhead.add(lblfindpwd);
		lblhead.add(cbmemory);
		lblhead.add(cbautologin);
		lblhead.add(btnLogin);
		lblface=new JLabel(new ImageIcon("face/qe.jpg"));
		lblface.setBounds(30, 190, 60, 60);
		lblhead.add(lblface);
		
		btnLogin.addActionListener(this);
//		cbmemory.addItemListener(this);
		cbmemory.setBackground(Color.white);
//		cbautologin.addItemListener(this);
		cbautologin.setBackground(Color.white);
//		cbqqCode.addItemListener(this);
		lblfindpwd.addMouseListener(this);
		lblReg.addMouseListener(this);		
		lblhead.addMouseListener(this);		
		setSize(428,325);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);		
	}
	
	public static void main(String[] args)
	{
		new Login();
	}

	public void actionPerformed(ActionEvent e) {
		
		//把界面上的账号密码设置给javaBean
		String qqcode=cbqqCode.getSelectedItem().toString();
		String pwds=new String(txtpwd.getPassword()).trim();
		Account acc=new Account();
		acc.setQqCode(Integer.parseInt(qqcode));
		acc.setPwd(pwds);
		//登录
		try {
			acc=login(acc);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		//登录成功
		if(acc.getStatus()==1)
		{
			Icon ico=new ImageIcon(acc.getFace());//获取头像
			lblface.setIcon(ico);//设置头像为该登录用户的头像（登录时刻已从数据库取出头像索引存在account对象中）			
			saveQQCode(acc.getQqCode()+"");//把登录成功的qq号保存在account.dat文件中
			JOptionPane.showMessageDialog(null, "登录成功");			
			new MainUI(acc,oot,ois);
			this.dispose();//关闭登录窗口 显示主窗体
		}else {
			JOptionPane.showMessageDialog(null, "账号或密码错误，请重新输入^.^");
		}
	}

	// 登录认证
	public Account login(Account acc) throws ClassNotFoundException {
		Account ac = null;
		Socket client=null;
			try {
				 client = new Socket(Cmd.serverIp, Cmd.LoginPort);
				client.setKeepAlive(true);
				InputStream is = client.getInputStream();
				OutputStream os = client.getOutputStream();
				oot=new ObjectOutputStream(os);
				ois = new ObjectInputStream(is);
				 SendMsg msg = new SendMsg();
				 msg.Cmd=Cmd.CMD_LOGIN;
				 msg.selfAccount=acc;
				oot.writeObject(msg);
				ac=(Account)ois.readObject();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}			
			return ac;
	}

	//当对象内容改变时（比如当qq号码下拉框填写的qq号码改变时）
//	public void itemStateChanged(ItemEvent e) {
//	
//
//		if(e.getSource()==cbqqCode)//更改头像
//		{
//			String qqcode=cbqqCode.getSelectedItem().toString();//得到下拉框
//			String face=new Service().getFace(Integer.parseInt(qqcode));//在数据库得到qq对应头像
//			lblface.setIcon(new ImageIcon(face));//改变头像框中的图片
//		}
//	}


	public void mouseClicked(MouseEvent e) {
		
		//在注册用户标签上点击鼠标 产生鼠标事件被检测到  注册用户
		if(e.getSource()==lblReg)
		{
			new RegUsers();
		}
		
		//面板点击鼠标 关闭面板
		if(e.getSource()==lblhead)
		{
			if(e.getClickCount()==2)
			{dispose();
			
			}
		}
	}

	public void mouseEntered(MouseEvent arg0) {
		
	}

	public void mouseExited(MouseEvent arg0) {
		
	}

	public void mousePressed(MouseEvent arg0) {
		
	}

	public void mouseReleased(MouseEvent arg0) {
		
	}

	//读曾经登录过的QQ账号
	public String[] readQQCode()
	{
		String[] qqcodes=null;
		File file=new File("account.dat");
		try
		{
			
			//文件不存在
			if(!file.exists())
			{
				file.createNewFile();
			}
		
			BufferedReader br=new BufferedReader(new FileReader(file));//FileReader把字节流转换为字符流
			String str=br.readLine();
			br.close();
			//文件内容转换为数组
			if(str==null)
				return null;
			qqcodes=str.split(",");
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return qqcodes;
	}
	
	
	
	//创建保存QQ账号的文件保存登录框中的之前登陆过的的QQ账号
	public void saveQQCode(String qqcode)
	{	
		try
		{	
			//文件内容转换为数组
			String[] qqcodes=readQQCode();
			//用集  避免元素重复
			Set<String> set=new HashSet<String>();
			String str="";
			if(qqcodes!=null)
			{						
				for(int i=0;i<qqcodes.length;i++)
				{
					set.add(qqcodes[i]);
				}
				set.add(qqcode);
			
				Iterator<String> it=set.iterator();
				
				while(it.hasNext())
				{
					str+=it.next()+",";			
				}
			}else {
				str=qqcode+"";			
			}
			//保存回文件
			File f=new File("account.dat");
			BufferedWriter bw=new BufferedWriter(new FileWriter(f));
			bw.write(str);
			bw.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}



}
