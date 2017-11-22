package com.jan;

import java.util.Random;
import java.util.regex.*;

/**
 * @author jan
 * @date 2017年11月19日 下午8:15:49
 * @version V1.0 
 */
import nl.flotsam.xeger.Xeger;


public class XegerTest {



    public static void main(String[] args) {
    	
    	Random r = new Random();
//    	for(int i = 0; i < 10; i++) {
//    		System.out.println(new Random().nextInt(2));
//    	}

        String regex = "[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}";
//        String regex1 = "^(0[1-9]|1[0-2])$";
        String regex1 = "^(1|-1)$";
        System.out.println(regex1.length());
//        for(int i = 0; i < regex1.length(); i++) {
//        	System.out.println(regex1.charAt(i));
//        }
        String s = "";
        for(int i = 0; i < regex1.length(); i++) {
        	if(regex1.charAt(i) == '\\') {
        		s = s + "[0-9]";
        	}else if (regex1.charAt(i) == 'd') {
        		
        	}else {
        		s = s + regex1.charAt(i);
        	}
        }
        
        System.out.println(s);
        //三位数
        Xeger generator = new Xeger(s);
        for (int i = 0; i < 20; i++) {

            String result = generator.generate();
            result = result.substring(1, result.length() - 1);
            System.out.println(result);
            assert result.matches(regex1);
        }
        
        String st = "^\\d{1}$|^\\d{2}$|^\\d{3}$|^\\d{4}$";
        Pattern p=Pattern.compile(st); 
        Matcher m=p.matcher("340"); 
        System.out.println(m.find());//返回true

    }

}
