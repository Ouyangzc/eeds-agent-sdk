package com.elco.eeds.agent.sdk.core.util;

import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import com.elco.eeds.agent.sdk.core.mapstruct.PropertiesValueMapper;
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
   * @param pvs
   * @return
   */
  public static List<PropertiesData> valueToData(List<PropertiesValue> pvs){
    List<PropertiesData> datas = PropertiesValueMapper.INSTANCE.valuesToDatas(pvs);
    return datas;
  }


}
