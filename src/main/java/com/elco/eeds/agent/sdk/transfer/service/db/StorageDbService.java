package com.elco.eeds.agent.sdk.transfer.service.db;

import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentBaseInfo;
import com.elco.eeds.agent.sdk.core.common.constant.ConstantCommon;
import com.elco.eeds.agent.sdk.core.common.constant.http.ConstantHttpApiPath;
import com.elco.eeds.agent.sdk.core.util.JSONUtils;
import com.elco.eeds.agent.sdk.core.util.ObjectsUtils;
import com.elco.eeds.agent.sdk.core.util.StrUtils;
import com.elco.eeds.agent.sdk.transfer.beans.storage.StorageDBInfoBean;
import com.elco.storage.domain.ChangeResult;
import com.elco.storage.service.StorageService;
import com.elco.storage.utils.SpringUtils;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName StorageDbService
 * @Description 数据存储服务类
 * @Author OuYang
 * @Date 2024/4/22 14:26
 * @Version 1.0
 */
public class StorageDbService {

  private static Logger logger = LoggerFactory.getLogger(StorageDbService.class);

  /**
   * 获取服务端存储配置,并加载该配置
   */
  public void getAndLoadStorageInfo() {
    try {
      AgentBaseInfo agentBaseInfo = Agent.getInstance().getAgentBaseInfo();
      Optional<String> optional = StorageDbRequestHttpService.getStorageInfo(
          agentBaseInfo.getToken());
      StorageService storageService = SpringUtils.getBean(StorageService.class);
      if (optional.isPresent() && ObjectsUtils.isNotNull(storageService)) {
        String data = optional.get();
        if (StrUtils.isNotEmpty(data)) {
          StorageDBInfoBean bean = JSONUtils.toBean(data, StorageDBInfoBean.class);
          if (ObjectsUtils.isNotNull(bean) && bean.getStatus().equals(ConstantCommon.ONE)) {
            ChangeResult changeResult = storageService.changeStorageDb(bean.getTcpUrl(),
                bean.getUserName(), bean.getPassword(), bean.getCertStr(), bean.getDbTypeEnum(),
                bean.getSecurityTypeEnum(), bean.getCluster());
            logger.info("切换数据库结果:{}", changeResult);
            return;
          }
        }
      }
      //默认初始化
      storageService.initStorageDbByConfig();
    } catch (Exception e) {
      logger.error("获取服务端存储配置并加载异常，异常信息:{},堆栈:", e.getMessage(), e);
    }

  }
}
