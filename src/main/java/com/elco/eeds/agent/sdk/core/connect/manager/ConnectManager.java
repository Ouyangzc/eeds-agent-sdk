package com.elco.eeds.agent.sdk.core.connect.manager;


import com.elco.eeds.agent.sdk.core.connect.ThingsConnection;
import com.elco.eeds.agent.sdk.core.connect.ThingsConnectionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ：ytl
 * @date ：Created in 2022/12/1 15:55
 * @description： 连接管理员
 */
public class ConnectManager {

    private static ConcurrentHashMap<String, ThingsConnection> CONNECTION_MAP = new ConcurrentHashMap<String, ThingsConnection>(16);


    private static ConcurrentHashMap<String, ThingsConnectionHandler> CONNECTION_HANDLER_MAP = new ConcurrentHashMap<String, ThingsConnectionHandler>(256);


    /**
     * 添加数据源连接实例
     *
     * @param key     数据源ID
     * @param handler
     */
    public static void addHandler(String key, ThingsConnectionHandler handler) {
        CONNECTION_HANDLER_MAP.put(key, handler);
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
}
