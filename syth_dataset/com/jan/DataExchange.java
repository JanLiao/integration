package com.jan;

import java.io.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.yaml.snakeyaml.Yaml;

import com.thingdata.sdk.mbc.util.JsonMessage;
import com.thingdata.sdk.mbc.util.JsonMessageBuilder;

import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import nl.flotsam.xeger.Xeger;

/**
 * @author jan
 * @date 2017年11月19日 下午2:18:20
 * @version V1.0 
 */
public class DataExchange {

	//存放data_exchange文件MODEL
	List<Object> dataModel = new ArrayList<Object>();
	
	//存放global文件arr-list属性
	Map<String, Object> dataAttr = new HashMap<String, Object>();
	
	//存放arr-list属性的MODEL
	Map<String, Object> dataAttrModel = new HashMap<String, Object>();
	
	
	private String modelName;
	private String streeName;
	
	//存放sql文件中数据来随机生成数据源
	Map<String, List<String>> valueMap = new HashMap<String, List<String>>();
	
	
	
	/**
	 * @return the modelName
	 */
	public String getModelName() {
		return modelName;
	}

	/**
	 * @return the streeName
	 */
	public String getStreeName() {
		return streeName;
	}

	public DataExchange(String model, String attr) throws FileNotFoundException {
		initValueMap();
		initDataModel(model);
		initDataAttr(attr);
	}
	
	//初始化sql文件数据
	public void initValueMap() {
		ExtractData ed = new ExtractData();
		valueMap = ed.getSqlData();
		List<String> list = new ArrayList<String>();
		list.add("" + 1);
		list.add("" + 2);
		list.add("" + 3);
		valueMap.put("bt_code", list);
//		List<String> list1 = new ArrayList<String>();
//		list1.add("云南省");
//		valueMap.put("state_name", list1);
//		for(String s : valueMap.keySet()) {
//			if(valueMap.get(s).size() != 0) {
//				System.out.println(s + ":" + valueMap.get(s) + "=" + valueMap.get(s).get(0));
//			}else {
//				System.out.println(s + ":" + valueMap.get(s) + "=");
//			}
//		}
	}
	
	//初始化数据MODEL
	@SuppressWarnings("unchecked")
	public void initDataModel(String model) throws FileNotFoundException {
		Yaml yaml = new Yaml();
		InputStream in = DataExchange.class.getResourceAsStream(model);
//		File file = new File(model);
//		Object obj = yaml.load(new FileInputStream(file));
		Object obj = yaml.load(in);
		JSONObject jsonobj = JSONObject.fromObject(obj);
		Map<String, Object> map = (Map<String, Object>) jsonobj.get("MODEL");
		
		for(Object o : map.values()) {
//			System.out.println(o);
			dataModel.add(o);
		}
	}
	
	//初始化attr-list属性及MOEDEL
	@SuppressWarnings("unchecked")
	public void initDataAttr(String attr) throws FileNotFoundException {
		Yaml yaml = new Yaml();
//		File file = new File(attr);
//		Object obj = yaml.load(new FileInputStream(file));
		InputStream in = DataExchange.class.getResourceAsStream(attr);
		Object obj = yaml.load(in);
		JSONObject jsonobj = JSONObject.fromObject(obj);
		dataAttr = (Map<String, Object>) jsonobj.get("ATTR");
//		System.out.println(dataAttr);
//		for(Object o : dataAttr.values()) {
//			System.out.println(o);
//		}
		
		dataAttrModel = (Map<String, Object>) jsonobj.get("MODEL");
		
//		System.out.println(dataAttrModel);
		
	}
	
	//生成JsonMessage数据
	public String generateData() {
		Random random = new Random();
		int offset = random.nextInt(dataModel.size());
//		System.out.println(dataModel.get(offset));
		JSONObject jsonobj = JSONObject.fromObject(dataModel.get(offset));
//		System.out.println((String)jsonobj.get("NAME"));
		modelName = (String)jsonobj.get("NAME");
		
		HashMap<String, Object> properties = generateProperty(dataModel.get(offset));
		
		//生成jsonmessage
	    JsonMessage jsonMessage = new JsonMessageBuilder()
	            .setProjectName("SYNTH_DATA_EXCHANGE")
	            .setEvent((String)jsonobj.get("NAME"))
	            .setTimestamp(System.currentTimeMillis())
	            .setProperties(properties) // 传入包含数据信息的 map 对象
	            .createJsonMessage();
	    
	    return jsonMessage.getJsonStr();
	}
	
	//生成attr-list属性值
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> generateProperty(Object obj) {
		JSONObject jsonobj = JSONObject.fromObject(obj);
		List<String> listAttr = (List<String>) jsonobj.get("ATTR-LIST");
//		System.out.println(listAttr);
		HashMap<String, Object> properties = new HashMap<String, Object>();
//		System.out.println(dataAttr.size());

		for(String s : listAttr) {
			Object object = generateValue(s);
			if(s.equals("datapoint_name")) {
				streeName = (String) object;
//				System.out.println(6666);
			}
//			System.out.println(s + ":" + object);
			properties.put(s, object);
		}
		return properties;
	}
	
