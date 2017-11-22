package com.jan;

import java.util.*;
import java.io.*;
import java.sql.*;

/**
 * @author jan
 * @date 2017年11月21日 下午10:43:40
 * @version V1.0 
 */
public class ExtractData {

	public static final String driver = "com.mysql.jdbc.Driver";
	
	public static final String url = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false";
	
	public static final String user = "root";
	
	public static final String password = "a123456";
	
	public Map<String, List<String>> map = new HashMap<String, List<String>>();
	
	public Map<String, List<String>> getSqlData() {
		
		String[] table = new String[] {"departmentalbudget", "npcbudget",
				"budgetindicator", "governmentprocurementinformation",
				"paymentdetails", "indicatorplan",
				"generalinformation",
				"generalinformationaccountingdocument",
				"generalinformationearly"};
		
		try {
			// 加载驱动程序
			Class.forName(driver);
			// 连续数据库
			Connection conn = DriverManager.getConnection(url, user, password);
			if (!conn.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			// statement用来执行SQL语句
			Statement statement = conn.createStatement();
			ResultSet rs = null;
			for(int i = 0; i < table.length; i++) {
				String sql = "select * from " + table[i];
				rs = statement.executeQuery(sql);
				ResultSetMetaData data=rs.getMetaData();
//				System.out.println(data.getColumnCount());
				int len = data.getColumnCount();
				for(int j = 3; j <= len; j++) {
//					System.out.println(data.getColumnName(j));
					if(map.containsKey(data.getColumnName(j))) {
						
					}else {
						List<String> list = new ArrayList<String>();
						map.put(data.getColumnName(j), list);
					}
				}
				while (rs.next()) {
					// 输出结果
					for(int j = 3; j <= len; j++) {
						String key = data.getColumnName(j);
						String value = "" + rs.getString(j);
						List<String> list = map.get(data.getColumnName(j));
						if(rs.getString(j) == null || "".equals(rs.getString(j))) {
//							System.out.print(" 666666" + ",");
						}else {
							if(list.size() == 0) {
								list.add(rs.getString(j).trim());
							}else {
								int flag = 0;
								for(String s : list) {
									if(s.equals(value.trim())) {
										flag = 1;
										break;
									}
								}
								if(flag == 0) {
									list.add(value.trim());
									map.put(key, list);
								}
							}
//							System.out.print(" " + rs.getString(j) + ",");
						}
					}
//					System.out.println();
				}
			}
			
			
//			// 要执行的SQL语句
//			String sql = "select * from departmentalbudget";
//			// 结果集
//			ResultSet rs = statement.executeQuery(sql);
//			ResultSetMetaData data=rs.getMetaData();
//			System.out.println(data.getColumnCount());
//			for(int i = 1; i <= data.getColumnCount(); i++) {
//				System.out.println(data.getColumnName(i));
//			}
//			String name = "";
//			while (rs.next()) {
//				// 选择Name这列数据
//				name = rs.getString(2);
//				// 输出结果
////				System.out.println("\t" + name);
//			}
//			System.out.println(name);
			rs.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			System.out.println("Sorry,can`t find the Driver!");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
	public static void main(String[] args) {
		ExtractData ed = new ExtractData();
		Map<String, List<String>> map = ed.getSqlData();
		for(String s : map.keySet()) {
			if(map.get(s).size() != 0) {
				System.out.println(s + ":" + map.get(s) + "=" + map.get(s).get(0));
			}else {
				System.out.println(s + ":" + map.get(s) + "=");
			}
		}
	}
}
