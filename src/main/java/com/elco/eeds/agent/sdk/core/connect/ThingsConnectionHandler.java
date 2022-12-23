package com.elco.eeds.agent.sdk.core.connect;


import cn.hutool.core.util.ObjectUtil;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesContext;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import com.elco.eeds.agent.sdk.core.connect.status.ConnectionStatus;
import com.elco.eeds.agent.sdk.core.parsing.DataParsing;
import com.elco.eeds.agent.sdk.transfer.beans.message.order.OrderPropertiesValue;
import com.elco.eeds.agent.sdk.transfer.beans.things.ThingsDriverContext;
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
 * @description：
 */
public abstract class ThingsConnectionHandler<T, M extends DataParsing> {

    public static final Logger logger = LoggerFactory.getLogger(ThingsConnectionHandler.class);

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(4);

    public static Map<String, ScheduledFuture> scheduledTaskMap = new ConcurrentHashMap();
    private ThingsConnection thingsConnection;

    public ThingsConnection getThingsConnection() {
        return thingsConnection;
    }

    public void setThingsConnection(ThingsConnection thingsConnection) {
        this.thingsConnection = thingsConnection;
    }

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
    private ConnectionStatus connectionStatus;

    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(ConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
    }


    private String thingsId;

    public String getThingsId() {
        return thingsId;
    }

    public void setThingsId(String thingsId) {
        this.thingsId = thingsId;
    }

    private ThingsDriverContext context;

    public ThingsDriverContext getContext() {
        return context;
    }

    public void setContext(ThingsDriverContext context) {
        this.context = context;
    }


    private String handlerName;

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


    private T master;

    private M parsing;

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


    public abstract void read(List<PropertiesContext> properties);


    public abstract void write(List<OrderPropertiesValue> propertiesValueList);

    /**
     * 执行模板方法
     *
     * @param thingsId
     * @param msg
     * @param collectTime
     */
    public void execute(String thingsId, String msg, Long collectTime) {
        List<PropertiesValue> valueList = this.getParsing()
                .parsing(this.context, ThingsSyncServiceImpl.getThingsPropertiesContextList(thingsId), msg);
        RealTimePropertiesValueService.recRealTimePropertiesValue(msg, thingsId, collectTime, valueList);
    }

    /**
     * 执行重连
     */
    public void reconnect() {
        ThingsConnectStatusMqService.sendDisConnectMsg(this.getThingsId());
        Integer reconnectNum = Integer.valueOf(this.context.getReconnectNum());
        Long reconnectInterval = Long.valueOf(this.context.getReconnectInterval()) * 1000;
        ThingsConnection connection = this.getThingsConnection();
        ThingsConnectionHandler  handler = this;
        if (!handler.getConnectionStatus().equals(ConnectionStatus.CONNECTED) || ObjectUtil.isEmpty(connection)) {

            ThingsDriverContext info = this.getContext();
            ScheduledFuture<?> future = scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                Integer num = 1;

                @Override
                public void run() {
                    try {
                        if (num <= reconnectNum) {
                            if (!handler.getConnectionStatus().equals(ConnectionStatus.CONNECTED) || ObjectUtil.isEmpty(connection)) {
                                scheduledTaskMap.get(thingsId).cancel(true);
                            } else {
                                connection.connect(info);
                            }
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
