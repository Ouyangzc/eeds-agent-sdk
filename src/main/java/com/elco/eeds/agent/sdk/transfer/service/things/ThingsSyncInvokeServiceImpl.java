package com.elco.eeds.agent.sdk.transfer.service.things;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentBaseInfo;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesContext;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesEvent;
import com.elco.eeds.agent.sdk.core.common.constant.ConstantThings;
import com.elco.eeds.agent.sdk.core.connect.ThingsConnection;
import com.elco.eeds.agent.sdk.core.connect.ThingsConnectionHandler;
import com.elco.eeds.agent.sdk.core.connect.manager.ConnectManager;
import com.elco.eeds.agent.sdk.core.connect.status.ConnectionStatus;
import com.elco.eeds.agent.sdk.core.start.AgentStartProperties;
import com.elco.eeds.agent.sdk.core.util.MapstructUtils;
import com.elco.eeds.agent.sdk.transfer.beans.message.things.SubThingsSyncIncrMessage;
import com.elco.eeds.agent.sdk.transfer.beans.things.EedsProperties;
import com.elco.eeds.agent.sdk.transfer.beans.things.EedsThings;
import com.elco.eeds.agent.sdk.transfer.beans.things.ThingsDriverContext;
import com.elco.eeds.agent.sdk.transfer.beans.things.ThingsSyncRequest;
import com.elco.eeds.agent.sdk.transfer.service.agent.AgentRequestInvokeService;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName ThingsSyncInvokeServiceImpl
 * @Description 本地调用
 * @Author OuYang
 * @Date 2024/4/1 15:16
 * @Version 1.0
 */
public class ThingsSyncInvokeServiceImpl implements ThingsSyncService, Serializable {

  private static final Logger logger = LoggerFactory.getLogger(ThingsSyncNewServiceImpl.class);

  private ThingsPropertiesService thingsService;

  public static final Map<String, PropertiesContext> PROPERTIES_CONTEXT_MAP = new ConcurrentHashMap<>();
  public static final Map<String, ThingsDriverContext> THINGS_DRIVER_CONTEXT_MAP = new ConcurrentHashMap<>();


  private AgentRequestInvokeService invokeService;

  public ThingsSyncInvokeServiceImpl(ThingsPropertiesService thingsService,
      AgentRequestInvokeService invokeService) {
    this.thingsService = thingsService;
    this.invokeService = invokeService;
  }

  @Override
  public void setupSyncThings() {
    Long thingsChangeTime = thingsService.getThingsChangeTime();
    AgentBaseInfo agentBaseInfo = Agent.getInstance().getAgentBaseInfo();
    String agentId = agentBaseInfo.getAgentId();
    String token = agentBaseInfo.getToken();
    ThingsSyncRequest thingsRequest = new ThingsSyncRequest();
    thingsRequest.setAgentId(Long.valueOf(agentId));
    thingsRequest.setLastTime(thingsChangeTime);
    List<EedsThings> syncDataList = this.getSetupSyncData(thingsRequest, token);
    handleSyncThingsDataList(syncDataList);
    //重新加载数据源文件
    this.loadThingDriver();
  }


  @Override
  public void incrSyncThings(SubThingsSyncIncrMessage message) {
    Long thingsChangeTime = thingsService.getThingsChangeTime();
    AgentBaseInfo agentBaseInfo = Agent.getInstance().getAgentBaseInfo();
    String agentId = agentBaseInfo.getAgentId();
    String token = agentBaseInfo.getToken();
    ThingsSyncRequest thingsRequest = new ThingsSyncRequest();
    thingsRequest.setTaskId(message.getTaskId());
    thingsRequest.setTableSearch(message.getTableSearch());
    thingsRequest.setAgentId(Long.valueOf(agentId));
    thingsRequest.setLastTime(thingsChangeTime);
    List<EedsThings> syncDataList = this.getSetupSyncData(thingsRequest, token);
    this.handleIncrSyncThingsDataList(syncDataList);
  }


  public List<EedsThings> getSetupSyncData(ThingsSyncRequest request, String token) {
    String datas = invokeService.getThingsSyncInfo(request);
    if (datas != null) {
      List<EedsThings> edgeThings = JSON.parseArray(datas, EedsThings.class);
      return edgeThings;
    }
    return null;
  }

  private void handleSyncThingsDataList(List<EedsThings> syncDataList) {
    this.convertData(syncDataList);
    String localThingsData;
    try {
      localThingsData = thingsService.getThingsFile();
    } catch (IOException e) {
      logger.error("获取本地数据源文件失败");
      return;
    }
    for (EedsThings syncThings : syncDataList) {
      String operatorType = syncThings.getOperatorType();
      switch (operatorType) {
        case ConstantThings.P_OPERATOR_TYPE_ADD:
          handleAddThings(syncThings, localThingsData);
          break;
        case ConstantThings.P_OPERATOR_TYPE_EDIT:
          handleEditThings(syncThings, localThingsData);
          break;
        case ConstantThings.P_OPERATOR_TYPE_DEL:
          handleDelThings(syncThings, localThingsData);
          break;
        case ConstantThings.P_OPERATOR_TYPE_UNCHANGE:
          handleUnchangeThings(syncThings, localThingsData);
          break;
      }
    }
    thingsService.saveToFile(JSONUtil.toJsonStr(thingsService.getCurrentThingsList()));
  }

