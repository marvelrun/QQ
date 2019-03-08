
package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import bean.Account;
import cmd.Cmd;

/*
 * 对数据库进行操作
 */
public class DBOper {

	// 注册验证
	public boolean sign(Account acc)
	{
		boolean bok=false;
		Connection conn=new DBConn().dbConn();
		
		try {
			
			String sql="insert into account values(?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement pstmt=conn.prepareStatement(sql);
			int i=1;
			pstmt.setInt(i++,acc.getQqCode());
			pstmt.setString(i++,acc.getNickName());
			pstmt.setString(i++,acc.getPwd());
			pstmt.setInt(i++,acc.getAge());
			pstmt.setString(i++,acc.getSex());
			pstmt.setString(i++,acc.getNation());
			pstmt.setString(i++,acc.getStar());
			pstmt.setString(i++,acc.getFace());
			pstmt.setString(i++,acc.getRemark());
			pstmt.setString(i++,acc.getSelfsign());
			pstmt.setInt(i++,acc.getStatus());
			//执行数据库操作
	//		bok=pstmt.execute();
			if(pstmt.executeUpdate()>0)
			{
				bok=true;
			}
			
			pstmt.close();
			conn.close();
		}catch(Exception e)
		{
			e.printStackTrace();
			
		}
		
		return bok;
		
	}


	//登录函数,返回account对象
	public Account login(Account acc)
	{				
		Connection conn=new DBConn().dbConn();		
		try {
			
			String sql="select * from account where qqCode =? and pwd=?";
			PreparedStatement pstmt=conn.prepareStatement(sql);
			
			pstmt.setInt(1,acc.getQqCode());
			pstmt.setString(2,acc.getPwd());
			
			//ResultSet表示从数据库中查询到的返回结果
			ResultSet rs=pstmt.executeQuery();
			if(rs.next())//查到了账号名跟密码匹配的东西  已经登陆了
			{	//登陆成功，读取用户所有信息
				//修改状态为在线(1)
				acc.setNickName(rs.getString("nickname").trim());
				acc.setAge(rs.getInt("age"));
				acc.setSex(rs.getString("sex").trim());
				acc.setNation(rs.getString("nation").trim());
				acc.setStar(rs.getString("star").trim());
				acc.setFace(rs.getString("face").trim());
				acc.setRemark(rs.getString("remark").trim());
				acc.setSelfsign(rs.getString("selfsign").trim());			
				acc.setStatus(1);//修改对象值为1
				//修改数据库中存储状态为上线
				modifyStatus(acc.getQqCode(),1);
				
			}		
			pstmt.close();
			conn.close();
		}catch(Exception e)
		{
			e.printStackTrace();
			
		}	
		return acc;		
	}
		//查找个人资料
			public Account findByQQcode (int qqcode)
			{					
				Connection conn=new DBConn().dbConn();
				Account acc=new Account();							
				try {			
					String sql="select * from account where qqCode =?";
					PreparedStatement pstmt=conn.prepareStatement(sql);				
					pstmt.setInt(1,qqcode);									
					//ResultSet表示从数据库中查询到的返回结果
					ResultSet rs=pstmt.executeQuery();
					if(rs.next())//查到了账号名跟密码匹配的东西  已经登陆了
					{	//登陆成功，读取用户所有信息
			
						acc.setQqCode(rs.getInt("qqcode"));					
						acc.setNickName(rs.getString("nickname").trim());
						acc.setAge(rs.getInt("age"));
						acc.setSex(rs.getString("sex").trim());
						acc.setNation(rs.getString("nation").trim());
						acc.setStar(rs.getString("star").trim());
						acc.setFace(rs.getString("face").trim());
						acc.setRemark(rs.getString("remark").trim());
						acc.setSelfsign(rs.getString("selfsign").trim());
						
					}				
					pstmt.close();
					conn.close();
				}catch(Exception e)
				{
					e.printStackTrace();				
				}		
				return acc;			
			}
		
		public boolean modifyStatus(int qqcode,int status)
		{
			boolean bok=false;
			Connection conn=new DBConn().dbConn();
			
			try {
				
				String sql="update account set status=? where qqCode=?";
				PreparedStatement pstmt=conn.prepareStatement(sql);
				pstmt.setInt(1,status);
				pstmt.setInt(2,qqcode);
				
			
				if(pstmt.executeUpdate()>0)
				{
					bok=true;
				}
				
				pstmt.close();
				conn.close();
			}catch(Exception e)
			{
				e.printStackTrace();
				
			}
			
			return bok;
			
		}
		
		
		//返回头像
		public String getFace(int qqcode)
		{
			String face="";
			Connection conn=new DBConn().dbConn();
			
			try {
				
				String sql="select face from account where qqCode =?";
				PreparedStatement pstmt=conn.prepareStatement(sql);
				
				pstmt.setInt(1,qqcode);
				
				//ResultSet表示从数据库中查询到的返回结果
				ResultSet rs=pstmt.executeQuery();
				if(rs.next())//查到了账号名跟密码匹配的东西  已经登陆了
				{	
					face=rs.getString("face").trim();
					
					
				}
				
				
				pstmt.close();
				conn.close();
			}catch(Exception e)
			{
				e.printStackTrace();
				
			}
		return face;
			
			
		}
		
