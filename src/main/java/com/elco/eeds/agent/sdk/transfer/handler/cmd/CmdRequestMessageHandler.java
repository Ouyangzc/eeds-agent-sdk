package com.elco.eeds.agent.sdk.transfer.handler.cmd;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.sdk.transfer.beans.message.cmd.CmdRequestMessage;
import com.elco.eeds.agent.sdk.transfer.beans.message.cmd.SubCmdRequestMessage;
import com.elco.eeds.agent.sdk.transfer.handler.IReceiverMessageHandler;
import com.elco.eeds.agent.sdk.transfer.service.cmd.CmdRequestManager;
import com.elco.eeds.agent.sdk.transfer.service.cmd.CmdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName CmdRequestMessageHandler
 * @Description 设备指令功能下发
 * @Author OuYang
 * @Date 2023/5/16 11:35
 * @Version 1.0
 */
public class CmdRequestMessageHandler implements IReceiverMessageHandler {

    private static Logger logger = LoggerFactory.getLogger(CmdRequestMessageHandler.class);

    private CmdService cmdService;

    public CmdRequestMessageHandler(CmdService cmdService) {
        this.cmdService = cmdService;
    }

    @Override
    public void handleRecData(String topic, String recData) {
        CmdRequestMessage message = JSON.parseObject(recData, CmdRequestMessage.class);
        logger.info("接收到指令功能下发消息，topic:{},data:{}", topic, recData);
        SubCmdRequestMessage data = message.getData();
        String msgSeqNo = data.getMsgSeqNo();
        String thingsId = data.getThingsId();
        // 发送指令功能下发确认报文
        cmdService.sendConfirmMsg(thingsId, msgSeqNo);
        //将该指令加入到缓存集合中
        cmdService.addThingsCmdRequestMessage(thingsId, data);
    }
}
