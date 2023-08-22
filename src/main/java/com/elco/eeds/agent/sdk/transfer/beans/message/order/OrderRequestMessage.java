package com.elco.eeds.agent.sdk.transfer.beans.message.order;

import cn.hutool.json.JSONUtil;
import com.elco.eeds.agent.sdk.transfer.beans.message.BaseMessage;

import java.io.Serializable;

/**
 * @ClassName ThingsSyncIncrMessage
 * @Description 数据源连接状态报文
 * @Author OUYANG
 * @Date 2022/12/19 14:29
 */
public class OrderRequestMessage extends BaseMessage<SubOrderRequestMessage> implements Serializable {

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
}
