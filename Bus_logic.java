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
    //�����ֲ�õ��Ĳ�ѯ����ת���ɲ�ѯ���
	public static String transfer_sql(String str) {
		String sql;
		sql="select" +"[����],"+"["+str+"]"+ "from dbo.[ʵ�����ݣ���֤ĳ��˾��������]";
		return sql;
	}
	//��resultSet�����ת����XYDataset
	public static XYDataset createDataset(ResultSet res,int [] a) {
		XYDataset dataset;
		//��ת����list
		List <String>list1=new ArrayList<String>();
		List<String>list2=new ArrayList<String>() ;
		   //��ʾ�������
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
     	//��ת�����м��
     	Day day;
     	double data;
		//ת����series
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
			 //�ж��Ƿ��ڴ�����
			 if(year>=a[0]&&year<=a[3]&&month>=a[1]&&month<=a[4]&&thatday>=a[2]&&thatday<=a[5]&&data!=0) {
			 day=new Day(thatday,month,year);
			 series.add(day,data);
			 }
		 }
		 dataset= new TimeSeriesCollection(series);

		return dataset;
	}
	public static void main(String []args)throws IOException{
		//����socket���ӿͻ���
		ServerSocket ss=new ServerSocket(8888);
		System.out.println("�������Ѵ�");
		while(true) {
			Socket s=ss.accept();
			System.out.println("�ͻ���"+s.getRemoteSocketAddress()+"�����˷�����");
			InputStream is=s.getInputStream();
			byte[]bs=new byte[1024];
			int length=-1;
			String str="";
			while((length=is.read(bs,0,bs.length))!=-1) {
				str+=new String (bs,0,length);
			}
			System.out.println("�ͻ���:"+str);
			String str2=Bus_logic.transfer_sql(str);
			OutputStream os=s.getOutputStream();
			PrintWriter pw=new PrintWriter(os);
			pw.print("ִ��"+str2);
			pw.flush();
			
			os.close();
			is.close();
			s.close();
		}
		
	}	
}

