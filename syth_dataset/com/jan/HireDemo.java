package com.jan;
/*
 * ThingData数据枢纽 消息服务Demo程序
 * 得一物联合 版权所有
 * Copyright © 2014-2017 Deyi IoT LLC. All Rights Reserved
 *
 */

import com.thingdata.sdk.mbc.internal.Message;
import com.thingdata.sdk.mbc.internal.MessageBusClient;
import com.thingdata.sdk.mbc.internal.MessageBusException;
import com.thingdata.sdk.mbc.internal.MessageLevel;
import com.thingdata.sdk.mbc.internal.async.MessageBusAsyncClient;
import com.thingdata.sdk.mbc.service.MessageArrivedListener;
import com.thingdata.sdk.mbc.service.MessageBusClientService;
import com.thingdata.sdk.mbc.util.JsonMessage;
import com.thingdata.sdk.mbc.util.JsonMessageBuilder;

import java.net.URISyntaxException;
import java.util.HashMap;

public class HireDemo {
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

  private final String SUBSCRIBE_TOPIC = "$META/TEST/My_Hire/Engineer/+";

  private MessageBusClientService client;

  /**
   * 初始化客户端并连接到服务总线
   *
   * @throws URISyntaxException  the uri syntax exception
   * @throws MessageBusException the message bus exception
   */
  public HireDemo() throws URISyntaxException, MessageBusException {
//    this.client = new MessageBusClient("tcp://localhost:1883", "Hire_Test_"+System.currentTimeMillis());
    this.client = new MessageBusClient("tcp://www.thing-data.com:1883", "Hire_Test_"+System.currentTimeMillis());
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
//    this.client.subscribe(SUBSCRIBE_TOPIC, MessageLevel.AT_LEAST_ONCE, new MessageArrivedListener() {
//      @Override
//      public void onMessageArrived(Message message) {
//        String topic = message.getTopic();
//        String jsonContent = new String(message.getPayload());
//        System.out.println("收到一条来自主题【"+topic+"】的消息：");
//        System.out.println(jsonContent);
//        System.out.println();
//        System.out.println("----------------------------------");
//      }
//    });
  }

  /**
   * 发送消息
   */
  public void send(String topic) throws MessageBusException {
    client.send(topic, MessageLevel.AT_LEAST_ONCE, constructContent());
  }

  public static void main(String[] args) throws URISyntaxException, MessageBusException, InterruptedException {

	  for(int i = 0; i < 500000; i++) {
		    HireDemo demo = new HireDemo();
		    System.out.println("是否已连接：" + demo.client.isConnected());
		    demo.subscribe();
		    demo.send("$META/TEST/My_Hire/Engineer/Junior Engineer"); // 此时 OnMessageArrived 中定义的方法被执行
		    demo.send("$META/TEST/My_Hire/Engineer/Engineer"); // 此时 OnMessageArrived 中定义的方法被执行
		    demo.send("$META/TEST/My_Hire/Engineer/Engineer/Another"); // 此时 OnMessageArrived 中定义的方法不会被执行
//		    Thread.sleep(2000); // 使用异步客户端时注意
		    demo.client.disconnect(); // 断开连接
		    System.out.println("是否已连接：" + demo.client.isConnected());
	  }
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
