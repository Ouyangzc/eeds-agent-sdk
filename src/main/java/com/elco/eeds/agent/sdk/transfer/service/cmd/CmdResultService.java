package com.elco.eeds.agent.sdk.transfer.service.cmd;

import com.elco.eeds.agent.sdk.core.quartz.QuartzManager;
import com.elco.eeds.agent.sdk.transfer.beans.message.cmd.CmdResult;
import java.io.Serializable;

/**
 * @ClassName CmdResultService
 * @Description 指令下发执行结果
 * @Author OuYang
 * @Date 2023/8/16 16:39
 * @Version 1.0
 */
public class CmdResultService implements Serializable {


    /**
     * 收到指令下发执行结果
     *
     * @param result
     */
    public static void sendCmdResult(CmdResult result) {
        QuartzManager.removeCmdTimeOutJob(result.getThingsId(), result.getMsgSeqNo());
        CmdService.sendResult(result);
    }
}
