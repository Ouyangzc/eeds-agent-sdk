package com.elco.eeds.agent.sdk.core.mapstruct;

import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesContext;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesEvent;
import com.elco.eeds.agent.sdk.transfer.beans.things.EedsProperties;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @ClassName PropertiesMapper
 * @Description 变量mapper
 * @Author OuYang
 * @Date 2024/5/7 10:58
 * @Version 1.0
 */
@Mapper
public interface PropertiesMapper {
  PropertiesMapper INSTANCE  = Mappers.getMapper(PropertiesMapper.class);


  PropertiesContext syncPropToContext(EedsProperties syncProperties,String agentId,String thingsId,String thingsType);

  PropertiesEvent syncPropToEvent(EedsProperties properties,String thingsId);
}
