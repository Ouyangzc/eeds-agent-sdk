package com.elco.eeds.agent.sdk.core.connect;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesContext;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import com.elco.eeds.agent.sdk.core.connect.status.ConnectionStatus;
import com.elco.eeds.agent.sdk.core.exception.EedsConnectException;
import com.elco.eeds.agent.sdk.core.parsing.DataParsing;
import com.elco.eeds.agent.sdk.transfer.beans.message.cmd.CmdResult;
import com.elco.eeds.agent.sdk.transfer.beans.message.cmd.SubCmdRequestMessage;
import com.elco.eeds.agent.sdk.transfer.beans.message.order.OrderPropertiesValue;
import com.elco.eeds.agent.sdk.transfer.beans.things.ThingsDriverContext;
import com.elco.eeds.agent.sdk.transfer.service.data.RealTimePropertiesValueService;
import com.elco.eeds.agent.sdk.transfer.service.things.ThingsConnectStatusMqService;
import com.elco.eeds.agent.sdk.transfer.service.things.ThingsSyncNewServiceImpl;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ：ytl
 * @date ：Created in 2022/12/2 15:25
 * @description： 数据源连接抽象类，需用户自己实现该客户端的连接方法，然后交由SDK对连接进行管理
 */
public abstract class ThingsConnectionHandler<T, M extends DataParsing> implements Serializable {

  public static final Logger logger = LoggerFactory.getLogger(ThingsConnectionHandler.class);

  private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(4);

  public static Map<String, ScheduledFuture> scheduledTaskMap = new ConcurrentHashMap();

  /**
   * 1：虚拟变量
   */
  private final static int REAL = 1;
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
   *
   * @param properties 变量点位集合
   */
  public abstract String read(List<PropertiesContext> properties);


  /**
   * 下发指令
   *
   * @param propertiesValueList
   * @param msgSeqNo
   */
  public abstract boolean write(List<OrderPropertiesValue> propertiesValueList, String msgSeqNo);


  /**
   * @param cmdMsg
   * @return
   * @throws RuntimeException
   */
  public abstract CmdResult cmdWrite(SubCmdRequestMessage cmdMsg) throws RuntimeException;

  /**
   * 校验下发指令是否合法
   *
   * @param inputData 下发内容
   * @return true 校验通过 false:检验不通过
   */
  public abstract boolean cmdCheck(String inputData);