		//返回所有好友，家人，同学，黑名单等资料
		public Vector<Account> getAllInfo(Account acc)
		{
					
			Connection conn=new DBConn().dbConn();
			Vector<Account> allInfo=new Vector<Account>();
			try {
				
				//取出朋友的信息，在线状态，以及分组
				String sql="select a.*,f.groupname from account a right outer join friend f on a.qqcode=f.friendaccount where f.selfAccount =?";
				PreparedStatement pstmt=conn.prepareStatement(sql);				
				pstmt.setInt(1,acc.getQqCode());
				
				
				//ResultSet表示从数据库中查询到的返回结果
				ResultSet rs=pstmt.executeQuery();
				while(rs.next())//查到了账号名跟密码匹配的东西  已经登陆了
				{	
					Account a=new Account();//一条记录创建一个Account对象
					a.setQqCode(rs.getInt("qqcode"));
					a.setNickName(rs.getString("nickname").trim());
					a.setPwd(rs.getString("pwd").trim());		
					a.setAge(rs.getInt("age"));
					a.setSex(rs.getString("sex").trim());
					a.setNation(rs.getString("nation").trim());
					a.setStar(rs.getString("star").trim());
					a.setFace(rs.getString("face").trim());
					a.setRemark(rs.getString("remark").trim());
					a.setSelfsign(rs.getString("selfsign").trim());
					a.setStatus(rs.getInt("status"));
					a.setGroupname(rs.getString("groupname").trim());				
					allInfo.add(a);		
				}			
				pstmt.close();
				conn.close();
			}catch(Exception e)
			{
				e.printStackTrace();
				
			}	
			return allInfo;					
		}
		
		//查找好友
			public Vector<Account> find(Account acc)
			{
						System.out.println("向数据库查询");
				Connection conn=new DBConn().dbConn();
				Vector <Account>allInfo=new Vector<Account>();
				try {
					
					//取出朋友的信息，在线状态，以及分组
					
					String sql="select * from account where qqcode=? or nickname =? or age=?";
					
					PreparedStatement pstmt=conn.prepareStatement(sql);
					
					int i=1;
					
					pstmt.setInt(i++,acc.getQqCode());
					pstmt.setString(i++,acc.getNickName());
					pstmt.setInt(i++,acc.getAge());
			//		pstmt.setString(i++,acc.getSex());
					
					//ResultSet表示从数据库中查询到的返回结果
					ResultSet rs=pstmt.executeQuery();
					while(rs.next())//查到了账号名跟密码匹配的东西  已经登陆了
					{	
						Account a=new Account();
						a.setQqCode(rs.getInt("qqcode"));
						a.setNickName(rs.getString("nickname").trim());
						a.setAge(rs.getInt("age"));
						a.setSex(rs.getString("sex").trim());
						a.setNation(rs.getString("nation").trim());
						a.setStar(rs.getString("star").trim());
						a.setFace(rs.getString("face").trim());
						a.setRemark(rs.getString("remark").trim());
						a.setSelfsign(rs.getString("selfsign").trim());
						a.setStatus(rs.getInt("status"));					
						allInfo.add(a);						
					}				
					pstmt.close();
					conn.close();
				}catch(Exception e)
				{
					e.printStackTrace();
					
				}
				
				
				
				return allInfo;
				
				
			}
			//添加好友
			public boolean addFriend(Account acc,int friendcode)
			{		
				boolean bok=false;
				Connection conn=new DBConn().dbConn();
				
				try {
					String sql="insert into friend (selfAccount,friendAccount,groupname,invalid)values(?,?,?,?)";
//					String sql="insert into friend values(?,?,?,?)";
					PreparedStatement pstmt=conn.prepareStatement(sql);
					int i=1;
					pstmt.setInt(i++,acc.getQqCode());
					pstmt.setInt(i++,friendcode);
					pstmt.setString(i++,Cmd.F_FRIEND);
					pstmt.setInt(i++,0);
					pstmt.executeUpdate();
					pstmt.close();
				//互相加好友	
					pstmt=conn.prepareStatement(sql);
					i=1;
					pstmt.setInt(i++,friendcode);
					pstmt.setInt(i++,acc.getQqCode());
					pstmt.setString(i++,Cmd.F_FRIEND);
					pstmt.setInt(i++,0);
					pstmt.executeUpdate();
					pstmt.close();				
					conn.close();
					bok=true;
				}catch(Exception e)
				{
					e.printStackTrace();
					
				}
				
				return bok;
				
			}	
			
			//删除好友
			public boolean delFriend(Account acc,int friendcode)
			{
				
				Connection conn=new DBConn().dbConn();
				
				try {
					
					String sql="delete friend where selfAccount=? and friendAccount=?";
					PreparedStatement pstmt=conn.prepareStatement(sql);
					int i=1;
					pstmt.setInt(i++,acc.getQqCode());
					pstmt.setInt(i++,friendcode);
					pstmt.executeUpdate();
					pstmt.close();
				//互相删除好友	
					pstmt=conn.prepareStatement(sql);
					i=1;
					pstmt.setInt(i++,friendcode);
					pstmt.setInt(i++,acc.getQqCode());
					pstmt.executeUpdate();
					pstmt.close();
					
					
					conn.close();
				}catch(Exception e)
				{
					e.printStackTrace();
					
				}
				
				return true;
				
			}	
			
			//移动好友
					public boolean moveFriend(Account acc,int friendcode,String groupname)
					{
						
						Connection conn=new DBConn().dbConn();
						
						try {
							
							String sql="update friend set groupname=? where selfAccount=? and friendAccount=?";
							PreparedStatement pstmt=conn.prepareStatement(sql);
							int i=1;
							pstmt.setString(i++,groupname);
							pstmt.setInt(i++,acc.getQqCode());
							pstmt.setInt(i++,friendcode);
							pstmt.executeUpdate();
							
							
							pstmt.close();
							conn.close();
						}catch(Exception e)
						{
							e.printStackTrace();
							
						}
						
						return true;
						
					}			
			
			





}