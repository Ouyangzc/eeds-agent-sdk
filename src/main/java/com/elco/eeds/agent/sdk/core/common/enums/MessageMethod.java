package com.elco.eeds.agent.sdk.core.common.enums;

/**
 * @ClassName MessageMethod
 * @Description 报文Method
 * @Author OuYang
 * @Date 2024/4/19 9:58
 * @Version 1.0
 */
public enum MessageMethod {
  AGENT_HEART_REQ("agent_heartBeat_request","客户端心跳请求"),
  AGENT_HEART_RSP("agent_heartBeat_respond","客户端心跳回应"),
  AGENT_CONFIG_GLOBAL("agent_global_config","客户端全局配置"),
  AGENT_CONFIG_LOCAL_CONFIG("agent_local_config","客户端私有配置"),
  AGENT_TOKEN_UPDATE("agent_update_token","客户端token更新"),
  AGENT_LINK_TEST("agent_test_respond","客户端链接测试"),

  THINGS_CONNECT_STATUS("things_connectStatus","数据源连接状态"),
  THINGS_BEAT_HEART("things_heartBeat","数据源心跳"),

  AGENT_ORDER_CONFIRM("agent_order_confirm","变量下发--确认"),
  AGENT_ORDER_RESP("agent_order_respond","变量下发--结果"),
  CMD_CONFIRM("agent_instruct_confirm","指令下发--确认"),
  CMD_RESPOND("agent_instruct_respond","指令下发--结果"),

  DATA_COUNT_POST("data_count_post","数据统计--发送统计记录报文"),
  DATA_SYNC_CONFIRM("data_sync_confirm","数据同步--确认"),
  DATA_SYNC_DATA("data_sync_properties","数据同步--同步数据"),
  DATA_SYNC_FINISH("data_sync_finish","数据同步--同步完成"),

  DATA_REALTIME_DATA("properties_data_report","实时数据"),


  DB_CHANGE_RESULT("storage_change_result_event","数据库切换结果")
  ;

  private String method;
  private String desc;

  MessageMethod(String method, String desc) {
    this.method = method;
    this.desc = desc;
  }

  public String getMethod() {
    return method;
  }
}
