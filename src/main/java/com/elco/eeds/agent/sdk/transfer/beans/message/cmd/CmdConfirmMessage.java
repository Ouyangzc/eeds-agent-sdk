package com.elco.eeds.agent.sdk.transfer.beans.message.cmd;

import com.elco.eeds.agent.sdk.core.common.enums.MessageMethod;
import com.elco.eeds.agent.sdk.transfer.beans.message.BaseMessage;
import java.io.Serializable;

/**
 * @ClassName CmdConfirmMessage
 * @Description 指令下发确认报文
 * @Author OuYang
 * @Date 2023/8/16 10:44
 * @Version 1.0
 */
public class CmdConfirmMessage extends BaseMessage<SubCmdConfirmMessage> implements Serializable {

    public static CmdConfirmMessage createMsg(String thingsId, String msgSeqNo) {
        CmdConfirmMessage msg = new CmdConfirmMessage();
        msg.setMethod(MessageMethod.CMD_CONFIRM.getMethod());
        msg.setTimestamp(System.currentTimeMillis());
        msg.setData(new SubCmdConfirmMessage(thingsId, msgSeqNo));
        return msg;
    }
}
