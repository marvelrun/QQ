package send;

import java.util.Vector;
import javax.swing.text.StyledDocument;

import bean.Account;

public class SendMsg implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	public int Cmd;//命令
	public Account selfAccount,friendAccount;
	public StyledDocument doc;//消息的样式
	public String sFileName;//发送的文件名
	public byte[] b;//文件转为的字节数组
	public Vector<Account> vector;//查询资料等返回的向量
}
