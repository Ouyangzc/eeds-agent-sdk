package com.elco.eeds.agent.sdk.transfer.service.things;

import cn.hutool.json.JSONUtil;
import com.elco.eeds.agent.sdk.common.entity.ResponseResult;
import com.elco.eeds.agent.sdk.common.enums.SysCodeEnum;
import com.elco.eeds.agent.sdk.core.common.enums.ErrorEnum;
import com.elco.eeds.agent.sdk.core.exception.SdkException;
import com.elco.eeds.agent.sdk.core.util.http.HttpUrlProcessor;
import com.elco.eeds.agent.sdk.transfer.beans.things.ThingsSyncRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName ThingsRequestHttpService
 * @Description 数据源请求服务端服务类
 * @Author OUYANG
 * @Date 2022/12/16 9:22
 */
public class ThingsRequestHttpService {

  private static final Logger logger = LoggerFactory.getLogger(ThingsRequestHttpService.class);

  /**
   * 数据源同步
   *
   * @param request 同步请求
   * @param token   token
   * @param apiPath api
   * @return
   */
  public static String getThingsSyncData(ThingsSyncRequest request, String token, String apiPath) {
    try {
      HttpUrlProcessor httpUrlProcessor = new HttpUrlProcessor(apiPath);
      String response = httpUrlProcessor.processRequest(token, JSONUtil.toJsonStr(request));
      ResponseResult responseResult = JSONUtil.toBean(response, ResponseResult.class);
      if (!SysCodeEnum.SUCCESS.getCode().equals(responseResult.getCode())) {
        throw new SdkException(ErrorEnum.THINGS_SYNC_SETUP_HTTP_ERROR.code());
      }
      return responseResult.getData().toString();
    } catch (Exception e) {
      logger.error("数据源同步--启动API异常, 形参为：{}", JSONUtil.toJsonStr(request));
      e.printStackTrace();
    }
    return null;
  }
}
