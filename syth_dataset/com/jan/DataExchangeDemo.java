package com.jan;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.jan.HireDemo.OnMessageArrived;
import com.thingdata.sdk.mbc.internal.Message;
import com.thingdata.sdk.mbc.internal.MessageBusClient;
import com.thingdata.sdk.mbc.internal.MessageBusException;
import com.thingdata.sdk.mbc.internal.MessageLevel;
import com.thingdata.sdk.mbc.service.MessageArrivedListener;
import com.thingdata.sdk.mbc.service.MessageBusClientService;
import com.thingdata.sdk.mbc.util.JsonMessage;
import com.thingdata.sdk.mbc.util.JsonMessageBuilder;

/**
 * @author jan
 * @date 2017年11月20日 上午3:11:02
 * @version V1.0 
 */
public class DataExchangeDemo {

	  /**
	   * 定义消息处理回调函数，只需要实现 MessageArrivedListener 接口
	   * 客户端可以通过主题以及消息内容来进行判断并执行更加丰富的处理操作
	   */
	  class OnMessageArrived implements MessageArrivedListener {
	    @Override
	    public void onMessageArrived(Message message) {
	      String topic = message.getTopic();
	      String jsonContent = new String(message.getPayload());
	      System.out.println("收到一条来自主题【"+topic+"】的消息：");
	      System.out.println(jsonContent);
	      System.out.println();
	      System.out.println("----------------------------------");
	    }
	  }

//	  private final String SUBSCRIBE_TOPIC = "$META/TEST/My_Hire/Engineer/+";
	  private final String SUBSCRIBE_TOPIC = "$SYS/ERROR/JSON/SYNTH_DATA_EXCHANGE/#";
//	  private final String SUBSCRIBE_TOPIC = "$META/SYNTH_DATA_EXCHANGE/#";
	  private final String SEND_TOPIC = "$META/SYNTH_DATA_CHANGE/";

	  private MessageBusClientService client;

	  /**
	   * 初始化客户端并连接到服务总线
	   *
	   * @throws URISyntaxException  the uri syntax exception
	   * @throws MessageBusException the message bus exception
	   */
	  public DataExchangeDemo() throws URISyntaxException, MessageBusException {
//	    this.client = new MessageBusClient("tcp://localhost:1883", "Hire_Test_"+System.currentTimeMillis());
	    this.client = new MessageBusClient("tcp://www.thing-data.com:1883", "Jan_"+System.currentTimeMillis());
	    this.client.connect();
	  }

	  /**
	   * 订阅消息
	   *
	   * @throws MessageBusException the message bus exception
	   */
	  public void subscribe() throws MessageBusException {
	    // 使用 OnMessageArrived 处理所有 $META/TEST/My_Hire/Engineer/ 所有下一级子目录下的事件
	    this.client.subscribe(SUBSCRIBE_TOPIC, MessageLevel.AT_LEAST_ONCE, new OnMessageArrived());

	    //或者使用匿名类的方式来处理消息
//	    this.client.subscribe(SUBSCRIBE_TOPIC, MessageLevel.AT_LEAST_ONCE, new MessageArrivedListener() {
//	      @Override
//	      public void onMessageArrived(Message message) {
//	        String topic = message.getTopic();
//	        String jsonContent = new String(message.getPayload());
//	        System.out.println("收到一条来自主题【"+topic+"】的消息：");
//	        System.out.println(jsonContent);
//	        System.out.println();
//	        System.out.println("----------------------------------");
//	      }
//	    });
	  }

	  /**
	   * 发送消息
	   */
	  public void send(String topic) throws MessageBusException {
	    client.send(topic, MessageLevel.AT_LEAST_ONCE, constructContent());
	  }
	  
	  public void send(String topic, String msg) throws MessageBusException {
		    client.send(topic, MessageLevel.AT_LEAST_ONCE, msg);
		  }

	  public static void main(String[] args) throws URISyntaxException, MessageBusException, InterruptedException, FileNotFoundException {
		DataExchangeDemo demo = new DataExchangeDemo();
	    System.out.println("是否已连接：" + demo.client.isConnected());
	    demo.subscribe();
	    
	    //方式一读取磁盘文件
//		DataExchange de = new DataExchange(
//				"C:\\Users\\MrLiao\\Desktop\\gitlab\\Integration-acl\\meta-files\\data_exchange.yaml", 
//				"C:\\Users\\MrLiao\\Desktop\\gitlab\\Integration-acl\\meta-files\\GLOBAL.yaml");
	    //方式二读取服务器文件
		DataExchange de = new DataExchange(
				"/com/jan/data_exchange.yaml", 
				"/com/jan/GLOBAL.yaml");
	    
//		String s = de.generateData();
//		System.out.println(s);
		
        Runnable runnable = new Runnable() {  
            public void run() {  
                // task to run goes here  
//                System.out.println("Hello !!");
            	String msg = de.generateData();
            	System.out.println("$META/SYNTH_DATA_EXCHANGE/" + de.getModelName() + "/" + de.getStreeName());
   //             System.out.println(msg);
                try {
					demo.send("$META/SYNTH_DATA_EXCHANGE/" + de.getModelName() + "/" + de.getStreeName(), msg);
				} catch (MessageBusException e) {
					e.printStackTrace();
				}
            }  
        };  
        ScheduledExecutorService service = Executors  
                .newSingleThreadScheduledExecutor();
     // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
        service.scheduleAtFixedRate(runnable, 10, 100, TimeUnit.MILLISECONDS);
	    
	    
//	    demo.send("$META/TEST/My_Hire/Engineer/Junior Engineer"); // 此时 OnMessageArrived 中定义的方法被执行
//	    demo.send("$META/TEST/My_Hire/Engineer/Engineer"); // 此时 OnMessageArrived 中定义的方法被执行
//	    demo.send("$META/TEST/My_Hire/Engineer/Engineer/Another"); // 此时 OnMessageArrived 中定义的方法不会被执行
//	    Thread.sleep(2000); // 使用异步客户端时注意
//	    demo.client.disconnect(); // 断开连接
	    System.out.println("是否已连接：" + demo.client.isConnected());
	  }

	  /**
	   * 使用 builder 方法来构造规范的 json 数据
	   *
	   * @return the string
	   */
	  private String constructContent() {
	    HashMap<String, Object> properties = new HashMap<String, Object>();
	    properties.put("Person_Addr", "1000 Blackweill Dr., NC");
	    properties.put("Position", "Engineer/Junior Engineer");
	    properties.put("Person_Name", "John");
	    properties.put("Person_ID", "12345");

	    JsonMessage jsonMessage = new JsonMessageBuilder()
	        .setProjectName("Test")
	        .setEvent("My_Hire")
	        .setTimestamp(System.currentTimeMillis())
	        .setProperties(properties) // 传入包含数据信息的 map 对象
	        .createJsonMessage();
	    return jsonMessage.getJsonStr();
	  }
	}
