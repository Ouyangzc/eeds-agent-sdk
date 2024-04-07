package com.elco.eeds.agent.sdk.core.util;

import cn.hutool.core.util.ObjectUtil;
import com.elco.eeds.agent.mq.nats.plugin.NatsPlugin;
import com.elco.eeds.agent.mq.plugin.MQPluginManager;
import com.elco.eeds.agent.mq.plugin.MQServicePlugin;
import com.elco.eeds.mq.eventbus.plugin.EventBusPlugin;
import java.util.concurrent.Future;

/**
 * @ClassName MqPluginUtils
 * @Description Mq消息工具类
 * @Author OuYang
 * @Date 2024/4/1 9:08
 * @Version 1.0
 */
public class MqPluginUtils {

  private static MQServicePlugin mqPlugin;

  static {
    boolean slimModle = AgentResourceUtils.isSlimModle();
    if (slimModle) {
      mqPlugin = MQPluginManager.getMQPlugin(EventBusPlugin.class.getName());
    } else {
      mqPlugin = MQPluginManager.getMQPlugin(NatsPlugin.class.getName());
    }
  }

  private MqPluginUtils() {
  }

  public static Future<String> request(String topic, String msg) {
    return mqPlugin.request(topic, msg);
  }


  private static void publishMessage(String topic, String msg) {

    try {
      if (ObjectUtil.isNotEmpty(mqPlugin)) {
        mqPlugin.publish(topic, msg, null);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 数据源心跳
   *
   * @param topic
   * @param msg
   */

  public static void sendThingsHeartBeatMsg(String topic, String msg) {
    publishMessage(topic, msg);
  }

  /**
   * 数据源连接消息
   *
   * @param topic
   * @param msg
   */
  public static void sendThingsConnectStatusMsg(String topic, String msg) {
    publishMessage(topic, msg);
  }

  /**
   * 数据源统计上报
   *
   * @param topic
   * @param msg
   */
  public static void sendThingsDataCountMsg(String topic, String msg) {
    publishMessage(topic, msg);
  }

  /**
   * 实时数据
   *
   * @param topic
   * @param msg
   */
  public static void sendRealTimeValueMsg(String topic, String msg) {
    publishMessage(topic, msg);
  }

  /**
   * 变量下发确认
   *
   * @param topic
   * @param msg
   */
  public static void sendOrderConfirmMsg(String topic, String msg) {
    publishMessage(topic, msg);
  }

  /**
   * 变量下发结果
   *
   * @param topic
   * @param msg
   */
  public static void sendOrderResultMsg(String topic, String msg) {
    publishMessage(topic, msg);
  }

  /**
   * 指令下发-结果
   *
   * @param topic
   * @param msg
   */
  public static void sendCmdResultMsg(String topic, String msg) {
    publishMessage(topic, msg);
  }

  /**
   * 指令下发-超时
   *
   * @param topic
   * @param msg
   */
  public static void sendCmdTimeoutMsg(String topic, String msg) {
    publishMessage(topic, msg);
  }

  /**
   * 指令下发-确认
   *
   * @param topic
   * @param msg
   */
  public static void sendCmdConfirmMsg(String topic, String msg) {
    publishMessage(topic, msg);
  }

  /**
   * 数据同步-完成
   *
   * @param topic
   * @param msg
   */
  public static void sendDataSyncFinishMsg(String topic, String msg) {
    publishMessage(topic, msg);
  }

  /**
   * 数据同步 数据
   *
   * @param topic
   * @param msg
   */
  public static void sendDataSyncPropertiesValueMsg(String topic, String msg) {
    publishMessage(topic, msg);
  }

  /**
   * 数据同步确认消息
   *
   * @param topic
   * @param msg
   */
  public static void sendDataSyncConfirmMsg(String topic, String msg) {
    publishMessage(topic, msg);
  }

  /**
   * 客户端链接测试响应
   *
   * @param topic
   * @param msg
   */
  public static void sendAgentLinkTestMsg(String topic, String msg) {
    publishMessage(topic, msg);
  }

  /**
   * 客户端链接测试报文(集群)
   *
   * @param topic
   * @param msg
   */
  public static void sendAgentLinkTestClusterMsg(String topic, String msg) {
    publishMessage(topic, msg);
  }

  /**
   * 客户端心跳响应请求
   *
   * @param topic
   * @param msg
   */
  public static void sendAgentHeartBeatMsg(String topic, String msg) {
    publishMessage(topic, msg);
  }


  public static void sendDaDaDa() {
    publishMessage("dadada", "dongdongdongdong");
  }
}
