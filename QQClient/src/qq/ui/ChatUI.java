package qq.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
//import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import bean.Account;
import cmd.Cmd;
import io.ObjectOutputStream;
import send.SendMsg;


public class ChatUI extends JFrame implements ActionListener,ItemListener{

	private Account selfAccount,friendAccount;
	JTextPane recePanel,sendPanel;
	JButton btnSend,btnshake,btnsendFile,btnColor,btnClose;
	JComboBox<?> cbFont,cbSize,cbImg;
	JLabel lblboy,lblgirl,lblfriendInfo;
	ObjectOutputStream oot;
	
	public ChatUI()
	{
		
	}
	public ChatUI(Account self,Account friend,ObjectOutputStream oot)
	{
		this.selfAccount=self;
		this.friendAccount=friend;
		this.oot=oot;
		ImageIcon ic=new ImageIcon(friend.getFace());
	
		setIconImage(ic.getImage());
		String str="";
		if(friend.getRemark()!=null && friend.getRemark().equals(""))
		{
			str=friend.getRemark()+"(";
			
		}
		else
		{
			str=friend.getNickName()+"(";
		
		}
		str+= friend.getQqCode()+")"+friend.getSelfsign();
		
		setTitle(str);
		lblfriendInfo=new JLabel(str,ic,JLabel.LEFT);

		add(lblfriendInfo,BorderLayout.NORTH);
		//中间放接收框
		recePanel=new JTextPane();
		recePanel.setEditable(false);
		
		//下边放一排按钮
		btnSend=new JButton("发送消息");
		btnshake=new JButton(new ImageIcon("images/dd.jpg"));
		btnshake.setMargin(new Insets(0,0,0,0));//调整按钮为图片大小
		btnsendFile=new JButton("发送文件");
		btnColor=new JButton("字体颜色");
		
		String fonts[]= {"宋体","楷体","黑体","隶书","微软雅黑"};
		cbFont=new JComboBox<String>(fonts);
		String fontsize[] = {"10","12","14","16","18"};
		cbSize=new JComboBox<String>(fontsize);
		cbImg=new JComboBox<Icon>(getImg());
		cbImg.setPreferredSize(new Dimension(100, 28));
				
		JPanel btnPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
		btnPanel.add(cbFont);
		btnPanel.add(cbSize);
		btnPanel.add(btnColor);
		btnPanel.add(cbImg);
		btnPanel.add(btnshake);
		btnPanel.add(btnsendFile);
		
		sendPanel=new JTextPane();
		JPanel southPanel=new JPanel(new BorderLayout());
		southPanel.add(btnPanel,BorderLayout.NORTH);
		southPanel.add(new JScrollPane(sendPanel));

		JPanel centerPanel=new JPanel(new GridLayout(2,1,10,10));
		
		centerPanel.add(new JScrollPane(recePanel));
		centerPanel.add(new JScrollPane(southPanel));
		add(centerPanel);
	
		btnSend=new JButton("发送(S)");
		btnSend.setMnemonic('S');
		
		btnClose=new JButton("关闭(X)");
		btnClose.setMnemonic('X');
		
		JPanel bottomPanel=new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		bottomPanel.add(btnSend);
		bottomPanel.add(btnClose);
		
		add(bottomPanel,BorderLayout.SOUTH);
		
		lblboy=new JLabel(new ImageIcon("images/boy.png"));
		lblgirl=new JLabel(new ImageIcon("images/girl.jpg"));
		
		JPanel rightPanel=new JPanel(new GridLayout(2,1,5,0));
		
		rightPanel.add(lblboy);
		rightPanel.add(lblgirl);
		add(rightPanel,BorderLayout.EAST);
		
		btnSend.addActionListener(this);
		btnshake.addActionListener(this);
		btnsendFile.addActionListener(this);
		btnColor.addActionListener(this);
		btnClose.addActionListener(this);
		
		cbFont.addItemListener(this);
		cbSize.addItemListener(this);
		cbImg.addItemListener(this);
		
		
		
		setSize(670,550);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		sendPanel.requestFocus(); 
		
		
	}
	public void send(SendMsg msg) throws IOException {
		oot.writeObject(msg);
	}
	private Icon[] getImg() {//获取表情
		Icon[] icon=null;	
		File file=new File("bq");
		String sfile[]=file.list();		
		icon=new ImageIcon[sfile.length];
		for(int i=0;i<sfile.length;i++)
		{
			icon[i]=new ImageIcon("bq/"+sfile[i]);
		}	
		return icon;
		
	}
	
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource()==cbFont || e.getSource()==cbSize)
		{		
			String sfont=cbFont.getSelectedItem().toString();
			int size=Integer.parseInt(cbSize.getSelectedItem().toString());			
			sendPanel.setFont(new Font(sfont,Font.PLAIN,size));		
		}else if(e.getSource()==cbImg)
		{
			Icon g=(Icon)cbImg.getSelectedItem();
			sendPanel.insertIcon(g);			
		}
		
		
	}
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==btnSend)
		{
			try {
				appendView(selfAccount.getNickName(),sendPanel.getStyledDocument());
				//生成发送类
				SendMsg msg=new SendMsg();
				msg.Cmd=Cmd.CMD_CHAT;
				msg.doc=sendPanel.getStyledDocument();//聊天内容 
				msg.selfAccount=selfAccount;
				msg.friendAccount=friendAccount;
				//发送
				send(msg);
				sendPanel.setText("");		
			} catch (BadLocationException | IOException e1) {
				e1.printStackTrace();
			}
//			sendPanel.setText("");
		
		}else if(e.getSource()==btnshake)
		{
			SendMsg msg=new SendMsg();
			msg.Cmd=Cmd.CMD_DOUDONG;
			msg.selfAccount=selfAccount;
			msg.friendAccount=friendAccount;	
			try {
				send(msg);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			shake();
			
		}
		else if(e.getSource()==btnsendFile)
		{//发送文件
			//创建文件选择器
			FileDialog fdlg=new FileDialog(this,"打开5M以下的文件",FileDialog.LOAD);	
			fdlg.setVisible(true);
			String sfilename=fdlg.getDirectory()+"\\"+fdlg.getFile();			
			File file=new File(sfilename);//创建文件	
			
			if(file.length()>1024*1024*5)
			{
				JOptionPane.showMessageDialog(this, "文件不能大于5M");
				return;				
			}			
			try {			
			byte[] b=new byte[(int)file.length()];
			FileInputStream fis=new FileInputStream(file);
			fis.read(b);//读取文件内容
			fis.close();			
			SendMsg msg=new SendMsg();
			msg.Cmd=Cmd.CMD_FILESEND;
			msg.selfAccount=selfAccount;
			msg.friendAccount=friendAccount;
			msg.sFileName=fdlg.getFile();//文件名
			msg.b=b;
			send(msg);			
			}catch(Exception e1)
			{
				e1.printStackTrace();
			}		
		}
		//选择颜色
		else if(e.getSource()==btnColor)
		{
//			JColorChooser cc=new JColorChooser();
			Color c=JColorChooser.showDialog(this, "选择字体颜色", Color.BLACK);
			sendPanel.setForeground(c);
		}
		else if(e.getSource()==btnClose)
		{
			dispose();
		}
	}
	//窗口抖动
	public void shake()
	{

		Point p=this.getLocationOnScreen();
		for(int i=0;i<20;i++)
		{
			if(i%2==0)
			{
				setLocation(p.x+5,p.y+5);
			}else
			{
				setLocation(p.x-5,p.y-5);
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e1) {
				
				e1.printStackTrace();
			}
		}	
	}
	
	//获取接受框内容
	public void appendView(String name, StyledDocument xx) throws BadLocationException{
		StyledDocument vdoc=recePanel.getStyledDocument();		
		Date date=new Date();		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");		
		String time=sdf.format(date);
		SimpleAttributeSet as=new SimpleAttributeSet();		
		String s=name+"    "+time+"\n";		
		vdoc.insertString(vdoc.getLength(),s,as);
		int end=0;
		while(end<xx.getLength())
		{
			Element e0=xx.getCharacterElement(end);
			
			SimpleAttributeSet asl=new SimpleAttributeSet();
			StyleConstants.setForeground(asl,StyleConstants.getForeground(e0.getAttributes()));
			StyleConstants.setFontSize(asl,StyleConstants.getFontSize(e0.getAttributes()));
			StyleConstants.setFontFamily(asl,StyleConstants.getFontFamily(e0.getAttributes()));
			
			s=e0.getDocument().getText(end, e0.getEndOffset()-end);
			if("icon".equals(e0.getName()))
			{
				vdoc.insertString(vdoc.getLength(),s,e0.getAttributes());
			}
			else
			{
				vdoc.insertString(vdoc.getLength(),s,asl);
			}
			
			end=e0.getEndOffset();

		}
		
		vdoc.insertString(vdoc.getLength(), "\n", as);

	}
	
	
}
