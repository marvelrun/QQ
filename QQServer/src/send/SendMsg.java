package send;

import java.util.Vector;
import javax.swing.text.StyledDocument;

import bean.Account;

public class SendMsg implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	public int Cmd;//����
	public Account selfAccount,friendAccount;
	public StyledDocument doc;//��Ϣ����ʽ
	public String sFileName;//���͵��ļ���
	public byte[] b;//�ļ�תΪ���ֽ�����
	public Vector<Account> vector;//��ѯ���ϵȷ��ص�����
}
