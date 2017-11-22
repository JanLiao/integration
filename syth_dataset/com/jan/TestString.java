package com.jan;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jan
 * @date 2017年11月19日 下午7:51:16
 * @version V1.0 
 */
public class TestString {
	
    public static String randomHexString(int len)  {  
        try {  
            StringBuffer result = new StringBuffer();  
            for(int i=0;i<len;i++) {  
                result.append(Integer.toHexString(new Random().nextInt(16)));  
            }  
            return result.toString().toUpperCase();  
              
        } catch (Exception e) {  
            // TODO: handle exception  
            e.printStackTrace();  
              
        }  
        return null;  
          
    }

	public static void main(String[] args) {
		String s3=RandomString.getHex(6);
		String s1 = RandomString.get(8);
		System.out.println(s3);
		System.out.println(s1);
		
		TestString ts = new TestString();
		System.out.println(ts.randomHexString(8));
//		^(0[1-9]|1[0-2])$
		String pattern = "^(0[1-9]|1[0-2])$";
		Pattern r = Pattern.compile(pattern);
//		while(true) {
//			String str = RandomString.get(8);
//	        String s = "^\\d{1}$|^\\d{2}$|^\\d{3}$|^\\d{4}$";
//	        Pattern p=Pattern.compile(s); 
//	        Matcher m=p.matcher(str); 
//	        System.out.println(str + "=" + m.find());//返回true
//	        if(m.find()) {
//	        	System.out.println(str);
//	        	break;
//	        }
//		}
		
		String str = RandomString.get(8);
        String s = "^\\d{1}$|^\\d{2}$|^\\d{3}$|^\\d{4}$";
        Pattern p=Pattern.compile(s); 
        Matcher m=p.matcher("D555"); 
        System.out.println(str + "=" + m.find());//返回true
	}
}
