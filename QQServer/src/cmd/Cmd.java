package cmd;

//ϵͳ��Ҫ�õ��������֣�����
public class Cmd {
	
	public static final String serverIp="localhost";//������ip
	public static final int LoginPort=8765;//�˿�
	public static final int SignPort=8766;//�˿�
	public static final int CMD_SIGN=998;//ע��
	public static final int CMD_LOGIN=999;//��¼
	public static final int CMD_ONLINE=1000;//����֪ͨ
	public static final int CMD_OFFLINE=1001;//����֪ͨ
	public static final int CMD_BUSY=1002;//æµ
	public static final int CMD_LEAVE=1003;//�뿪
	public static final int CMD_CHAT=1004;//����
	public static final int CMD_DOUDONG=1005;//����
	public static final int CMD_ADDFRIEND=1006;//��Ӻ���
	public static final int CMD_DELFRIEND=1007;//ɾ������
	public static final int CMD_SENDFILE=1008;//�����ļ�	
	public static final int CMD_AFREEFRIEND=1009;//�����ļ�
	public static final int CMD_REJECTFRIEND=1010;//�����ļ�
	public static final int CMD_FILESUCC=1011;//�ļ����ͳɹ�
	public static final int CMD_FILEFAILED=1012;//�ܾ������ļ�
	public static final int CMD_FILESEND=1013;//�ļ�����	
	public static final int MFRIEND= 1014;//�ƶ�������
	public static final int MFAMILY= 1015;//�ƶ�������
	public static final int MCLASSMATE = 1016;//�ƶ���ͬѧ
	public static final int MHMD = 1017;//�ƶ���������
	
	public static final int getAllInfo = 1018;
	public static final int addFriend=1019;
	public static final int delFriend=1020;
	public static final int moveFriend=1021;
	public static final int findByQQcode=1022;
//	public static final int modifyStatus=1023;
	public static final int getFace=1024;
	public static final int find=1025;
	
	public static final String F_FRIEND="����";
	public static final String F_FAMILY="����";
	public static final String F_CLASSMATE="ͬѧ";
	public static final String F_HMD="������";
	
	
	
}
