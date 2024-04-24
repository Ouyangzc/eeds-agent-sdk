package com.elco.eeds.agent.sdk.transfer.service.cmd;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.elco.eeds.agent.sdk.core.common.constant.CmdConstant;
import com.elco.eeds.agent.sdk.core.common.constant.ConstantCommon;
import com.elco.eeds.agent.sdk.core.common.constant.message.ConstantTopic;
import com.elco.eeds.agent.sdk.core.connect.ThingsConnectionHandler;
import com.elco.eeds.agent.sdk.core.connect.manager.ConnectManager;
import com.elco.eeds.agent.sdk.core.quartz.QuartzManager;
import com.elco.eeds.agent.sdk.core.util.MqPluginUtils;
import com.elco.eeds.agent.sdk.transfer.beans.message.cmd.CmdConfirmMessage;
import com.elco.eeds.agent.sdk.transfer.beans.message.cmd.CmdResult;
import com.elco.eeds.agent.sdk.transfer.beans.message.cmd.CmdResultMessage;
import com.elco.eeds.agent.sdk.transfer.beans.message.cmd.SubCmdRequestMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName CmdService
 * @Description 指令下发服务
 * @Author OuYang
 * @Date 2023/8/16 10:23
 * @Version 1.0
 */
public class CmdService {
    public static final Logger logger = LoggerFactory.getLogger(CmdService.class);

    private CmdRequestManager cmdRequestManager;

    public CmdService(CmdRequestManager cmdRequestManager) {
        this.cmdRequestManager = cmdRequestManager;
    }

    public static void setReadyStatus(String thingsId) {
        CmdRequestManager.setReadyStatus(thingsId);
    }

    /**
     * 发送指令下发确认报文
     *
     * @param thingsId
     * @param msgSeqNo
     */
    public void sendConfirmMsg(String thingsId, String msgSeqNo) {
        String message = CmdConfirmMessage.createMsg(thingsId, msgSeqNo).toJson();
        String topic = ConstantTopic.TOPIC_AGENT_CMD_CONFIRM_RESPOND.replace(
            ConstantCommon.TOPIC_SUFFIX_THINGSID, thingsId);
        MqPluginUtils.sendCmdConfirmMsg(topic,message);
        logger.info("指令下发发送确认报文：{}", message);
    }

    public static void sendResult(CmdResult result) {
        if (result.isResult()) {
            sendResult(result.getThingsId(), result.getMsgSeqNo(), null);
        } else {
            sendResult(result.getThingsId(), result.getMsgSeqNo(), result.getResultMsg());
        }
    }

    public static void sendTimeoutResult(String thingsId, String msgSeqNo, String errMsg) {
        CmdResultMessage cmdResultMessage = CmdResultMessage.createFail(thingsId, msgSeqNo, errMsg);
        String message = JSONUtil.toJsonStr(cmdResultMessage);
        String topic = ConstantTopic.TOPIC_AGENT_CMD_RESULT_RESPOND.replace(ConstantCommon.TOPIC_SUFFIX_THINGSID, thingsId);
        MqPluginUtils.sendCmdTimeoutMsg(topic,message);
        logger.info("指令下发结果报文：topic: {}; message: {}", topic, message);
        setReadyStatus(thingsId);
        refreshThingsCmdQueue(thingsId);
    }


    public static void sendResult(String thingsId, String msgSeqNo, String errMsg) {
        CmdResultMessage cmdResultMessage = null;
        if (StrUtil.isEmpty(errMsg)) {
            cmdResultMessage = CmdResultMessage.createSuccess(thingsId, msgSeqNo);
        } else {
            cmdResultMessage = CmdResultMessage.createFail(thingsId, msgSeqNo, errMsg);
        }
        String message = JSONUtil.toJsonStr(cmdResultMessage);
        String topic = ConstantTopic.TOPIC_AGENT_CMD_RESULT_RESPOND.replace(ConstantCommon.TOPIC_SUFFIX_THINGSID, thingsId);
        MqPluginUtils.sendCmdResultMsg(topic,message);
        logger.info("指令下发结果报文：topic: {}; message: {}", topic, message);
        setReadyStatus(thingsId);
        refreshThingsCmdQueue(thingsId);
    }


    /**
     * 将该数据源的下发指令加入到队列中
     *
     * @param thingsId
     * @param cmdRequestMessage
     */
    public void addThingsCmdRequestMessage(String thingsId, SubCmdRequestMessage cmdRequestMessage) {
        cmdRequestManager.addThingsCmdRequestMessage(thingsId, cmdRequestMessage);
        refreshThingsCmdQueue(thingsId);
    }

    public static void refreshThingsCmdQueue(String thingsId) {
        if (!CmdRequestManager.checkThingsQueueCanRun(thingsId)) {
            logger.debug("当前数据源执行指令条件不足,数据源:{}", thingsId);
            return;
        }
        SubCmdRequestMessage requestMessage = CmdRequestManager.getNextCmdMessage(thingsId);
        String msgSeqNo = "";
        try {
            if (null == requestMessage) {
                logger.debug("当前数据源无可执行指令,数据源:{}", thingsId);
                return;
            }
             msgSeqNo = requestMessage.getMsgSeqNo();
            int orderTimeOut = requestMessage.getOrderTimeOut();
            ThingsConnectionHandler handler = ConnectManager.getHandler(thingsId);
            if (ObjectUtil.isEmpty(handler)) {
                logger.error("指令功能下发,数据源:{},连接异常:{}", thingsId, "CL0101011");
                CmdService.sendResult(thingsId, msgSeqNo, "【失败】数据源连接不存在,请检查数据源状态");
                return;
            }
            if (!handler.cmdCheck(requestMessage.getInputData())) {
                logger.error("指令功能下发,消息ID:{},参数校验不通过：{}", msgSeqNo, requestMessage.getInputData());
                CmdService.sendResult(thingsId, msgSeqNo, "参数校验不通过,请检查下发参数");
                return;
            }
            String orderType = requestMessage.getOrderType();
            CmdResult result = handler.cmdWrite(requestMessage);
            if (CmdConstant.ORDER_TYPE_NO_RESPONSE.equals(orderType)) {
                //未响应，
                CmdService.sendResult(thingsId, msgSeqNo, null);
                refreshThingsCmdQueue(thingsId);
            } else {
                if (null != result) {
                    logger.debug("指令功能下发，执行结果：{}",result);
                    if (result.isResult()) {
                        CmdService.sendResult(thingsId, msgSeqNo, null);
                    } else {
                        CmdService.sendResult(thingsId, msgSeqNo, result.getResultMsg());
                    }
                } else {
                    QuartzManager.addCmdTimeOutJob(msgSeqNo, thingsId, orderTimeOut);
                    //设置等待状态，等待回复或者超时
                    CmdRequestManager.setWaitingStatus(thingsId);
                }
            }
        } catch (Exception e) {
            logger.error("指令下发,发生异常,异常堆栈:", e);
            //发生异常，发送异常信息到服务端
            CmdService.sendResult(thingsId, msgSeqNo, e.getMessage());
            setReadyStatus(thingsId);
        }

    }


}
