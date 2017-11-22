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
import com.thingdata.sdk.mbc.service.MessageArrivedListener;
import com.thingdata.sdk.mbc.service.MessageBusClientService;
import com.thingdata.sdk.mbc.util.JsonMessage;
import com.thingdata.sdk.mbc.util.JsonMessageBuilder;

import java.net.URISyntaxException;
import java.util.HashMap;

/**
 * 主要展示通过主题过滤器来订阅多级目录的消息
 *
 * @author dan E-mail:hi@chendan.me
 * @since 17-9-3
 */
public class HireDemoTopicFilter {

  public static void main(String[] args) throws URISyntaxException, MessageBusException, InterruptedException {
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

    //订阅 $META/TEST/My_Hire/Engineer/ 下所有子目录的消息
    final String SUBSCRIBE_TOPIC = "$META/TEST/My_Hire/Engineer/#";
//    MessageBusClientService client = new MessageBusClient("tcp://localhost:1883", "Hire_Test_"+System.currentTimeMillis());
    MessageBusClientService client = new MessageBusClient("tcp://www.thing-data.com:1883", "Hire_Test_"+System.currentTimeMillis());

    client.connect(); // 确保首先连接到服务总线

    client.subscribe(SUBSCRIBE_TOPIC, MessageLevel.AT_LEAST_ONCE, new MessageArrivedListener() {
      @Override
      public void onMessageArrived(Message message) {
        System.out.println("收到来自主题【" + message.getTopic() +"】的消息");
        System.out.println("----------------------------------------------------");
      }
    });

    //客户端将会处理这些消息
    client.send("$META/TEST/My_Hire/Engineer/Junior/a/b/Engineer", jsonMessage.getJsonStr());
    client.send("$META/TEST/My_Hire/Engineer/Junior/a/b/c/Engineer", jsonMessage.getJsonStr());

//  client.send("$META/TEST/My_Hire/Engineer/Junior Engineer", "{Bad json string"); // 不规范的json字符串将会导致运行时异常

    Thread.sleep(2000);
    client.disconnect();
  }
}
