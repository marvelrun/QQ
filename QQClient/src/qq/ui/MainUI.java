package qq.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;
import java.util.Vector;
import javax.swing.*;

import bean.Account;
import cmd.Cmd;
import io.ObjectInputStream;
import io.ObjectOutputStream;
import send.SendMsg;

//�����߳� ����ѭ���ȵ�������Ϣ
public class MainUI extends JFrame implements MouseListener, ActionListener, java.io.Serializable {
	ObjectOutputStream oot;
	ObjectInputStream ois;
//	Vector vdata;
	FindUI fui;

	private Account account, friendAccount;
	private JTabbedPane tab;
	private JLabel lblhead;// ��½����ʾ�ĸ�������
	private JList<Account> lstFriend;
	private JList<Account> lstFamily;
	private JList<Account> lstclassmate;
	private JList<Account> lsthmd;
	private JButton btnFind;// ���Һ��Ѱ�ť
	private Vector<Account> vFriend, vFamily, vClassmate, vHmd, vAllDetail;
	private JPopupMenu pop;// �����˵�
//	private JMenu menu;
	private JMenuItem miChat, miLookInfo, miFriend, miFamily, miMate, miHmd, miDel;// ���� �鿴���� ɾ������ �ƶ�����
	// ������ϣ�������������û��Ĵ���
	private LinkedHashMap<Integer, ChatUI> ht_ChatUsers;

	public MainUI() {

	}

	public MainUI(Account acc, ObjectOutputStream oot, ObjectInputStream ois) {
		this.account = acc;
		this.oot = oot;
		this.ois = ois;
		setTitle(acc.getNickName());// ���ô��ڱ�ǩ�������ͷ��
		setIconImage(new ImageIcon(acc.getFace()).getImage());
		// ��ȡ�ǳ� ��ע QQ�� ����ǩ��
		String str = "";
		// ��ע��Ϊ��ʱ����ʾ��ע����ʾ�ǳ�
		if (acc.getRemark() != null || !acc.getRemark().equals("")) {
			str = acc.getRemark() + "(";
		}
		// ��עΪ��ʱ����ʾ�ǳ�
		else {
			str = acc.getNickName() + "(";
		}
		str += acc.getQqCode() + ")" + acc.getSelfsign();
		lblhead = new JLabel(str, new ImageIcon(acc.getFace()), JLabel.LEFT);
		add(lblhead, BorderLayout.NORTH);

		vFriend = new Vector<Account>();
		vFamily = new Vector<Account>();
		vClassmate = new Vector<Account>();
		vHmd = new Vector<Account>();
		vAllDetail = new Vector<Account>();

		lstFamily = new JList<Account>();
		lstFriend = new JList<Account>();
		lstclassmate = new JList<Account>();
		lsthmd = new JList<Account>();

		lstFriend.addMouseListener(this);
		lstFamily.addMouseListener(this);
		lstclassmate.addMouseListener(this);
		lsthmd.addMouseListener(this);
		refresh();// �������б�
		tab = new JTabbedPane();
		tab.add("����", new JScrollPane(lstFriend));
		tab.add("����", new JScrollPane(lstFamily));
		tab.add("ͬѧ", new JScrollPane(lstclassmate));
		tab.add("������", new JScrollPane(lsthmd));
		add(tab);
		createMenu();
		btnFind = new JButton("���Һ���");
		add(btnFind, BorderLayout.SOUTH);

		btnFind.addActionListener(this);
		setSize(300, 680);
		setVisible(true);
		setResizable(true);
		// �õ���Ļ���,���ô���λ��
		int width = Toolkit.getDefaultToolkit().getScreenSize().width - 300;
		setLocation(width, 50);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// ������Ϣ�����߳�
		new ReceThread().start();
	}

	class listmodel extends AbstractListModel<Account> {
		Vector<Account> dats;

		public listmodel(Vector<Account> dats) {
			this.dats = dats;
		}

		// ��ȡ����
		public Account getElementAt(int index) {
			Account user = (Account) dats.get(index);
			return user;
		}

		// ��ȡ����
		public int getSize() {
			return dats.size();
		}

	}

	// ��ȡ����ͷ��
	class myfind extends DefaultListCellRenderer {
		Vector<Account> datas;

