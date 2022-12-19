package com.elco.eeds.agent.sdk.transfer.service.things;

import com.elco.eeds.agent.sdk.transfer.beans.message.things.SubThingsSyncIncrMessage;

/**
 * @ClassName ThingsSyncService
 * @Description 数据源同步接口
 * @Author OUYANG
 * @Date 2022/12/16 9:03
 */
public interface ThingsSyncService {

     void setupSyncThings();

     void incrSyncThings(SubThingsSyncIncrMessage message);
}