  /**
   * 执行模板方法 主动，被动获取原始报文后都需调用该方法，传入原始报文，由SDK调用解析方法，解析出点位数据
   *
   * @param thingsId    数据源ID
   * @param msg         原始报文
   * @param collectTime 采集时间戳
   */
  public void execute(String thingsId, String msg, Long collectTime) {
    long startTime = System.currentTimeMillis();
    List<PropertiesContext> propertiesContextList = ThingsSyncNewServiceImpl
        .getThingsPropertiesContextList(thingsId);
    if (CollUtil.isNotEmpty(propertiesContextList)) {
      List<PropertiesValue> valueList = this.getParsing()
          .parsing(this.context, ThingsSyncNewServiceImpl.getThingsPropertiesContextList(thingsId),
              msg);
      valueList.stream().forEach(pv -> {
        pv.setTimestamp(collectTime);
        pv.setIsVirtual(REAL);
      });
      RealTimePropertiesValueService realTimePropertiesValueService = RealTimePropertiesValueService.getInstance();
      realTimePropertiesValueService.process(msg, thingsId, collectTime, valueList,
          propertiesContextList);
      long time = System.currentTimeMillis() - startTime;
      num++;
      logger.debug("数据处理耗时，time:{},推送数据量:{}", time, num);
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
    Integer reconnectNum = Integer.valueOf(this.context.getReconnectNum());
    Long reconnectInterval = Long.valueOf(this.context.getReconnectInterval()) * 1000;
    ThingsConnection connection = this.getThingsConnection();
    ThingsConnectionHandler handler = this;
    //设置为断开状态
    ThingsConnectionHandler.ThingsStatus thingsStatus = handler.new ThingsStatus();
    thingsStatus.setValue(handler, ConnectionStatus.DISCONNECT);
    String thingsId = this.getThingsId();
    synchronized (thingsId) {
      if (ObjectUtil.isEmpty(scheduledTaskMap.get(this.thingsId)) && (
          this.getConnectionStatus().equals(ConnectionStatus.DISCONNECT) || ObjectUtil.isEmpty(
              connection))) {
        ScheduledFuture<?> future = scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
          Integer num = 1;

          @Override
          public void run() {
            try {
              if (num <= reconnectNum) {
                if (!handler.getConnectionStatus().equals(ConnectionStatus.DISCONNECT) || ObjectUtil
                    .isEmpty(connection)) {
                  scheduledTaskMap.get(thingsId).cancel(true);
                  logger.debug("删除定时任务：{}", thingsId);
                } else {
                  ThingsDriverContext info = ThingsSyncNewServiceImpl.THINGS_DRIVER_CONTEXT_MAP.get(
                      getThingsId());
                  //连接中
                  thingsStatus.setValue(handler, ConnectionStatus.CONNECTING);
//                                    Optional<PropertiesContext> optional = ThingsSyncNewServiceImpl.PROPERTIES_CONTEXT_MAP.values().stream().filter(p -> p.getThingsId().equals(context.getThingsId())).findAny();
//                                    if (optional.isPresent()) {
                  try {
                    connection.connect(info);
                  } catch (EedsConnectException e) {
                    logger.error("创建连接失败,发生可知异常,连接信息：{}", JSONUtil.toJsonStr(info));
                    thingsStatus.setValue(handler, ConnectionStatus.DISCONNECT, e.getMessage());
                    num++;
                    logger.info("自定义重连，尝试连接，连接次数:{}", num - 1);
                    return;
                  } catch (Exception e) {
                    logger.error("创建连接失败，发生未知异常,连接信息：{}", JSONUtil.toJsonStr(info));
                    thingsStatus.setValue(handler, ConnectionStatus.DISCONNECT, e.getMessage());
                    num++;
                    logger.info("自定义重连，尝试连接，连接次数:{}", num - 1);
                    return;
                  }
                  ThingsConnectionHandler.ThingsStatus thingsStatus = handler.new ThingsStatus();
                  thingsStatus.setValue(handler, ConnectionStatus.CONNECTED);
                  scheduledTaskMap.get(thingsId).cancel(true);
                  scheduledTaskMap.remove(thingsId);
                  logger.info("数据源重连成功,删除定时任务：数据源ID:{}", thingsId);
//                                    } else {
//                                        //断开连接
//                                        thingsStatus.setValue(handler, ConnectionStatus.DISCONNECT);
//                                        num++;
//                                        logger.info("自定义重连，尝试连接，连接次数:{}", num - 1);
//                                    }
                }

              } else {
                scheduledTaskMap.get(thingsId).cancel(true);
                scheduledTaskMap.remove(thingsId);
                logger.debug("设定次数：{}，已抵达，删除定时任务：{}", reconnectNum, thingsId);
              }
            } catch (Throwable e) {
              num++;
              logger.info("自定义重连失败，失败原因，msg:{}", e.getMessage());
            }
          }
        }, 1000, reconnectInterval, TimeUnit.MILLISECONDS);
        scheduledTaskMap.put(thingsId, future);
      }
    }
  }

  public class ThingsStatus {

    /**
     * 设置数据源的状态
     *
     * @param handler
     * @param connectionStatus
     */
    public void setValue(ThingsConnectionHandler handler, ConnectionStatus connectionStatus) {
      handler.connectionStatus = connectionStatus;
      logger.info("客户端状态改变：数据源ID:{}，连接状态:{}", handler.thingsId, connectionStatus);
      if (connectionStatus.equals(ConnectionStatus.CONNECTED)) {
        ThingsConnectStatusMqService.sendConnectMsg(thingsId);
      } else if (connectionStatus.equals(ConnectionStatus.CONNECTING)) {
        ThingsConnectStatusMqService.sendConnectingMsg(thingsId);
      } else {
        ThingsConnectStatusMqService.sendDisConnectMsg(thingsId);
      }
    }

    public void setValue(ThingsConnectionHandler handler, ConnectionStatus connectionStatus,
        String msg) {
      handler.connectionStatus = connectionStatus;
      logger.info("客户端状态改变：数据源ID:{}，连接状态:{}", handler.thingsId, connectionStatus);
      if (connectionStatus.equals(ConnectionStatus.CONNECTED)) {
        ThingsConnectStatusMqService.sendConnectMsg(thingsId);
      } else if (connectionStatus.equals(ConnectionStatus.CONNECTING)) {
        ThingsConnectStatusMqService.sendConnectingMsg(thingsId);
      } else {
        ThingsConnectStatusMqService.sendDisConnectMsg(thingsId, msg);
      }
    }
  }
}