		public myfind(Vector<Account> datas) {
			this.datas = datas;
		}

		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			if (index >= 0 && index < datas.size()) {

				Account user = (Account) datas.get(index);
				// �����б��еĺ�������״̬����ͷ��
				if (user.getStatus() == 1) {
					setIcon(new ImageIcon(user.getFace()));
				} else {
					setIcon(new ImageIcon("face/bzx.jpg"));//���ò�����

				}
				setText(user.getNickName().trim() + "(" + user.getQqCode() + ")");
			}

			// ����������ɫ
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());

			}

			setEnabled(list.isEnabled());
			setFont(list.getFont());
			setOpaque(true);
			return c;

		}

	}

	// �����˵�
	public void createMenu() {
		pop = new JPopupMenu();
		miChat = new JMenuItem("����");
		miLookInfo = new JMenuItem("�鿴����");
		miFriend = new JMenuItem("�ƶ�������");
		miFamily = new JMenuItem("�ƶ�������");
		miMate = new JMenuItem("�ƶ���ͬѧ");
		miHmd = new JMenuItem("�ƶ���������");
		miDel = new JMenuItem("ɾ������");

		miChat.addActionListener(this);
		miLookInfo.addActionListener(this);
		miFriend.addActionListener(this);
		miFamily.addActionListener(this);
		miMate.addActionListener(this);
		miHmd.addActionListener(this);
		miDel.addActionListener(this);

		pop.add(miChat);
		pop.add(miLookInfo);
		pop.add(miFriend);
		pop.add(miFamily);
		pop.add(miMate);
		pop.add(miHmd);
		pop.add(miDel);

	}
	// ˢ�½���
	public void refresh() {
		SendMsg msg = new SendMsg();
		msg.Cmd = Cmd.getAllInfo;
		msg.selfAccount = account;
//		try {
			oot.writeObject(msg);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == lstFriend) {
			friendAccount = (Account) vFriend.get(lstFriend.getSelectedIndex());
			// ˫��
			if (e.getClickCount() == 2) {
				findWin(friendAccount.getQqCode(), null);

			}
			// �ʼ�
			if (e.getButton() == 3) {
				// ���ѱ�ѡ��
				if (lstFriend.getSelectedIndex() >= 0) {
					pop.show(lstFriend, e.getX(), e.getY());
				}

			}
		} else if (e.getSource() == lstFamily) {
			friendAccount = (Account) vFamily.get(lstFamily.getSelectedIndex());
			// ˫��
			if (e.getClickCount() == 2) {

				findWin(friendAccount.getQqCode(), null);
			}
			// �ʼ�
			if (e.getButton() == 3) {

				// ��ѡ��
				if (lstFamily.getSelectedIndex() >= 0) {
					pop.show(lstFamily, e.getX(), e.getY());
				}

			}
		} else if (e.getSource() == lstclassmate) {
			friendAccount = (Account) vClassmate.get(lstclassmate.getSelectedIndex());
			// ˫��
			if (e.getClickCount() == 2) {

				findWin(friendAccount.getQqCode(), null);
			}
			// �ʼ�
			if (e.getButton() == 3) {

				// ��ѡ��
				if (lstclassmate.getSelectedIndex() >= 0) {
					pop.show(lstclassmate, e.getX(), e.getY());
				}

			}
		} else if (e.getSource() == lsthmd) {
			friendAccount = (Account) vHmd.get(lsthmd.getSelectedIndex());
			// ˫��
			if (e.getClickCount() == 2) {
				findWin(friendAccount.getQqCode(), null);
			}
			// �һ�
			if (e.getButton() == 3) {

				// ��ѡ��
				if (lsthmd.getSelectedIndex() >= 0) {
					pop.show(lsthmd, e.getX(), e.getY());
				}

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

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnFind) {
			 fui=new FindUI(this,account, ois, oot);
		} else if (e.getSource() == miChat) {
			// new ChatUI(account,friendAccount);
			findWin(friendAccount.getQqCode(), null);
		} else if (e.getSource() == miLookInfo) {

			if (friendAccount != null)
				new LookUsers(friendAccount);
		} else if (e.getSource() == miDel) {
			SendMsg msg = new SendMsg();
			msg.Cmd = Cmd.delFriend;
			msg.selfAccount = account;
			msg.friendAccount = friendAccount;
//			try {
				oot.writeObject(msg);
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
			refresh();
		} else if (e.getSource() == miFriend) {
			SendMsg msg = new SendMsg();
			msg.Cmd = Cmd.MFRIEND;
			msg.selfAccount = account;
			msg.friendAccount = friendAccount;
//			try {
				oot.writeObject(msg);
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
			refresh();
		} else if (e.getSource() == miFamily) {
			SendMsg msg = new SendMsg();
			msg.Cmd = Cmd.MFAMILY;
			msg.selfAccount = account;
			msg.friendAccount = friendAccount;
//			try {
				oot.writeObject(msg);
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
			refresh();
		} else if (e.getSource() == miMate) {
			SendMsg msg = new SendMsg();
			msg.Cmd = Cmd.MCLASSMATE;
			msg.selfAccount = account;
			msg.friendAccount = friendAccount;
//			try {
				oot.writeObject(msg);
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
			// new DBOper().moveFriend(account, friendAccount.getQqCode(),Cmd.F_CLASSMATE);
			refresh();
		} else if (e.getSource() == miHmd) {

		}

	}

	// ������Ϣ���߳�
	class ReceThread extends Thread {

		public ReceThread() {
			// �����߳�ʱ �������洰�ڵĹ�ϣ��
			ht_ChatUsers = new LinkedHashMap<Integer, ChatUI>();
		}

		public void run() {

			try {
				while (true) {
					SendMsg msg = (SendMsg) ois.readObject();
					switch (msg.Cmd) {
					case Cmd.getAllInfo:// ��ȡ����
						vAllDetail = msg.vector;
						// ���֮ǰ���еļ�¼
						vFriend.clear();
						vFamily.clear();
						vClassmate.clear();
						vHmd.clear();
						// �����ݿ���������ݷֱ�ŵ���Ӧ������
						for (int i = 0; i < vAllDetail.size(); i++) {
							Account a = vAllDetail.get(i);
							if (a.getGroupname().equals(Cmd.F_FRIEND)) {
								vFriend.add(a);
							} else if (a.getGroupname().equals(Cmd.F_FAMILY)) {
								vFamily.add(a);
							} else if (a.getGroupname().equals(Cmd.F_CLASSMATE)) {
								vClassmate.add(a);
							} else if (a.getGroupname().equals(Cmd.F_HMD)) {
								vHmd.add(a);
							}

						}
						// ����������List�ؼ�
						lstFriend.setModel(new listmodel(vFriend));// ��ʾ����
						lstFriend.setCellRenderer(new myfind(vFriend));// ��ʾͷ��
						lstFamily.setModel(new listmodel(vFamily));// ��ʾ����
						lstFamily.setCellRenderer(new myfind(vFamily));// ��ʾͷ��
						lstclassmate.setModel(new listmodel(vClassmate));// ��ʾ����
						lstclassmate.setCellRenderer(new myfind(vClassmate));// ��ʾͷ��
						lsthmd.setModel(new listmodel(vHmd));// ��ʾ����
						lsthmd.setCellRenderer(new myfind(vHmd));// ��ʾͷ��
						break;
					case Cmd.find://����	
					fui.setVdata(msg.vector);
					fui.mytable.datas=msg.vector;
					fui.getDataTable().updateUI();
						break;
					case Cmd.findByQQcode:
						SendMsg Amsg=new SendMsg();					
						Amsg.Cmd=Cmd.CMD_ADDFRIEND;	
						Amsg.selfAccount=account;
						Amsg.friendAccount=msg.friendAccount;	
						System.out.println(msg.friendAccount.getQqCode());
						oot.writeObject(Amsg);
						oot.flush();
						System.out.println("׼������");
						break;
					case Cmd.CMD_ONLINE:// ��������֪ͨ
						refresh();
						break;
					case Cmd.CMD_OFFLINE:// ��������֪ͨ
						refresh();
						break;
					case Cmd.CMD_CHAT:// ����������Ϣ
						ChatUI chat = findWin(msg.selfAccount.getQqCode(), msg);// ��ʾ����
						chat.appendView(msg.selfAccount.getNickName(), msg.doc);
						break;
					case Cmd.CMD_DOUDONG:// ���ն�����Ϣ
						chat = findWin(msg.selfAccount.getQqCode(), msg);// ��ʾ����
						chat.shake();
						break;

					case Cmd.CMD_ADDFRIEND:// ��Ӻ���
						String str = msg.selfAccount.getNickName() + "�����Ϊ���� ����ȷ��";
						// ������ȷ����ť�����
						SendMsg m = new SendMsg();
						m.friendAccount = msg.friendAccount;
						m.selfAccount = account;
						if (JOptionPane.showConfirmDialog(null, str, "��Ӻ���",
								JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {// �����ѱ�����Ӽ�¼							
							m.Cmd = Cmd.CMD_AFREEFRIEND;
							oot.writeObject(m);
							oot.flush();
							SendMsg amsg = new SendMsg();
							amsg.Cmd = Cmd.addFriend;
							amsg.selfAccount = account;
							amsg.friendAccount = msg.selfAccount;
							oot.writeObject(amsg);
							oot.flush();
							refresh();
							System.out.println("��Ӻ��ѳɹ�");
						} else {
							m.Cmd = Cmd.CMD_REJECTFRIEND;
							oot.writeObject(m);
							oot.flush();
						}
					
						break;

					case Cmd.CMD_AFREEFRIEND:// ͬ��
						JOptionPane.showMessageDialog(null, msg.selfAccount.getNickName() + "��������ĺ�������");
						refresh();// ˢ�½���
						break;

					case Cmd.CMD_REJECTFRIEND:// �ܾ�
						JOptionPane.showMessageDialog(null, msg.selfAccount.getNickName() + "�ܽ�����ĺ�������");
						break;

					case Cmd.CMD_FILESEND:// �����ļ�
						String str1 = msg.selfAccount.getNickName() + "�������ļ�" + msg.sFileName;

						int cmd = Cmd.CMD_FILESUCC;
						if (JOptionPane.showConfirmDialog(null, str1, "�����ļ�",
								JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
							FileDialog dlg = new FileDialog(MainUI.this, "����", FileDialog.SAVE);
							dlg.setFile(msg.sFileName);
							dlg.setVisible(true);
							String sfilename = dlg.getDirectory() + "\\" + dlg.getFile();
							File file = new File(sfilename);
							if (!file.exists()) {
								file.createNewFile();
							}
							FileOutputStream fos = new FileOutputStream(sfilename);
							fos.write(msg.b);
							fos.close();
						} else {
							cmd = Cmd.CMD_FILEFAILED;
						}
						SendMsg msg1 = new SendMsg();
						msg1.selfAccount = account;
						msg1.friendAccount = msg.friendAccount;
						msg1.Cmd = cmd;
						oot.writeObject(msg1);
						break;

					case Cmd.CMD_FILESUCC:// �ظ����ܳɹ�
						JOptionPane.showMessageDialog(null, msg.selfAccount.getNickName() + "�������ļ�");
						break;

					case Cmd.CMD_FILEFAILED:// �ظ��ܽ�

						JOptionPane.showMessageDialog(null, msg.selfAccount.getNickName() + "�ܾ����ļ�");
						break;
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	// ���Ҵ����Ƿ����,����������򴴽�,������ֱ����ʾ��Ϣ�ڽ���
	public ChatUI findWin(Integer qqcode, SendMsg msg) {
		ChatUI chat = null;
		// ���Ҵ����Ƿ����
		chat = ht_ChatUsers.get(qqcode);
		if (chat == null)// �������򴴽����촰��
		{
			if (msg == null)// ˫�������Ҽ��򿪴���
			{
				chat = new ChatUI(account, friendAccount, oot);
			} else// �̴߳򿪴���
			{
				chat = new ChatUI(msg.friendAccount, msg.selfAccount, oot);
			}
			// ���ڼ����ϣ��
			ht_ChatUsers.put(qqcode, chat);
		}

		if (!chat.isVisible()) {
			chat.setVisible(true);
		}
		return chat;
	}
	// �޸��û�״̬
	public void UpdateStatus(Account acc) {
		int size = vAllDetail.size();
		for (int i = 0; i < size; i++) {
			Account a = vAllDetail.get(i);
			if (a.getQqCode() == acc.getQqCode()) {
				a.setStatus(acc.getStatus());
				vAllDetail.set(i, a);
				break;
			}

		}
		refresh();

	}

	public Object receive() throws IOException, ClassNotFoundException {
		return ois.readObject();
	}
}