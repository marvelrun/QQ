package qq.ui;

import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.io.ObjectInputStream;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import bean.Account;
import cmd.Cmd;
import io.ObjectInputStream;
import io.ObjectOutputStream;
import send.SendMsg;

/*
 * 查找好友
 */
public class FindUI extends JFrame implements ActionListener {

	private JTextField txtQQcode, txtNickname, txtage;
	private JComboBox<String> cbSex;
	private JTable dataTable;

	public JTable getDataTable() {
		return dataTable;
	}

	private JButton btnFind, btnClose, btnAdd;
	private Vector<String> vhead;
	private Vector<Account> vdata;

	public void setVdata(Vector<Account> vdata) {
		this.vdata = vdata;
	}
	myTable mytable = null;
	ObjectInputStream ois;
	ObjectOutputStream oot;
	MainUI mainUI;

	public FindUI(MainUI mainUI, Account account, ObjectInputStream ois, ObjectOutputStream oot) {
		super("查找好友");
		this.mainUI = mainUI;
		this.ois = ois;
		this.oot = oot;
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lblqqcode = new JLabel("QQ号码: ");
		JLabel lblnickname = new JLabel("昵称: ");
		JLabel lblage = new JLabel("年龄: ");
		JLabel lblsex = new JLabel("性别: ");

		txtQQcode = new JTextField(10);
		txtNickname = new JTextField(10);
		txtage = new JTextField(5);

		cbSex = new JComboBox<String>(new String[] { "男", "女" });
		btnFind = new JButton("查找");

		topPanel.add(lblqqcode);
		topPanel.add(txtQQcode);

		topPanel.add(lblnickname);
		topPanel.add(txtNickname);

		topPanel.add(lblage);
		topPanel.add(txtage);

		topPanel.add(lblsex);
		topPanel.add(cbSex);
		topPanel.add(btnFind);
		btnFind.addActionListener(this);
		add(topPanel, BorderLayout.NORTH);
		vhead = new Vector<String>();
		vhead.add("QQ号码");
		vhead.add("昵称");
		vhead.add("年龄");
		vhead.add("性别");
		vhead.add("民族");
		vhead.add("星座");
		vhead.add("头像");
		vhead.add("备注");
		vhead.add("个性签名");
		vhead.add("状态");

		vdata = new Vector<Account>();

		mytable = new myTable(vdata, vhead);

		dataTable = new JTable(mytable);
		add(new JScrollPane(dataTable));

		btnClose = new JButton("关闭");
		btnAdd = new JButton("添加");

		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		bottomPanel.add(btnAdd);
		bottomPanel.add(btnClose);
		add(bottomPanel, BorderLayout.SOUTH);

		btnAdd.addActionListener(this);
		btnClose.addActionListener(this);

		setSize(700, 400);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public void find(Account acc) {
		SendMsg fmsg = new SendMsg();
		fmsg.Cmd = Cmd.find;
		fmsg.friendAccount = acc;
//		try {
			oot.writeObject(fmsg);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	// 设置JTable控件
	class myTable extends AbstractTableModel {
		Vector <Account>datas = new Vector<Account>();
		Vector<String> columns = new Vector<String>();

		// 构造函数必须把表头和数据传进来
		public myTable(Vector<Account> datas, Vector<String> columns) {
			this.datas = datas;
			this.columns = columns;
		}

		// 获取列数
		public int getColumnCount() {

			return columns.size();
		}

		// 获取列名
		public String getColumnName(int columnIndex) {

			return columns.get(columnIndex).toString();
		}

		// 获取行数
		public int getRowCount() {
			return datas.size();
		}

		// 每一列要显示的数据类
		public Object getValueAt(int rowIndex, int columnIndex) {
			Account v = datas.get(rowIndex);// 取出第rowindex个account对象
			if (columnIndex == 0)
				return v.getQqCode();
			if (columnIndex == 1)
				return v.getNickName();
			if (columnIndex == 2)
				return v.getAge();
			if (columnIndex ==3)
				return v.getSex();
			if (columnIndex ==4)
				return v.getNation();
			if (columnIndex == 5)
				return v.getStar();
			if (columnIndex == 6)// 头像列返回图片
				return new ImageIcon(v.getFace().toString());
			if (columnIndex == 7)
				return v.getRemark();
			if (columnIndex == 8)
					return v.getSelfsign();
			else 
						return v.getStatus();
		}
	}
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btnFind) {
			Account acc = new Account();
			if (!txtQQcode.getText().equals("")) {
				acc.setQqCode(Integer.parseInt(txtQQcode.getText().trim()));
			}

			if (!txtNickname.getText().equals("")) {
				acc.setNickName(txtNickname.getText().trim());
			}

			if (!txtage.getText().equals("")) {
				acc.setAge(Integer.parseInt(txtage.getText().trim()));
			}

			acc.setSex(cbSex.getSelectedItem().toString());

			find(acc);

		} else if (e.getSource() == btnAdd) {
			int row;
			row = dataTable.getSelectedRow();// 得到选择的行
			// 获取第一列的QQcode
			int qqcode = vdata.get(row).getQqCode();
			SendMsg fmsg = new SendMsg();
			fmsg.Cmd = Cmd.findByQQcode;
			Account facc = new Account();
			facc.setQqCode(qqcode);
			fmsg.friendAccount = facc;
				oot.writeObject(fmsg);
				System.out.println("向服务器查询");
		}

		else if (e.getSource() == btnClose) {
			dispose();
		}
	}

}
