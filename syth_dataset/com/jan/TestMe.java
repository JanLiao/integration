package com.jan;

import java.io.*;
import java.util.*;

import org.yaml.snakeyaml.Yaml;

/**
 * @author jan
 * @date 2017年11月18日 下午11:27:33
 * @version V1.0 
 */
public class TestMe {

	public static void main(String[] args) {
	    try {
	        Yaml yaml = new Yaml();
	        java.net.URL url = TestMe.class.getClassLoader().getResource("/com/jan/me1.yaml");
	        if (url != null) {
	            //获取test.yaml文件中的配置数据，然后转换为obj，
	            Object obj =yaml.load(new FileInputStream(url.getFile()));
	            System.out.println(obj);
	            //也可以将值转换为Map
	            Map map =(Map)yaml.load(new FileInputStream(url.getFile()));
	            System.out.println(map);
	            //通过map我们取值就可以了.
	            
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
