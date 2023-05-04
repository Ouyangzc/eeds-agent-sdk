package com.elco.eeds.agent.sdk.core.common.enums;

/**
 * @ClassName AgentStatus
 * @Description 客户端状态
 * @Author ouyang
 * @Date 2023/2/28 13:35
 * @Version 1.0
 */
public enum AgentStatus {
	/**
	 * 初始化状态
	 * 默认
	 */
	INTI,
	/**
	 * 客户端初始化协议成功，并可以与Server端进行通信
	 */
	READY,
	/**
	 * sdk加载文件,开启定时任务
	 */
	LOAD,
	/**
	 * 客户端启动正常
	 */
	RUNNING,
	/**
	 * 客户端启动失败，代表服务不可用
	 */
	ERROR;
}
