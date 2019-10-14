
import org.jfree.chart.*;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import org.jfree.data.category.*;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;


public class Presentation_layer 
{

	public static final int[] a = new int[6];
	static String []data= {"前收盘价(元)","开盘价(元)","最高价(元)","最低价(元)","最低价(元)","涨跌(元)","均价(元)","总市值(元)","市盈率","市销率","市净率","市现率"};
	static JComboBox  jcb=new JComboBox(data);
	static JComboBox jcb1=new JComboBox();
	static JComboBox  jcb2=new JComboBox();
	static JComboBox  jcb3=new JComboBox();
	static JComboBox jcb4=new JComboBox();
	static JComboBox jcb5=new JComboBox();
	static JComboBox  jcb6=new JComboBox();
	static JTextArea jf=new JTextArea();
	
	static JPanel panel=new JPanel();
	public static void paint()  {
		String str=(String)jcb.getSelectedItem();
		
		a[0]=(Integer) jcb1.getSelectedItem();
		a[1]=(Integer) jcb2.getSelectedItem();
		a[2]=(Integer) jcb3.getSelectedItem();
		a[3]=(Integer) jcb4.getSelectedItem();
		a[4]=(Integer) jcb5.getSelectedItem();
		a[5]=(Integer) jcb6.getSelectedItem();
		
		//建立和服务器的连接
		String str2=Bus_logic.transfer_sql(str);
		Socket s;
		
		try {
			s = new Socket("localhost",8888);
			
			//System.out.println("建立了与服务器的连接");
			OutputStream os=s.getOutputStream();
			os.write(str.getBytes());
			jf.append("->服务器："+str);
			
//			InputStream inputStream=s.getInputStream();//获取一个输入流，接收服务端的信息
//            InputStreamReader inputStreamReader=new InputStreamReader(inputStream);//包装成字符流，提高效率
//            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);//缓冲区
//            String info="";
//            String temp=null;//临时变量
//            while((temp=bufferedReader.readLine())!=null){
//                info+=temp;
//
//            }
//            jf.append("服务器->"+info);
//
//			bufferedReader.close();
//			inputStream.close();
			os.flush();
			os.close();
			s.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		ResultSet rs=dbconnection.select_return(str2);
		XYDataset dataset=Bus_logic.createDataset(rs,Presentation_layer.a);
		
		
		//创建主题样式
		StandardChartTheme standardChartTheme=new StandardChartTheme("CN");
		//设置标题字体
		standardChartTheme.setExtraLargeFont(new Font("隶书",Font.BOLD,20));
		//设置图例的字体
		standardChartTheme.setRegularFont(new Font("宋书",Font.PLAIN,15));
		//设置轴向的字体
		standardChartTheme.setLargeFont(new Font("宋书",Font.PLAIN,15));
		//应用主题样式
		ChartFactory.setChartTheme(standardChartTheme);
		JFreeChart 	freechart =ChartFactory.createTimeSeriesChart(
				str+"时间变化图 ", // title
	            "时间"+a[0]+"."+a[1]+"."+a[2]+"---"+a[3]+"."+a[4]+"."+a[5], // x-axis label
	            "数值", // y-axis label
	            dataset, // data
	            true, // create legend?
	            true, // generate tooltips?
	            false // generate URLs?
	        );       
	
		//设置主题
		TextTitle textTitle = freechart.getTitle();
		textTitle.setFont(new Font("宋体",Font.BOLD,20));
		LegendTitle legend = freechart.getLegend();
		if (legend!=null) {
		legend.setItemFont(new Font("宋体", Font.BOLD, 20));
		}
		

	
		ChartPanel panel1=new ChartPanel(freechart);
		JFrame jframe1=new JFrame("varing chart");
		jframe1.add(panel1);
		jframe1.setSize(450,400);
		jframe1.setVisible(true);

	}
	public static void window() {
		JFrame jframe=new JFrame("股票信息");
		Container contenpane=jframe.getContentPane();
		//初始化6个下拉框
		for(int i=1999;i<=2016;i++) {
			jcb1.addItem(i);
			jcb4.addItem(1999+2016-i);
			}
		for(int i=1;i<=12;i++) {
			jcb2.addItem(i);
			jcb5.addItem(13-i);
			}
		for(int i=1;i<=30;i++) {
			jcb3.addItem(i);
			jcb6.addItem(31-i);
		}
		
		panel.setLayout(null); 
		
		JLabel lable1=new JLabel("日期1");
		lable1.setBounds(20, 30, 60, 40);
		jcb1.setBounds(80, 40, 70, 20);
		jcb2.setBounds(150, 40, 50, 20);
		jcb3.setBounds(200, 40, 50, 20);
		panel.add(jcb1);
		panel.add(jcb2);
		panel.add(jcb3);
		panel.add(lable1);
		JLabel lable2=new JLabel("日期2");
		jcb4.setBounds(80, 130, 70, 20);
		panel.add(jcb4);
		jcb5.setBounds(150, 130, 50, 20);
		jcb6.setBounds(200, 130, 50, 20);
		panel.add(jcb5);
		panel.add(jcb6);
		lable2.setBounds(20,120, 60, 40);
		panel.add(lable2);
		
		jcb.setBounds(320,75,100, 30);
		panel.add(jcb);
		jf.setEditable(false);
		jf.setLineWrap(true);
		jf.setFont(new Font("宋体",Font.PLAIN,16));
		JScrollPane up=new JScrollPane(jf,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		up.setBounds(10,160,300,100);
		panel.add(up);
		JButton button1=new JButton();
		button1.setLabel("查询");
		button1.setBounds(60, 270, 100, 30);
		panel.add(button1);
		JButton button2=new JButton();
		button2.setLabel("画图");
		button2.setBounds(290,270, 100, 30);
		panel.add(button2);
		button2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
			
					paint();
				
			}
			}
		);
		
	
		
		contenpane.add(panel);
		jframe.setSize(450,400);
		jframe.setVisible(true);
		}

	 public static void main(String[] args) {
		 window();
		 jf.append("建立了与服务器的连接\n");
	 }

}


