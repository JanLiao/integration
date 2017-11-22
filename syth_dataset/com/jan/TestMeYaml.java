package com.jan;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import net.sf.json.JSONObject;

/**
 * @author jan
 * @date 2017年11月18日 下午10:56:21
 * @version V1.0 
 */
public class TestMeYaml {
    @SuppressWarnings("unchecked")
	public void testParseMeYaml() throws FileNotFoundException {
        Yaml yaml = new Yaml();
//        Me me = yaml.loadAs(new FileInputStream(new File("C:\\Users\\MrLiao\\Desktop\\structs导入jar包\\获取视频时长\\me.yaml")), Me.class);
        InputStream in = TestMeYaml.class.getResourceAsStream("/com/jan/me.yaml");
        Me me = yaml.loadAs(in, Me.class );
        System.out.println(me);
        
        Yaml yaml1 = new Yaml();
//        File f = new File("C:\\Users\\MrLiao\\Desktop\\structs导入jar包\\获取视频时长\\me.yaml");
        File f = new File("C:\\Users\\MrLiao\\Desktop\\gitlab\\Integration\\meta-files\\data_exchange.yaml");
        Object obj = yaml1.load(new FileInputStream(f));
        System.out.println(obj.getClass());
        System.out.println(obj);
        System.out.println();
        
        JSONObject jsonObj = JSONObject.fromObject(obj);
        System.out.println(jsonObj.get("MODEL"));
        Map<String, Object> map = (Map<String, Object>)jsonObj.get("MODEL");
        System.out.println(map.get("部门预算"));
        for(String s : map.keySet()) {
        	System.out.println(s);
        }
        
        for(Object o : map.values()) {
        	System.out.println(o);
        }
        
        List<Object> list = new ArrayList<Object>();
        
        for(Object o : map.values()) {
        	list.add(o);
        }
        
        System.out.println(list.get(2));
        
    }
    
    @SuppressWarnings("unchecked")
	public void testGlobal() throws FileNotFoundException {
    	Yaml yaml = new Yaml();
        File f = new File("C:\\Users\\MrLiao\\Desktop\\gitlab\\Integration\\meta-files\\GLOBAL.yaml");
        Object obj = yaml.load(new FileInputStream(f));
        System.out.println(obj.getClass());
//        System.out.println(obj);
        JSONObject jsonObj = JSONObject.fromObject(obj);
        System.out.println(jsonObj.get("ATTR"));
//        System.out.println(jsonObj.get("MODEL"));
        Map<String, Object> map = (Map<String, Object>)jsonObj.get("MODEL");
        List<Object> list = new ArrayList<Object>();
        for(Object o : map.values()) {
        	list.add(o);
        }
        System.out.println(list.get(0));
    }
    
    public static void main(String[] args) throws FileNotFoundException {
    	TestMeYaml test = new TestMeYaml();
    	test.testParseMeYaml();
    	test.testGlobal();
    }
}


