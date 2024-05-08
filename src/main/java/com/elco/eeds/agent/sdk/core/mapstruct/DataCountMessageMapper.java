package com.elco.eeds.agent.sdk.core.mapstruct;

import com.elco.eeds.agent.sdk.transfer.beans.data.count.PostDataCount;
import com.elco.eeds.agent.sdk.transfer.beans.message.data.count.post.SubDataCountMessage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @ClassName DataCountMessageMapper
 * @Description 数据统计Mapper
 * @Author OuYang
 * @Date 2024/5/7 14:15
 * @Version 1.0
 */
@Mapper
public interface DataCountMessageMapper {
  DataCountMessageMapper INSTANCE  = Mappers.getMapper(DataCountMessageMapper.class);

  SubDataCountMessage dataCountToMsg(PostDataCount dataCount);
}
