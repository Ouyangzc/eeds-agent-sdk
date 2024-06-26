package com.elco.eeds.agent.sdk.core.connect.manager;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONUtil;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesEvent;
import com.elco.eeds.agent.sdk.core.common.constant.ConstantCommon;
import com.elco.eeds.agent.sdk.core.common.constant.ReadTypeEnums;
import com.elco.eeds.agent.sdk.core.connect.ThingsConnection;
import com.elco.eeds.agent.sdk.core.connect.ThingsConnectionHandler;
import com.elco.eeds.agent.sdk.core.connect.status.ConnectionStatus;
import com.elco.eeds.agent.sdk.core.exception.EedsConnectException;
import com.elco.eeds.agent.sdk.core.quartz.QuartzManager;
import com.elco.eeds.agent.sdk.transfer.beans.things.ThingsDriverContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ：ytl
 * @date ：Created in 2022/12/1 15:55
 * @description： 连接管理员
 */
public class ConnectManager {

    public static final Logger logger = LoggerFactory.getLogger(ConnectManager.class);
    private static ConcurrentHashMap<String, String> CONNECTION_MAP = new ConcurrentHashMap<>(16);


    private static ConcurrentHashMap<String, ThingsConnectionHandler> CONNECTION_HANDLER_MAP = new ConcurrentHashMap<String, ThingsConnectionHandler>(
            256);

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
     * @param thingsId 数据源ID
     */
    public static ThingsConnectionHandler getHandler(String thingsId) {
        ThingsConnectionHandler handler = CONNECTION_HANDLER_MAP.get(thingsId);
        if (ObjectUtil.isEmpty(handler)) {
            return null;
        }
        return handler;
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
     * @param classPath
     */
    public static void addConnection(String key, String classPath) {
        CONNECTION_MAP.put(key, classPath);
    }

    /**
     * 获取数据源连接
     *
     * @param key
     * @return
     */
    public static ThingsConnection getConnection(String key) {
        return ReflectUtil.newInstance(CONNECTION_MAP.get(key));
    }

    /**
     * 删除数据源连接
     *
     * @param thingsId
     */
    public static void delConnection(String thingsId) {
        ThingsConnectionHandler handler = getHandler(thingsId);
        if (null != handler) {
            ThingsConnectionHandler.ThingsStatus thingsStatus = handler.new ThingsStatus();
            try {
                handler.getThingsConnection().disconnect();
                thingsStatus.setValue(handler, ConnectionStatus.DISCONNECT);
            }catch (Exception e) {
                thingsStatus.setValue(handler, ConnectionStatus.DISCONNECT, e.getMessage());
            }
            CONNECTION_HANDLER_MAP.remove(thingsId);
        }
    }

    /**
     * 创建连接并实现handler
     *
     * @param driverContext
     */
    public static void create(ThingsDriverContext driverContext, String connectKey) {
        logger.info("连接协议：{}", connectKey);
        logger.info("开始创建连接，连接信息：{}", driverContext);
        if (ObjectUtil.isEmpty(driverContext)){
            logger.info("暂无数据源信息：无法创建连接");
            return;
        }
        ThingsConnection connection = ConnectManager.getConnection(connectKey);
        ThingsConnectionHandler handler = (ThingsConnectionHandler) connection;
        handler.setContext(driverContext);
        handler.setThingsConnection(connection);
        handler.setThingsId(driverContext.getThingsId());
        ThingsConnectionHandler.ThingsStatus thingsStatus = handler.new ThingsStatus();
        thingsStatus.setValue(handler, ConnectionStatus.CONNECTING);
        try {
            if (!connection.connect(driverContext)) {
                logger.error("创建连接失败，客户端创建连接失败,连接信息：{}", JSONUtil.toJsonStr(driverContext));
                thingsStatus.setValue(handler, ConnectionStatus.DISCONNECT);
                return;
            }
        }catch (EedsConnectException e) {
            logger.error("创建连接失败,发生可知异常,连接信息：{}", JSONUtil.toJsonStr(driverContext));
            thingsStatus.setValue(handler, ConnectionStatus.DISCONNECT, e.getMessage());
            return;
        }catch (Exception e) {
            logger.error("创建连接失败，发生未知异常,连接信息：{}", JSONUtil.toJsonStr(driverContext));
            thingsStatus.setValue(handler, ConnectionStatus.DISCONNECT, e.getMessage());
            return;
        }
        ConnectManager.addHandler(handler);
        logger.info("创建连接成功，连接信息：{}", driverContext);
        thingsStatus.setValue(handler, ConnectionStatus.CONNECTED);
        if (!connection.getReadType().equals(ReadTypeEnums.INITIATIVE)) {
            String corn = null;
            String samplingInterval = driverContext.getExtraMap().get(ConstantCommon.SamplingInterval);
            if (ObjectUtil.isNotEmpty(samplingInterval)) {
                corn = samplingInterval;
            }else {
                corn = connection.getCorn();
            }
            try {
                HashMap<String, Object> extraMap = new HashMap<>();
                extraMap.put("handler",ConnectManager.getHandler(handler.getThingsId()));
                QuartzManager.addRealTimePropertiesJob(handler.getThingsId(),connection.getReadType(),corn,extraMap);
            }catch (Exception e) {
                logger.error("添加定时任务失败，连接信息：{}", JSONUtil.toJsonStr(driverContext));
            }

        }
    }


    /**
     * 创建连接并实现handler
     *
     * @param driverContext
     */
    public static void recreate(ThingsDriverContext driverContext, String connectKey) {
        delConnection(driverContext.getThingsId());

        create(driverContext, connectKey);
    }


    /**
     * 创建连接并实现handler
     *
     * @param driverContext
     */
    public static void destroy(ThingsDriverContext driverContext) {
        ThingsConnectionHandler handler = ConnectManager.getHandler(driverContext.getThingsId());
        if (null != handler) {
            handler.getThingsConnection().disconnect();
        }
    }

    public static void sendPropertiesEventNotify(String thingsId, PropertiesEvent propertiesEvent) {
        if (propertiesEvent.getIsVirtual() == 1) {

            ThingsConnectionHandler handler = getHandler(thingsId);
            if (null != handler) {
                try {
                    logger.info("发送变量变动通知,thingsId:{}", thingsId);
                    propertiesEvent.setThingsId(thingsId);
                    handler.getThingsConnection().propertiesEventNotify(propertiesEvent);
                }catch (Exception e) {
                    logger.error("发送变量变动通知异常，异常信息:", e);
                }
            }

        }
    }
}