  private void handleIncrSyncThingsDataList(List<EedsThings> syncDataList) {
    this.convertData(syncDataList);
    String localThingsData;
    try {
      localThingsData = thingsService.getThingsFile();
    } catch (IOException e) {
      logger.error("获取本地数据源文件失败");
      return;
    }
    for (EedsThings syncThings : syncDataList) {
      String operatorType = syncThings.getOperatorType();
      String thingsId = syncThings.getThingsId();
      switch (operatorType) {
        case ConstantThings.P_OPERATOR_TYPE_ADD:
          handleAddThings(syncThings, localThingsData);
          //建立连接
          ThingsConnection connection = ConnectManager.getConnection(
              AgentStartProperties.getInstance().getAgentClientType());
          if (checkThingsConnectParams(connection, syncThings)) {
            //增量新增数据源
            ThingsDriverContext driverContext =MapstructUtils.syncThingsToThingsDriver(syncThings);
            THINGS_DRIVER_CONTEXT_MAP.put(thingsId, driverContext);
            ConnectManager.delConnection(thingsId);
            ConnectManager.create(driverContext,
                AgentStartProperties.getInstance().getAgentClientType());
          }
          break;
        case ConstantThings.P_OPERATOR_TYPE_EDIT:
          handleEditThings(syncThings, localThingsData);
          //编辑连接
          ThingsDriverContext driverContext =MapstructUtils.syncThingsToThingsDriver(syncThings);
          THINGS_DRIVER_CONTEXT_MAP.put(thingsId, driverContext);
          ConnectManager.recreate(driverContext,
              AgentStartProperties.getInstance().getAgentClientType());
          break;
        case ConstantThings.P_OPERATOR_TYPE_DEL:
          handleDelThings(syncThings, localThingsData);
          //删除连接
          if (!thingsService.checkThingsExist(thingsId)) {
            //该数据源不存在，则删除数据源
            THINGS_DRIVER_CONTEXT_MAP.remove(thingsId);
            ConnectManager.delConnection(thingsId);
          }
          break;
        case ConstantThings.P_OPERATOR_TYPE_UNCHANGE:
          handleUnchangeThings(syncThings, localThingsData);
          break;
      }

    }
    saveToFile();
  }


  private void saveToFile() {
    List<EedsThings> currentThingsList = thingsService.getCurrentThingsList();
    if (currentThingsList.size() > 0) {
      thingsService.saveToFile(JSONUtil.toJsonStr(currentThingsList));
    } else {
      thingsService.saveToFile("");
    }
  }

  private void handleAddThings(EedsThings syncThings, String localThingsData) {
    List<EedsProperties> properties = syncThings.getProperties().stream()
        .filter(p -> p.getOperatorType().equals(ConstantThings.P_OPERATOR_TYPE_ADD)).collect(
            Collectors.toList());
    List<EedsProperties> delPropertiesList = syncThings.getProperties().stream()
        .filter(p -> p.getOperatorType().equals(ConstantThings.P_OPERATOR_TYPE_DEL))
        .collect(Collectors.toList());
    delPropertiesList.forEach(propertiesContext -> PROPERTIES_CONTEXT_MAP.values()
        .removeIf(p -> p.getPropertiesId().equals(propertiesContext.getPropertiesId())));
    syncThings.setProperties(properties);
    thingsService.addThings(syncThings);
  }


  private void handleUnchangeThings(EedsThings syncThings, String localThingsData) {
    String thingsId = syncThings.getThingsId();
    List<EedsProperties> syncThingsPropertiesList = syncThings.getProperties();
    for (EedsProperties properties : syncThingsPropertiesList) {
      String operatorType = properties.getOperatorType();
      PropertiesEvent propertiesEvent = MapstructUtils.syncPropToEvent(properties,thingsId);

      if (ConstantThings.P_OPERATOR_TYPE_ADD.equals(operatorType)) {
        thingsService.addProperties(thingsId, properties);
      } else if (ConstantThings.P_OPERATOR_TYPE_DEL.equals(operatorType)) {
        thingsService.delProperties(thingsId, properties);
        PROPERTIES_CONTEXT_MAP.remove(properties.getPropertiesId());
      }
    }
    handlePropertiesEvent(thingsId,syncThingsPropertiesList);

  }

  /**
   * 同步点位，按照修改时间倒叙排列,同步应按正序发送
   *
   * @param propertiesList
   */
  private void handlePropertiesEvent(String thingsId,List<EedsProperties> propertiesList) {
    for (int i = propertiesList.size() - 1; i >= 0; i--) {
      EedsProperties properties = propertiesList.get(i);
      PropertiesEvent propertiesEvent = MapstructUtils.syncPropToEvent(properties, thingsId);
      ConnectManager.sendPropertiesEventNotify(thingsId, propertiesEvent);
    }

  }

