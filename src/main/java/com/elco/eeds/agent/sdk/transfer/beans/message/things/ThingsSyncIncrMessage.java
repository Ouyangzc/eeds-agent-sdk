package com.elco.eeds.agent.sdk.transfer.beans.message.things;

import cn.hutool.json.JSONUtil;
import com.elco.eeds.agent.sdk.transfer.beans.message.BaseMessage;

import java.io.Serializable;

/**
 * @ClassName ThingsSyncIncrMessage
 * @Description 数据源增量同步报文结构体
 * @Author OUYANG
 * @Date 2022/12/19 14:29
 */
public class ThingsSyncIncrMessage extends BaseMessage<SubThingsSyncIncrMessage> implements Serializable {

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
}
