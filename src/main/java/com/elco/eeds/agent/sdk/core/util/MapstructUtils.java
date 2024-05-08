package com.elco.eeds.agent.sdk.core.util;

import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesContext;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesEvent;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import com.elco.eeds.agent.sdk.core.mapstruct.DataCountMessageMapper;
import com.elco.eeds.agent.sdk.core.mapstruct.DataSyncMapper;
import com.elco.eeds.agent.sdk.core.mapstruct.PropertiesMapper;
import com.elco.eeds.agent.sdk.core.mapstruct.PropertiesValueMapper;
import com.elco.eeds.agent.sdk.core.mapstruct.RealTimePVMapper;
import com.elco.eeds.agent.sdk.core.mapstruct.ThingsSyncMapper;
import com.elco.eeds.agent.sdk.transfer.beans.data.count.PostDataCount;
import com.elco.eeds.agent.sdk.transfer.beans.data.sync.DataSyncServerRequest;
import com.elco.eeds.agent.sdk.transfer.beans.message.data.count.post.SubDataCountMessage;
import com.elco.eeds.agent.sdk.transfer.beans.message.data.realTime.SubDataRealTimePropertiesMessage;
import com.elco.eeds.agent.sdk.transfer.beans.message.data.sync.data.SubDataSyncPropertiesValueMessage;
import com.elco.eeds.agent.sdk.transfer.beans.things.EedsProperties;
import com.elco.eeds.agent.sdk.transfer.beans.things.EedsThings;
import com.elco.eeds.agent.sdk.transfer.beans.things.ThingsDriverContext;
import com.elco.storage.domain.PropertiesData;
import java.util.List;

/**
 * @ClassName MapstructUtils
 * @Description Mapstruct工具包
 * @Author OuYang
 * @Date 2024/4/9 9:06
 * @Version 1.0
 */
public class MapstructUtils {

  /**
   * value转data
   *
   * @param pvs
   * @return
   */
  public static List<PropertiesData> valueToData(List<PropertiesValue> pvs) {
    List<PropertiesData> datas = PropertiesValueMapper.INSTANCE.valuesToDatas(pvs);
    return datas;
  }

  /**
   * context转PV
   * @param context
   * @return
   */
  public static  PropertiesValue contextToVirtualPV(PropertiesContext context){
    return PropertiesValueMapper.INSTANCE.contextToVirtualPV(context);
  }

  /**
   * 同步数据源复制到本地
   *
   * @param syncThings
   * @return
   */
  public static ThingsDriverContext syncThingsToThingsDriver(EedsThings syncThings) {
    ThingsDriverContext thingsDriverContext = ThingsSyncMapper.INSTANCE.syncThingsToThingsDriver(
        syncThings);
    return thingsDriverContext;
  }

  /**
   * EedsThings复制
   * @param things
   * @return
   */
  public static   EedsThings thingsToThings(EedsThings things){
    return ThingsSyncMapper.INSTANCE.thingsToThings(things);
  }

  /**
   * 同步变量复制到context
   *
   * @param syncProperties
   * @return
   */
  public static PropertiesContext syncPropToContext(EedsProperties syncProperties, String agentId,
      String thingsId, String thingsType) {
    PropertiesContext propertiesContext = PropertiesMapper.INSTANCE.syncPropToContext(
        syncProperties, agentId, thingsId, thingsType);
    return propertiesContext;
  }

  /**
   * 同步变量复制到event
   *
   * @param properties
   * @param thingsId
   * @return
   */
  public static PropertiesEvent syncPropToEvent(EedsProperties properties, String thingsId) {
    PropertiesEvent propertiesEvent = PropertiesMapper.INSTANCE.syncPropToEvent(properties,
        thingsId);
    return propertiesEvent;
  }

  /**
   * 统计记录转报文
   *
   * @param dataCount
   * @return
   */
  public static SubDataCountMessage dataCountToMsg(PostDataCount dataCount) {
    SubDataCountMessage subMsg = DataCountMessageMapper.INSTANCE.dataCountToMsg(
        dataCount);
    return subMsg;
  }

  /**
   * 数据同步对象转ThingsContext对象
   *
   * @param request
   * @return
   */
  public static ThingsDriverContext requestToThingsContext(DataSyncServerRequest request) {
    return DataSyncMapper.INSTANCE.requestToThingsContext(request);
  }

  /**
   * 实时数据对象转Msg
   * @param pv
   * @return
   */
  public static SubDataRealTimePropertiesMessage pvToMsg(PropertiesValue pv){
    return RealTimePVMapper.INSTANCE.pvToMsg(pv);
  }

  /**
   * 实时数据转对象
   * @param pv
   * @return
   */
  public static SubDataSyncPropertiesValueMessage pvToSyncMsg(PropertiesValue pv){
    return RealTimePVMapper.INSTANCE.pvToSyncMsg(pv);
  }


}
