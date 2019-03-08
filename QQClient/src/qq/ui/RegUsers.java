package qq.ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import bean.Account;



public class RegUsers extends JFrame implements ActionListener{
	private JLabel lblbg;//背景图片
	private JComboBox<Icon>cbface;
	private JComboBox<String> nation,star;
	private JTextField qqCode,nickName,age,remark;
	private JRadioButton sex1,sex2;
	private JTextArea selfsign;
	private JPasswordField pwd,cfgpwd;
	private JButton btnSave,btnClose;
	private String nations[] = {"人族","兽族","神族"};
	private String stars[] = {"处女座","天蝎座","狮子座"};
	private String[] faces= {
			"face/0.jpg",
			"face/1.jpg",
			"face/2.jpg",
			"face/3.jpg",
			"face/4.jpg",
			"face/5.jpg",
			"face/6.jpg",
	};
	
	public RegUsers()
	{
		this.setUndecorated(true);
		init();
	}
	//初始化界面 背景图片放在label label直接设为背景  其他东西都放在该label
	public void init()
	{
		Icon bgimg=new ImageIcon("images/bg5.jpg");
		lblbg=new JLabel(bgimg);		
		add(lblbg);
		setResizable(false);//不能更改窗口大小
		JLabel lblTitle=new JLabel("个人资料");
		lblTitle.setFont(new Font("微软雅黑",Font.BOLD,20));
		lblTitle.setBounds(5,25,150,40);
		lblbg.add(lblTitle);
		int i=0;
		Icon[] face= {
				new ImageIcon(faces[i++]),
				new ImageIcon(faces[i++]),
				new ImageIcon(faces[i++]),
				new ImageIcon(faces[i++]),
				new ImageIcon(faces[i++]),
				new ImageIcon(faces[i++]),
				new ImageIcon(faces[i++]),
		};
		
		cbface=new JComboBox<Icon>(face);
		cbface.setMaximumRowCount(5);
		JLabel lblface=new JLabel("头像:",JLabel.RIGHT);
		lblface.setBounds(270,70,60,25);
		cbface.setBounds(330,70,80,60);
		lblbg.add(lblface);
		lblbg.add(cbface);
		
		JLabel lblqqcode=new JLabel("QQ号码: ",JLabel.RIGHT);
		JLabel lblnickname=new JLabel("昵称: ",JLabel.RIGHT);
		JLabel lblpwd=new JLabel("登录密码: ",JLabel.RIGHT);
		JLabel lblcfgpwd=new JLabel("确认密码: ",JLabel.RIGHT);
		JLabel lblage=new JLabel("年龄: ",JLabel.RIGHT);
		JLabel lblsex=new JLabel("性别: ",JLabel.RIGHT);
		JLabel lblnation=new JLabel("民族: ",JLabel.RIGHT);
		JLabel lblstar=new JLabel("星座: ",JLabel.RIGHT);
		JLabel lblremark=new JLabel("备注: ",JLabel.RIGHT);
		JLabel lblselfsign=new JLabel("个性签名: ",JLabel.RIGHT);
		
		qqCode=new JTextField(15);
		lblqqcode.setBounds(10,70,100,25);
		qqCode.setBounds(110,70,100,25);
		lblbg.add(lblqqcode);
		lblbg.add(qqCode);
		
		nickName=new JTextField(15);
		lblnickname.setBounds(10,110,100,25);
		nickName.setBounds(110,110,100,25);
		lblbg.add(lblnickname);
		lblbg.add(nickName);
		
		pwd=new JPasswordField(15);
		cfgpwd=new JPasswordField(15);
		lblpwd.setBounds(50,150,60,25);
		lblcfgpwd.setBounds(270,150,60,25);
		pwd.setBounds(110,150,100,25);
		cfgpwd.setBounds(330,150,100,25);
		lblbg.add(lblpwd);
		lblbg.add(pwd);
		lblbg.add(lblcfgpwd);
		lblbg.add(cfgpwd);	
		
		age=new JTextField("0");
		lblage.setBounds(50,190,60,25);
		age.setBounds(110,190,100,25);
		lblbg.add(lblage);
		lblbg.add(age);
		sex1=new JRadioButton("男");
		sex2=new JRadioButton("女");
		sex1.setSelected(true);
		lblsex.setBounds(270,230,60,25);
		sex1.setBounds(330,230,60,25);
		sex2.setBounds(390,230,60,25);
		ButtonGroup bg=new ButtonGroup();
		bg.add(sex1);
		bg.add(sex2);
		lblbg.add(lblsex);
		lblbg.add(sex1);
		lblbg.add(sex2);
		
		nation =new JComboBox<String>(nations);
		lblnation.setBounds(50,270,60,25);
		nation.setBounds(110,270,100,25);
		lblbg.add(lblnation);
		lblbg.add(nation);
		
		
		star =new JComboBox<String>(stars);
		lblstar.setBounds(270,270,60,25);
		star.setBounds(330,270,100,25);
		lblbg.add(lblstar);
		lblbg.add(star);
		
		remark =new JTextField();
		lblremark.setBounds(50,310,60,25);
		remark.setBounds(110,310,320,25);
		lblbg.add(lblremark);
		lblbg.add(remark);
		
		selfsign =new JTextArea();
		lblselfsign.setBounds(50,350,60,25);
		selfsign.setBounds(110,350,320,25);
		lblbg.add(lblselfsign);
		lblbg.add(selfsign);
		
		btnSave=new JButton("保存");
		btnClose=new JButton("关闭");
		btnSave.addActionListener(this);
		btnClose.addActionListener(this);
		btnSave.setBounds(170,450,80,30);
		btnClose.setBounds(270,450,80,30);
		lblbg.add(btnSave);
		lblbg.add(btnClose);
				
		
		setResizable(false);
		setSize(500,600);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//关闭当前窗口 不关闭整个程序
		
	}

		
		
		
		
//	}
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==btnClose)
		{
		//	System.exit(0);
			dispose();//关闭窗口
		}
		
		if(e.getSource()==btnSave)
		{
			Account account=new Account();
			try {
				
				Integer.parseInt(qqCode.getText().trim());
				
			}catch(Exception ex)
			{
				JOptionPane.showMessageDialog(null, "QQ号码有误 必须为合法数字");
				return;
			}
			//验证密码和确认密码是否一�?
			if(new String(pwd.getPassword()).trim()=="")
			{
				JOptionPane.showMessageDialog(null,"请输入密码");
				return;
			}
			if(!new String(pwd.getPassword()).equals(new String(cfgpwd.getPassword())))
			{
				JOptionPane.showMessageDialog(null,"登录密码与确认密码不一致");
				return;
			}
			int nage=0;
			try
			{
				nage=Integer.parseInt(age.getText().trim());		
			}catch(Exception ex)
			{
				JOptionPane.showInputDialog(null, "年龄含有非法字符");
				return;
			}
			if(nage<1 || nage>150)
			{
				JOptionPane.showInputDialog(null, "年龄超范围");
				return;
				
			}
			//把数值设置到对象�?
			account.setQqCode(Integer.parseInt(qqCode.getText().trim()));
			account.setNickName(nickName.getText().trim());
			account.setPwd(new String(pwd.getPassword()).trim());
			account.setFace(faces[cbface.getSelectedIndex()]);
			account.setAge(Integer.parseInt(age.getText().trim()));
			if(sex1.isSelected())
			{
				account.setSex("男");
			}
			else
			{
				account.setSex("女");
			}
			account.setNation(nations[nation.getSelectedIndex()]);
			account.setStar(stars[star.getSelectedIndex()]);
			account.setRemark(remark.getText().trim());
			account.setSelfsign(selfsign.getText().trim());
			account.setStatus(0);
			int bok = 0;
			Socket sclient ;
			try {		
				sclient = new Socket("localhost",8766);
				ObjectOutputStream oos = new ObjectOutputStream(sclient.getOutputStream());			
						oos.writeObject(account);				
							bok = sclient.getInputStream().read();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
			if(bok==1){
				
				JOptionPane.showMessageDialog(null, "注册成功！请记住qq号码");
				
			}else {
				
				JOptionPane.showMessageDialog(null, "注册失败");
				
			}
			
			}
		}
		
	}
	
	
	
	

