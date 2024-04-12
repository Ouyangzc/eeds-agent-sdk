package com.elco.eeds.agent.sdk.transfer.service.data;

import cn.hutool.json.JSONUtil;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesContext;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import com.elco.eeds.agent.sdk.core.disruptor.RealTimeDataDisruptorServer;
import com.elco.eeds.agent.sdk.core.util.MqPluginUtils;
import com.elco.eeds.agent.sdk.core.util.RealTimeDataMessageFileUtils;
import com.elco.eeds.agent.sdk.transfer.beans.data.OriginalPropertiesValueMessage;
import com.elco.eeds.agent.sdk.transfer.beans.data.count.ThingsDataCount;
import com.elco.eeds.agent.sdk.transfer.beans.message.data.realTime.DataRealTimePropertiesMessage;
import com.elco.eeds.agent.sdk.transfer.handler.properties.VirtualPropertiesHandle;
import com.elco.eeds.agent.sdk.transfer.service.data.count.DataCountServiceImpl;
import com.elco.eeds.agent.sdk.transfer.service.data.realtime.AbstractRealTimeService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName RealTimePropertiesValueService
 * @Description 实时数据类
 * @Author OUYANG
 * @Date 2022/12/20 13:39
 */
public class RealTimePropertiesValueService extends AbstractRealTimeService {

  private static Logger logger = LoggerFactory.getLogger(RealTimePropertiesValueService.class);

  private static RealTimePropertiesValueService instance;

  public static RealTimePropertiesValueService getInstance() {
    if (instance == null) {
      synchronized (RealTimePropertiesValueService.class) {
        if (instance == null) {
          instance = new RealTimePropertiesValueService();
        }
      }
    }
    return instance;
  }

  @Override
  protected void pushDataToLocally(List<PropertiesValue> propertiesValueList) {
    RealTimeDataDisruptorServer disruptorServer = RealTimeDataDisruptorServer.getInstance();
    disruptorServer.sendData(propertiesValueList);
  }

  @Override
  protected void pushDataToMQ(String agentId, String thingsId,
      List<PropertiesValue> propertiesValueList, Long collectTime) {
    //MQ推送数据
    String postMsg = DataRealTimePropertiesMessage.getMessage(propertiesValueList);
    String topic = DataRealTimePropertiesMessage.getTopic(agentId, thingsId);
    logger.debug("实时数据推送：采集时间:{} topic:{}, msg:{}", collectTime, topic, postMsg);
    MqPluginUtils.sendRealTimeValueMsg(topic, postMsg);
  }

  @Override
  protected void noNeedLocalCache(List<PropertiesContext> propertiesContextList,
      List<PropertiesValue> propertiesValueList, Long collectTime) {
    // 计算虚拟变量
    VirtualPropertiesHandle.creatVirtualProperties(propertiesContextList, propertiesValueList,
        collectTime);
  }

  @Override
  protected void localCache(String agentId, String thingsId, String message,
      List<PropertiesContext> propertiesContextList,
      List<PropertiesValue> propertiesValueList, Long collectTime) {
    //存储原始数据
    OriginalPropertiesValueMessage originalPropertiesValueMessage = new OriginalPropertiesValueMessage();
    originalPropertiesValueMessage.setCollectTime(collectTime);
    originalPropertiesValueMessage.setMessage(message);
    RealTimeDataMessageFileUtils.writeAppend(thingsId,
        JSONUtil.toJsonStr(originalPropertiesValueMessage), collectTime);

    // 计算虚拟变量
    VirtualPropertiesHandle.creatVirtualProperties(propertiesContextList, propertiesValueList,
        collectTime);

    //调用统计接口
    ThingsDataCount dataCount = new ThingsDataCount();
    dataCount.setThingsId(thingsId);
    dataCount.setSize(propertiesValueList.size());
    dataCount.setCollectTime(collectTime);
    dataCount.setStartTime(collectTime);
    dataCount.setEndTime(collectTime);
    DataCountServiceImpl.recRealTimeData(agentId, collectTime, dataCount);
  }
}
