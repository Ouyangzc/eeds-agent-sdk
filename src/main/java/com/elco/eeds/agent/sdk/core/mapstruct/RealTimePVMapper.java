package com.elco.eeds.agent.sdk.core.mapstruct;

import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import com.elco.eeds.agent.sdk.transfer.beans.message.data.realTime.SubDataRealTimePropertiesMessage;
import com.elco.eeds.agent.sdk.transfer.beans.message.data.sync.data.SubDataSyncPropertiesValueMessage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @ClassName RealTimePVMapper
 * @Description 实时数据Mapper
 * @Author OuYang
 * @Date 2024/5/7 14:24
 * @Version 1.0
 */
@Mapper
public interface RealTimePVMapper {
  RealTimePVMapper INSTANCE  = Mappers.getMapper(RealTimePVMapper.class);

  SubDataRealTimePropertiesMessage pvToMsg(PropertiesValue pv);

  SubDataSyncPropertiesValueMessage pvToSyncMsg(PropertiesValue pv);
}
