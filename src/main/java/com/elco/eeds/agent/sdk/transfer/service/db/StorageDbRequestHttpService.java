package com.elco.eeds.agent.sdk.transfer.service.db;

import cn.hutool.json.JSONUtil;
import com.elco.eeds.agent.sdk.common.entity.ResponseResult;
import com.elco.eeds.agent.sdk.common.enums.SysCodeEnum;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentClusterProperties;
import com.elco.eeds.agent.sdk.core.common.constant.http.ConstantHttpApiPath;
import com.elco.eeds.agent.sdk.core.common.enums.ErrorEnum;
import com.elco.eeds.agent.sdk.core.exception.SdkException;
import com.elco.eeds.agent.sdk.core.util.AgentResourceUtils;
import com.elco.eeds.agent.sdk.core.util.JSONUtils;
import com.elco.eeds.agent.sdk.core.util.http.HttpUrlProcessor;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName StorageDbRequestHttpService
 * @Description 数据存储请求
 * @Author OuYang
 * @Date 2024/4/22 10:19
 * @Version 1.0
 */
public class StorageDbRequestHttpService {

  private static final Logger logger = LoggerFactory.getLogger(StorageDbRequestHttpService.class);


  /**
   * 请求数据存储
   *
   * @param token
   * @return
   */
  public static Optional<String> getStorageInfo(String token) {
    try {
      HttpUrlProcessor httpUrlProcessor = new HttpUrlProcessor(ConstantHttpApiPath.STORAGE_DB_INFO_API);
      String response = httpUrlProcessor.processRequest(token, "");
      ResponseResult responseResult = JSONUtil.toBean(response, ResponseResult.class);
      if (!SysCodeEnum.SUCCESS.getCode().equals(responseResult.getCode())) {
        throw new SdkException(ErrorEnum.THINGS_SYNC_SETUP_HTTP_ERROR.code());
      }
      return Optional.of(JSONUtils.toJsonStr(responseResult.getData()));
    } catch (Exception e) {
      logger.error("数据存储请求--API异常, 报错信息为：{}", e.getMessage());
    }
    return Optional.absent();
  }
}
