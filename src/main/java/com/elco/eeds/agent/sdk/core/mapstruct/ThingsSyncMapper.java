package com.elco.eeds.agent.sdk.core.mapstruct;

import com.elco.eeds.agent.sdk.transfer.beans.things.EedsThings;
import com.elco.eeds.agent.sdk.transfer.beans.things.ThingsDriverContext;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @ClassName ThingsSyncMapper
 * @Description 数据源同步Mapper
 * @Author OuYang
 * @Date 2024/5/7 10:20
 * @Version 1.0
 */
@Mapper
public interface ThingsSyncMapper {
  ThingsSyncMapper INSTANCE  = Mappers.getMapper(ThingsSyncMapper.class);

  /**
   * 同步数据源复制到本地
   * @param syncThings
   * @return
   */
  ThingsDriverContext syncThingsToThingsDriver(EedsThings syncThings);

  EedsThings thingsToThings(EedsThings things);
}
