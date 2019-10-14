import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

public class Bus_logic {
    //将表现层得到的查询内容转换成查询语句
	public static String transfer_sql(String str) {
		String sql;
		sql="select" +"[日期],"+"["+str+"]"+ "from dbo.[实验数据：上证某公司股市数据]";
		return sql;
	}
	//将resultSet结果集转换成XYDataset
	public static XYDataset createDataset(ResultSet res,int [] a) {
		XYDataset dataset;
		//先转换成list
		List <String>list1=new ArrayList<String>();
		List<String>list2=new ArrayList<String>() ;
		   //显示表格内容
     	try {
			while(res.next())
			 {
			  list1.add(res.getString(1));
			  list2.add(res.getString(2));
			  }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     	//做转换的中间件
     	Day day;
     	double data;
		//转化成series
		 TimeSeries series = new TimeSeries( "timeseries" );
		 for(int i=0;i<list1.size();i++) {
			 if(list2.get(i).equals("N/A")) {
			   data=0;
			 }
			 else
				 data=Double.parseDouble(list2.get(i));
			
			 
			 int year=Integer.parseInt((list1.get(i).substring(0, 4)));
			 int month=Integer.parseInt((list1.get(i).substring(5, 7)));
			 int thatday=Integer.parseInt((list1.get(i).substring(8, 10)));
//			 System.out.println(year);
//			 System.out.println(month);
//			 System.out.println(thatday);
			 //判断是否在此区间
			 if(year>=a[0]&&year<=a[3]&&month>=a[1]&&month<=a[4]&&thatday>=a[2]&&thatday<=a[5]&&data!=0) {
			 day=new Day(thatday,month,year);
			 series.add(day,data);
			 }
		 }
		 dataset= new TimeSeriesCollection(series);

		return dataset;
	}
	public static void main(String []args)throws IOException{
		//设置socket监视客户端
		ServerSocket ss=new ServerSocket(8888);
		System.out.println("服务器已打开");
		while(true) {
			Socket s=ss.accept();
			System.out.println("客户端"+s.getRemoteSocketAddress()+"连接了服务器");
			InputStream is=s.getInputStream();
			byte[]bs=new byte[1024];
			int length=-1;
			String str="";
			while((length=is.read(bs,0,bs.length))!=-1) {
				str+=new String (bs,0,length);
			}
			System.out.println("客户端:"+str);
			String str2=Bus_logic.transfer_sql(str);
			OutputStream os=s.getOutputStream();
			PrintWriter pw=new PrintWriter(os);
			pw.print("执行"+str2);
			pw.flush();
			
			os.close();
			is.close();
			s.close();
		}
		
	}	
}

