package com.elco.eeds.agent.sdk.core.connect;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesContext;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import com.elco.eeds.agent.sdk.core.connect.status.ConnectionStatus;
import com.elco.eeds.agent.sdk.core.parsing.DataParsing;
import com.elco.eeds.agent.sdk.transfer.beans.message.order.OrderPropertiesValue;
import com.elco.eeds.agent.sdk.transfer.beans.things.ThingsDriverContext;
import com.elco.eeds.agent.sdk.transfer.handler.properties.VirtualPropertiesHandle;
import com.elco.eeds.agent.sdk.transfer.service.data.RealTimePropertiesValueService;
import com.elco.eeds.agent.sdk.transfer.service.things.ThingsConnectStatusMqService;
import com.elco.eeds.agent.sdk.transfer.service.things.ThingsSyncServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author ：ytl
 * @date ：Created in 2022/12/2 15:25
 * @description： 数据源连接抽象类，需用户自己实现该客户端的连接方法，然后交由SDK对连接进行管理
 */
public abstract class ThingsConnectionHandler<T, M extends DataParsing> {

    public static final Logger logger = LoggerFactory.getLogger(ThingsConnectionHandler.class);

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(4);

    public static Map<String, ScheduledFuture> scheduledTaskMap = new ConcurrentHashMap();

    /**
     * 1：虚拟变量
     */
    private final static String REAL = "1";
    /**
     * 数据源连接接口
     */
    private ThingsConnection thingsConnection;

    /**
     * 数据源的连接状态
     */
    private ConnectionStatus connectionStatus;
    /**
     * 数据源ID
     */
    private String thingsId;
    /**
     * 数据源连接信息上下文
     */
    private ThingsDriverContext context;

    /**
     *
     */
    private String handlerName;

    /**
     * 数据源连接的client
     */
    private T master;

    /**
     * 数据解析实现类
     */
    private M parsing;
    
    public static int num = 0;


    public ThingsConnectionHandler() {
        attach();
    }

    public void attach() {

        //1、返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type
        Type genType = getClass().getGenericSuperclass();
        //2、泛型参数
        Type[] types = ((ParameterizedType) genType).getActualTypeArguments();
        //3、因为BasePresenter 有两个泛型 数组有两个
        try {
            //
            parsing = (M) ((Class) types[1]).newInstance();
            //这里需要强转得到的是实体类类路径
            //如果types[1].getClass().newInstance();并不行，得到的是泛型类型
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 被动获取数据方法，抽象方法，需用户实现，SDK定时调用该方法
     * @param properties 变量点位集合
     */
    public abstract String read(List<PropertiesContext> properties);


    /**
     * 下发指令
     * @param propertiesValueList
     * @param msgSeqNo
     */
    public abstract boolean write(List<OrderPropertiesValue> propertiesValueList, String msgSeqNo);


    /**
     * 执行模板方法
     * 主动，被动获取原始报文后都需调用该方法，传入原始报文，由SDK调用解析方法，解析出点位数据
     * @param thingsId 数据源ID
     * @param msg 原始报文
     * @param collectTime 采集时间戳
     */
    public void execute(String thingsId, String msg, Long collectTime) {
        long startTime = System.currentTimeMillis();
        List<PropertiesContext> propertiesContextList = ThingsSyncServiceImpl.getThingsPropertiesContextList(thingsId);
        if(CollectionUtil.isNotEmpty(propertiesContextList)){
            List<PropertiesValue> valueList = this.getParsing()
                    .parsing(this.context, ThingsSyncServiceImpl.getThingsPropertiesContextList(thingsId), msg);
            valueList.stream().forEach(pv -> {
                pv.setTimestamp(collectTime);
                pv.setIsVirtual(REAL);
            });
            VirtualPropertiesHandle.creatVirtualProperties(propertiesContextList, valueList, collectTime);
            RealTimePropertiesValueService.recRealTimePropertiesValue(msg, thingsId, collectTime, valueList);
            long time = System.currentTimeMillis()-startTime;
            num++;
            logger.info("数据处理耗时，time:{},推送数据量:{}",time,num);
    
        }
    }

    public ThingsConnection getThingsConnection() {
        return thingsConnection;
    }

    public void setThingsConnection(ThingsConnection thingsConnection) {
        this.thingsConnection = thingsConnection;
    }


    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(ConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
    }


    public String getThingsId() {
        return thingsId;
    }

    public void setThingsId(String thingsId) {
        this.thingsId = thingsId;
    }


    public ThingsDriverContext getContext() {
        return context;
    }

    public void setContext(ThingsDriverContext context) {
        this.context = context;
    }


    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }


    public T getMaster() {
        return master;
    }

    public void setMaster(T master) {
        this.master = master;
    }


    public M getParsing() {
        return parsing;
    }

    public void setParsing(M parsing) {
        this.parsing = parsing;
    }

    public int schedule() {
        return 0;
    }

//    public void command(EedsThings things,String command){
//        this.write(things,this.parsing.parsingCommand(command));
//    }

    /**
     * 执行重连
     */
    public void reconnect() {
        ThingsConnectStatusMqService.sendDisConnectMsg(this.getThingsId());
        Integer reconnectNum = Integer.valueOf(this.context.getReconnectNum());
        Long reconnectInterval = Long.valueOf(this.context.getReconnectInterval()) * 1000;
        ThingsConnection connection = this.getThingsConnection();
        ThingsConnectionHandler handler = this;
//        if (!handler.getConnectionStatus().equals(ConnectionStatus.CONNECTED) || ObjectUtil.isEmpty(connection)) {
        if (handler.getConnectionStatus().equals(ConnectionStatus.DISCONNECT) || ObjectUtil.isEmpty(connection)) {

            ThingsDriverContext info = this.getContext();
            ScheduledFuture<?> future = scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                Integer num = 1;

                @Override
                public void run() {
                    try {
                        if (num <= reconnectNum) {
//                            if (!handler.getConnectionStatus().equals(ConnectionStatus.CONNECTED) || ObjectUtil.isEmpty(connection)) {
//                                scheduledTaskMap.get(thingsId).cancel(true);
//                            } else {
//                                connection.connect(info);
//                            }
                            if(!handler.getConnectionStatus().equals(ConnectionStatus.DISCONNECT) || ObjectUtil.isEmpty(connection)) {
                                scheduledTaskMap.get(thingsId).cancel(true);
                                logger.debug("删除定时任务：{}", thingsId);
                            }else {
                                if (!connection.connect(info)) {
                                    num++;
                                }
                            }

                        }else {
                            scheduledTaskMap.get(thingsId).cancel(true);
                            logger.debug("设定次数：{}，已抵达，删除定时任务：{}", reconnectNum, thingsId);
                        }
                    } catch (Throwable e) {
                        num++;
                        logger.error("自定义重连失败，失败原因，msg:{}", e.getMessage());
                    }
                }
            }, 1000, reconnectInterval, TimeUnit.MILLISECONDS);
            scheduledTaskMap.put(thingsId, future);

        }

    }

}
