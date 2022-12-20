package com.elco.eeds.agent.sdk.core.connect.manager;


import cn.hutool.json.JSONUtil;
import com.elco.eeds.agent.sdk.core.connect.ThingsConnection;
import com.elco.eeds.agent.sdk.core.connect.ThingsConnectionHandler;
import com.elco.eeds.agent.sdk.transfer.beans.things.ThingsDriverContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ：ytl
 * @date ：Created in 2022/12/1 15:55
 * @description： 连接管理员
 */
public class ConnectManager {
    public static final Logger logger = LoggerFactory.getLogger(ConnectManager.class);
    private static ConcurrentHashMap<String, ThingsConnection> CONNECTION_MAP = new ConcurrentHashMap<String, ThingsConnection>(16);


    private static ConcurrentHashMap<String, ThingsConnectionHandler> CONNECTION_HANDLER_MAP = new ConcurrentHashMap<String, ThingsConnectionHandler>(256);


    /**
     * 添加数据源连接实例
     *
     * @param handler
     */
    public static void addHandler(ThingsConnectionHandler handler) {

        CONNECTION_HANDLER_MAP.put(handler.getThingsId(), handler);
    }


    /**
     * 添加数据源连接实例
     *
     * @param key 数据源ID
     */
    public static ThingsConnectionHandler getHandler(String key) {
        return CONNECTION_HANDLER_MAP.get(key);
    }

    /**
     * 获取所有数据源连接实例
     */
    public static List<ThingsConnectionHandler> getAllHandler() {
        return new ArrayList<>(CONNECTION_HANDLER_MAP.values());
    }


    /**
     * 添加数据源连接
     *
     * @param key
     * @param connection
     */
    public static void addConnection(String key, ThingsConnection connection) {
        CONNECTION_MAP.put(key, connection);
    }

    /**
     * 获取数据源连接
     *
     * @param key
     * @return
     */
    public static ThingsConnection getConnection(String key) {
        return CONNECTION_MAP.get(key);
    }

    /**
     * 删除数据源连接
     *
     * @param key
     */
    public static void delConnection(String key) {
        CONNECTION_MAP.get(key).disconnect();
        CONNECTION_MAP.remove(key);
    }

    /**
     * 创建连接并实现handler
     *
     * @param driverContext
     */
    public static void create(ThingsDriverContext driverContext, String connectKey) {
        logger.debug("开始创建连接，连接信息：{}", JSONUtil.toJsonStr(driverContext));
        ThingsConnection connection = ConnectManager.getConnection(connectKey);
        connection.connect(driverContext);
        ThingsConnectionHandler handler = (ThingsConnectionHandler) connection;
        handler.setContext(driverContext);
        handler.setThingsConnection(connection);
        handler.setThingsId(driverContext.getThingsId());
        ConnectManager.addHandler(handler);
        logger.debug("创建连接成功，连接信息：{}", JSONUtil.toJsonStr(driverContext));
    }


    /**
     * 创建连接并实现handler
     *
     * @param driverContext
     */
    public static void destroy(ThingsDriverContext driverContext) {
        ThingsConnectionHandler handler = ConnectManager.getHandler(driverContext.getThingsId());
        handler.getThingsConnection().disconnect();
    }
}