	//根据attr-list MODEL以及sql文件数据随机生成数据源
	@SuppressWarnings("unchecked")
	public Object generateValue(String str) {
		Object obj = null;
//				System.out.println(dataAttr.get(str));
				Map<String, String> map = (Map<String, String>) dataAttr.get(str);
				if(map.get("TYPE").equals(Constant.typeModel)) {
					obj = getModelStree((String) map.get("MODEL"));
//					streeName = (String) obj;
//					System.out.println(obj);
				}else if(map.get("TYPE").equals(Constant.typeString)) {
					if(valueMap.get(str).size() == 0) {
						if(map.containsKey("REGEX")) {
//							System.out.println((String) map.get("REGEX"));
							obj = generateString((String) map.get("REGEX"));
//							System.out.println(obj);
						}else {
							if(new Random().nextInt(2) == 0) {
								obj = RandomString.get(8);
							}else {
								obj = randomHexString(8);
							}
						}
					}else {
						obj = valueMap.get(str).get(new Random().nextInt(valueMap.get(str).size()));
					}
				}else if(map.get("TYPE").equals(Constant.typeNumber)) {
					if(valueMap.get(str).size() == 0) {
						if(map.containsKey("REGEX")) {
//							System.out.println((String) map.get("REGEX"));
							obj = generateString((String) map.get("REGEX"));
							obj = Integer.parseInt((String)obj);
//							System.out.println(obj);
//							if(new Random().nextInt(2) == 0) {
//								obj = 1;
//							}else {
//								obj = 1;
//							}
						}else {
							obj = 80 + new Random().nextInt(30);
						}
					}else {
						String valueStrs = valueMap.get(str).get(new Random().nextInt(valueMap.get(str).size()));
						obj = Integer.parseInt(valueStrs);
					}
				}
		return obj;
	}
	
	//根据global文件stree生成数据
	@SuppressWarnings("unchecked")
	public String getModelStree(String modelName) {
		Map<String, Object> map = (Map<String, Object>) dataAttrModel.get(modelName);
		Map<String, String> mapStree = (Map<String, String>) map.get("STREE");
		String s = "";
		Random random = new Random();
		int offset = random.nextInt(mapStree.size());
		int len = 0;
		Map<String, Object> mapChild = null;
		for(String ss : mapStree.keySet()) {
			if(offset == len) {
				s = s + ss;
//				System.out.println((Object)mapStree.get(ss));
				Object o = (Object)mapStree.get(ss);
//				JSONObject jsonobj = JSONObject.fromObject(o);
//				System.out.println(jsonobj.toString());
				if(o instanceof JSONNull) {
//					System.out.println(222);
				}else {
					mapChild = (Map<String, Object>) o;
				}
				break;
			}
			len++;
		}
//		System.out.println(s);
//		System.out.println(offset + "=" + len);
//		System.out.println(mapStree);
//		System.out.println(mapChild);
		return s + deepStree(mapChild);
	}
	
	//递归生成stree数据
	@SuppressWarnings("unchecked")
	public String deepStree(Map<String, Object> map) {
		Random random = new Random();
		String s = "";
		if(map != null) {
			int offset = random.nextInt(map.size() + 1);
//			System.out.println(map.size() + "=" + offset);
			if(offset == map.size()) {
				return "";
			}else {
				int k = 0;
				Map<String, Object> mm = null;
				for(String str : map.keySet()) {
					if(offset == k) {
						s = "/" + s + str;
						Object o = (Object)map.get(str);
						if(o instanceof JSONNull) {
//							System.out.println(666666);
						}else {
							mm = (Map<String, Object>) o;
						}
						break;
					}
					k++;
				}
				return s + deepStree(mm);
			}
		}else {
			return "";
		}
	}
	
	//若sql文件数据值为空，则随机生成8位16进制字符串
    public String randomHexString(int len)  {  
        try {  
            StringBuffer result = new StringBuffer();  
            for(int i=0;i<len;i++) {  
                result.append(Integer.toHexString(new Random().nextInt(16)));  
            }  
            return result.toString().toUpperCase();  
              
        } catch (Exception e) {   
            e.printStackTrace();  
              
        }  
        return null;  
          
    }
	
  //根据正则表达式反向生成符合表达式的字符串
	public String generateString(String regex) {
//		System.out.println(regex);
        String s = "";
        for(int i = 0; i < regex.length(); i++) {
        	if(regex.charAt(i) == '\\') {
        		s = s + "[0-9]";
        	}else if (regex.charAt(i) == 'd') {
        		
        	}else {
        		s = s + regex.charAt(i);
        	}
        }
		Xeger generator = new Xeger(s);
		String result = generator.generate();
		result = result.substring(1, result.length() - 1);
//		System.out.println(regex + "=" + s + "=" + result);
		return result;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		DataExchange de = new DataExchange(
				"/com/jan/data_exchange.yaml", 
				"/com/jan/GLOBAL.yaml");
//		DataExchange de = new DataExchange(
//				"C:\\Users\\MrLiao\\Desktop\\gitlab\\Integration-acl\\meta-files\\data_exchange.yaml",
//				"C:\\Users\\MrLiao\\Desktop\\gitlab\\Integration-acl\\meta-files\\GLOBAL.yaml");
		String s = de.generateData();
		System.out.println(s);
		
        Runnable runnable = new Runnable() {  
            public void run() {  
                // task to run goes here  
                System.out.println("Hello !!");  
                System.out.println(de.generateData());
            }  
        };  
        ScheduledExecutorService service = Executors  
                .newSingleThreadScheduledExecutor();
     // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
        service.scheduleAtFixedRate(runnable, 10, 100, TimeUnit.MILLISECONDS);
	}
	
}
