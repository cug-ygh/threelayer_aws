	import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class dbconnection {

	//���ӱ������ݿ�
	static String user="sa";
	static String pwd="015033";
	static String dburl="jdbc:sqlserver://localhost:1433;DatabaseName=stock_market";
	static Connection conn;
	//����aws���ݿ�
//	static String user="admin";
//	static String pwd="wang630306";
//	static String dburl= "jdbc:sqlserver://database-1.c6ugvizdrrn0.us-east-1.rds.amazonaws.com:1433;DatabaseName = S-T";
	//�����ݿ�
	public static void connection() 
	{
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//�����ݿ�����������ص�jvm��
		try {
			conn=DriverManager.getConnection(dburl,user,pwd);
			//System.out.println("Successfully connection.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block1
			e.printStackTrace();
		}
	}
	//�ر����ݿ�
	public static void disconnection() {
	
	}
	//ִ�в�ѯ���ؽ����
	public static ResultSet select_return(String  sqlParameter) {
		ResultSet rs = null;
		try {
			connection();
			PreparedStatement stmt = conn.prepareStatement(sqlParameter);
			
			rs = stmt.executeQuery();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error - Connection.executeSelectQuery - Query: " + " \nException: " + e.toString());
			return null;
		}
		return rs;
	}
	
	

	@SuppressWarnings("null")
	static public void main(String[]args) throws SQLException {
		String str="select [����]  from dbo.[ʵ�����ݣ���֤ĳ��˾��������]";
		ResultSet res=select_return(str);
		List <String>list=new ArrayList<String>();
		List <String>list1=new ArrayList<String>();
		List <String>list2=new ArrayList<String>();
		List <String>list3=new ArrayList<String>();
		   //��ʾ�������
     	while(res.next())
		 {
		  list.add(res.getString(1));
		  }
		   
		for(int i=0;i<list.size();i++) {
		String year=list.get(i).substring(0, 4);
	    list1.add(year);
		
		String month=list.get(i).substring(5, 7);
		list2.add(month);
		
	
		String thatday=list.get(i).substring(8, 10);
		list3.add(thatday);
		}
		}
	
}
