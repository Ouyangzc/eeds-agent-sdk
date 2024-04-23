package com.elco.eeds.agent.sdk.transfer.beans.message.cmd;

import com.elco.eeds.agent.sdk.core.common.enums.MessageMethod;
import com.elco.eeds.agent.sdk.transfer.beans.message.BaseMessage;
import java.io.Serializable;

/**
 * @ClassName CmdResultMessage
 * @Description 指令下发结果报文
 * @Author OuYang
 * @Date 2023/8/16 10:56
 * @Version 1.0
 */
public class CmdResultMessage extends BaseMessage<SubCmdResultMessage> implements Serializable {
    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";

    public static CmdResultMessage createSuccess(String thingsId, String msgSeqNo) {
        CmdResultMessage msg = new CmdResultMessage();
        msg.setMethod(MessageMethod.CMD_RESPOND.getMethod());
        msg.setTimestamp(System.currentTimeMillis());
        msg.setData(new SubCmdResultMessage(thingsId, msgSeqNo, SUCCESS, ""));
        return msg;
    }

    public static CmdResultMessage createFail(String thingsId, String msgSeqNo, String errMsg) {
        CmdResultMessage msg = new CmdResultMessage();
        msg.setMethod(MessageMethod.CMD_RESPOND.getMethod());
        msg.setTimestamp(System.currentTimeMillis());
        msg.setData(new SubCmdResultMessage(thingsId, msgSeqNo, FAIL, errMsg));
        return msg;
    }
}
