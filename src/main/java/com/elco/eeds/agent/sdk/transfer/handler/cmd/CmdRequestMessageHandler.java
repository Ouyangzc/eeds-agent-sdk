package com.elco.eeds.agent.sdk.transfer.handler.cmd;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.common.constant.CmdConstant;
import com.elco.eeds.agent.sdk.core.connect.ThingsConnectionHandler;
import com.elco.eeds.agent.sdk.core.connect.manager.ConnectManager;
import com.elco.eeds.agent.sdk.core.connect.scheduler.IJobManageService;
import com.elco.eeds.agent.sdk.transfer.beans.message.cmd.CmdRequestMessage;
import com.elco.eeds.agent.sdk.transfer.beans.message.cmd.CmdResult;
import com.elco.eeds.agent.sdk.transfer.beans.message.cmd.SubCmdRequestMessage;
import com.elco.eeds.agent.sdk.transfer.handler.IReceiverMessageHandler;
import com.elco.eeds.agent.sdk.transfer.service.order.OrderConfirmMqService;
import com.elco.eeds.agent.sdk.transfer.service.order.OrderResultMqService;
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

    @Override
    public void handleRecData(String topic, String recData) {
        CmdRequestMessage message = JSON.parseObject(recData, CmdRequestMessage.class);
        logger.info("接收到指令功能下发消息，topic:{},data:{}", topic, JSONUtil.toJsonStr(recData));
        SubCmdRequestMessage data = message.getData();
        String msgSeqNo = data.getMsgSeqNo();
        String thingsId = data.getThingsId();
        // 发送指令功能下发确认报文
        OrderConfirmMqService.send(thingsId, msgSeqNo);
        ThingsConnectionHandler handler = ConnectManager.getHandler(thingsId);
        if (ObjectUtil.isEmpty(handler)) {
            logger.error("指令功能下发,数据源:{},连接异常:{}", thingsId, "CL0101011");
            OrderResultMqService.sendFail(thingsId, msgSeqNo, "【失败】数据源连接不存在,请检查数据源状态");
            return;
        }
        boolean checkResult = handler.writeCheck(data.getIdentifier(), data.getInputData());
        if (!checkResult) {
            logger.error("指令功能下发,消息ID:{},参数校验不通过：{}", msgSeqNo, JSONUtil.toJsonStr(data.getInputData()));
            OrderResultMqService.sendFail(thingsId, msgSeqNo, "参数校验不通过,请检查下发参数");
        }
        String orderType = data.getOrderType();
        CmdResult result = handler.write(data);
        if (CmdConstant.ORDER_TYPE_NO_RESPONSE.equals(orderType)) {
            //未响应，
            OrderResultMqService.sendSuccess(thingsId, msgSeqNo);
        } else {
            if (null != result) {
                logger.info("指令功能下发，执行结果：{}", JSONUtil.toJsonStr(result));
                if (result.isResult()) {
                    OrderResultMqService.sendSuccess(thingsId, msgSeqNo);
                } else {
                    OrderResultMqService.sendFail(thingsId, msgSeqNo, result.getResultMsg());
                }
            } else {
                Integer orderTimeOut = Agent.getInstance().getAgentBaseInfo().getOrderTimeOut();
                IJobManageService jobManage = ConnectManager.getJobManage();
                jobManage.addCmdTimeOutJob(msgSeqNo, thingsId, orderTimeOut);
            }
        }

    }
}
