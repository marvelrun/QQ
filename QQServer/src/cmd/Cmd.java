package cmd;

//系统中要用到的命令字，常量
public class Cmd {
	
	public static final String serverIp="localhost";//服务器ip
	public static final int LoginPort=8765;//端口
	public static final int SignPort=8766;//端口
	public static final int CMD_SIGN=998;//注册
	public static final int CMD_LOGIN=999;//登录
	public static final int CMD_ONLINE=1000;//上线通知
	public static final int CMD_OFFLINE=1001;//下线通知
	public static final int CMD_BUSY=1002;//忙碌
	public static final int CMD_LEAVE=1003;//离开
	public static final int CMD_CHAT=1004;//聊天
	public static final int CMD_DOUDONG=1005;//抖动
	public static final int CMD_ADDFRIEND=1006;//添加好友
	public static final int CMD_DELFRIEND=1007;//删除好友
	public static final int CMD_SENDFILE=1008;//发送文件	
	public static final int CMD_AFREEFRIEND=1009;//发送文件
	public static final int CMD_REJECTFRIEND=1010;//发送文件
	public static final int CMD_FILESUCC=1011;//文件发送成功
	public static final int CMD_FILEFAILED=1012;//拒绝接收文件
	public static final int CMD_FILESEND=1013;//文件发送	
	public static final int MFRIEND= 1014;//移动到好友
	public static final int MFAMILY= 1015;//移动到家人
	public static final int MCLASSMATE = 1016;//移动到同学
	public static final int MHMD = 1017;//移动到黑名单
	
	public static final int getAllInfo = 1018;
	public static final int addFriend=1019;
	public static final int delFriend=1020;
	public static final int moveFriend=1021;
	public static final int findByQQcode=1022;
//	public static final int modifyStatus=1023;
	public static final int getFace=1024;
	public static final int find=1025;
	
	public static final String F_FRIEND="好友";
	public static final String F_FAMILY="家人";
	public static final String F_CLASSMATE="同学";
	public static final String F_HMD="黑名单";
	
	
	
}
