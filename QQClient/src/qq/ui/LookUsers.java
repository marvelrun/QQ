package qq.ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import bean.Account;

public class LookUsers extends JFrame implements ActionListener,MouseListener{

	
	
	private Account account;
	private JLabel lblbg;//背景图片


	public LookUsers(Account account)
	{	
		this.setUndecorated(true);
		this.account=account;
		init();
	
	}
	//初始化界面
	public void init()
	{
		Icon bgimg=new ImageIcon("images/bj_hyzl.jpg");
		lblbg=new JLabel(bgimg);		
		add(lblbg);
		setResizable(false);//不能更改窗口大小
		JLabel lblTitle=new JLabel("好友资料");
		lblTitle.setFont(new Font("微软雅黑",Font.BOLD,15));
		lblTitle.setBounds(5,15,150,40);
		lblbg.add(lblTitle);
//		int i=0;
		
		JLabel lblface=new JLabel(new ImageIcon(account.getFace()),JLabel.RIGHT);
		lblface.setBounds(310,70,80,80);
		
		lblbg.add(lblface);
		
		
		JLabel lblqqcode=new JLabel("QQ号码: "+account.getQqCode(),JLabel.LEFT);
		JLabel lblnickname=new JLabel("昵称: "+account.getNickName(),JLabel.LEFT);
		JLabel lblage=new JLabel("年龄: "+account.getAge(),JLabel.LEFT);
		JLabel lblsex=new JLabel("性别: "+account.getSex(),JLabel.LEFT);
		JLabel lblnation=new JLabel("民族: "+account.getNation(),JLabel.LEFT);
		JLabel lblstar=new JLabel("星座: "+account.getStar(),JLabel.LEFT);
		JLabel lblremark=new JLabel("备注: "+account.getRemark(),JLabel.LEFT);
		JLabel lblselfsign=new JLabel("个性签名: "+account.getSelfsign(),JLabel.LEFT);
		

		lblqqcode.setBounds(90,70,160,25);
		
		lblbg.add(lblqqcode);
	
	
		lblnickname.setBounds(90,110,160,25);
	
		lblbg.add(lblnickname);

	
		lblage.setBounds(90,150,160,25);
		
		lblbg.add(lblage);
		
				
		lblsex.setBounds(310,150,160,25);
		
//		ButtonGroup bg=new ButtonGroup();
		
		lblbg.add(lblsex);
		
	
		lblnation.setBounds(90,190,160,25);
	
		lblbg.add(lblnation);
	
		
		
	
		lblstar.setBounds(310,190,160,25);

		lblbg.add(lblstar);
	
		

		lblremark.setBounds(90,230,160,25);
		
		lblbg.add(lblremark);
	
		
		
		lblselfsign.setBounds(90,270,160,25);
	
		lblbg.add(lblselfsign);

		lblbg.addMouseListener(this);
		
		setResizable(false);
		setSize(500,440);
		setVisible(true);
		setLocationRelativeTo(null);
		
		
	}


	public void actionPerformed(ActionEvent e) {
		
		dispose();
		
	
	}
	public void mouseClicked(MouseEvent e) {
		if(e.getSource()==lblbg)
		{
			if(e.getClickCount()==2)
			{

				dispose();
				
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
	
	
	
	
}
