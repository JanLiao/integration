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

import java.net.URISyntaxException;

/**
 * @author dan E-mail:hi@chendan.me
 * @since 17-9-13
 */
public class Subscriber {
  private static int count = 0;

  public static void main(String[] args) throws URISyntaxException, MessageBusException {
//    MessageBusClient client = new MessageBusClient("tcp://localhost:1883", "chendan_test_sub");
    MessageBusClient client = new MessageBusClient("tcp://www.thing-data.com:1883", "chendan_test_sub");
    client.connect();
    client.subscribe("thread/#", MessageLevel.AT_LEAST_ONCE, new MessageArrivedListener() {
      @Override
      public void onMessageArrived(Message message) {
        System.out.println(message.getTopic() + ">>> " + new String(message.getPayload()));
        System.out.println("------------------------------"+count++);
      }
    });
  }
}
