package com.elco.eeds.agent.sdk.core.mapstruct;

import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesContext;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import com.elco.storage.domain.PropertiesData;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @ClassName PropertiesValueMapper
 * @Description 实时数据转换类
 * @Author OuYang
 * @Date 2024/4/9 8:53
 * @Version 1.0
 */
@Mapper
public interface PropertiesValueMapper {
  PropertiesValueMapper INSTANCE  = Mappers.getMapper(PropertiesValueMapper.class);

  PropertiesData valueToData(PropertiesValue propertiesValue);

  List<PropertiesData> valuesToDatas(List<PropertiesValue> pvs);

  PropertiesValue contextToVirtualPV(PropertiesContext context);
}
