package com.elco.eeds.agent.sdk.core.mapstruct;

import com.elco.eeds.agent.sdk.transfer.beans.data.sync.DataSyncServerRequest;
import com.elco.eeds.agent.sdk.transfer.beans.things.ThingsDriverContext;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @ClassName DataSyncMapper
 * @Description 数据同步
 * @Author OuYang
 * @Date 2024/5/7 14:19
 * @Version 1.0
 */
@Mapper
public interface DataSyncMapper {
  DataSyncMapper INSTANCE  = Mappers.getMapper(DataSyncMapper.class);

  ThingsDriverContext requestToThingsContext(DataSyncServerRequest request);

}