  private void handleDelThings(EedsThings syncThings, String localThingsData) {
    List<EedsProperties> properties = syncThings.getProperties();
    properties.forEach(propertiesContext -> PROPERTIES_CONTEXT_MAP.values()
        .removeIf(p -> p.getPropertiesId().equals(propertiesContext.getPropertiesId())));
    thingsService.delThings(syncThings.getThingsId());
  }

  private void handleEditThings(EedsThings syncThings, String localThingsData) {
    String thingsId = syncThings.getThingsId();
    List<EedsThings> currentThingsList = thingsService.getCurrentThingsList();
    if (ObjectUtil.isEmpty(currentThingsList)) {
      //新增数据源
      handleAddThings(syncThings, localThingsData);
      //建立连接
      ThingsConnection connection = ConnectManager.getConnection(
          AgentStartProperties.getInstance().getAgentClientType());
      if (checkThingsConnectParams(connection, syncThings)) {
        //增量新增数据源
        ThingsDriverContext driverContext =MapstructUtils.syncThingsToThingsDriver(syncThings);
        THINGS_DRIVER_CONTEXT_MAP.put(thingsId, driverContext);
        ConnectManager.delConnection(thingsId);
        ConnectManager.create(driverContext,
            AgentStartProperties.getInstance().getAgentClientType());
      }
    } else {
      Optional<EedsThings> first = currentThingsList.stream()
          .filter(things -> things.getThingsId().equals(syncThings.getThingsId())).findFirst();
      if (!first.isPresent()) {
        //新增数据源
        handleAddThings(syncThings, localThingsData);
      } else {
        thingsService.editThings(syncThings);
        List<EedsProperties> properties = syncThings.getProperties();
        for (EedsProperties p : properties) {
          String operatorType = p.getOperatorType();
          if (ConstantThings.P_OPERATOR_TYPE_ADD.equals(operatorType)) {
            thingsService.addProperties(thingsId, p);
          } else if (ConstantThings.P_OPERATOR_TYPE_DEL.equals(operatorType)) {
            thingsService.delProperties(thingsId, p);
            PROPERTIES_CONTEXT_MAP.remove(p.getPropertiesId());
          }
        }
      }
    }

  }

  private void loadThingDriver() {
    try {
      String localThings = thingsService.getThingsFile();
      if (!ObjectUtil.isEmpty(localThings)) {
        List<EedsThings> eedsThings = JSON.parseArray(localThings, EedsThings.class);
        for (EedsThings things : eedsThings) {
          ThingsConnection connection = ConnectManager.getConnection(
              AgentStartProperties.getInstance().getAgentClientType());
          if (checkThingsConnectParams(connection, things)) {
            ThingsDriverContext driverContext =MapstructUtils.syncThingsToThingsDriver(things);
            String agentId = things.getAgentId();
            String thingsId = things.getThingsId();
            THINGS_DRIVER_CONTEXT_MAP.put(things.getThingsId(), driverContext);
            List<EedsProperties> properties = things.getProperties();
            for (EedsProperties p : properties) {
              PropertiesContext propertiesContext = MapstructUtils.syncPropToContext(p, agentId,
                  thingsId, things.getThingsType());
              PROPERTIES_CONTEXT_MAP.put(p.getPropertiesId(), propertiesContext);
            }
            ConnectManager.create(driverContext,
                AgentStartProperties.getInstance().getAgentClientType());
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public List<PropertiesContext> convertData(List<EedsThings> convertData) {
    List<PropertiesContext> propertiesContextList = new ArrayList<>();
    for (EedsThings edgeThings : convertData) {
      String agentId = edgeThings.getAgentId();
      String thingsId = edgeThings.getThingsId();
      List<EedsProperties> properties = edgeThings.getProperties();
      for (EedsProperties p : properties) {
        PropertiesContext propertiesContext = MapstructUtils.syncPropToContext(p, agentId,
            thingsId, edgeThings.getThingsType());
        propertiesContextList.add(propertiesContext);
        PROPERTIES_CONTEXT_MAP.put(p.getPropertiesId(), propertiesContext);
      }
    }
    return propertiesContextList;
  }

  /**
   * 根据数据源id,获取点位信息
   *
   * @param thingsId 数据源ID
   * @return
   */
  public static List<PropertiesContext> getThingsPropertiesContextList(String thingsId) {
    Map<String, List<PropertiesContext>> thingsPropertiesMap = PROPERTIES_CONTEXT_MAP.values()
        .stream().collect(Collectors.groupingBy(PropertiesContext::getThingsId));
    return thingsPropertiesMap.get(thingsId);
  }

  private boolean checkThingsConnectParams(ThingsConnection connection, EedsThings things) {
    if (!connection.checkThingsConnectParams(things)) {
      ThingsConnectionHandler handler = (ThingsConnectionHandler) connection;
      handler.setThingsId(things.getThingsId());
      ThingsConnectionHandler.ThingsStatus thingsStatus = handler.new ThingsStatus();
      thingsStatus.setValue(handler, ConnectionStatus.DISCONNECT,
          "连接参数校验失败,请检查数据源连接参数");
      return false;
    }
    return true;
  }
}
