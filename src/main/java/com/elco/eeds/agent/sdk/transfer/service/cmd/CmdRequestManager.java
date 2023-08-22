package com.elco.eeds.agent.sdk.transfer.service.cmd;

import com.elco.eeds.agent.sdk.transfer.beans.message.cmd.SubCmdRequestMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @ClassName CmdRequestManager
 * @Description 指令下发集合管理类
 * @Author OuYang
 * @Date 2023/8/15 14:51
 * @Version 1.0
 */
public class CmdRequestManager {

    public static Logger logger = LoggerFactory.getLogger(CmdRequestManager.class);

    private static final Map<String, ThingsCmdRequestList> thingsCmdRequestMap = new ConcurrentHashMap<>();


    public void addThingsCmdRequestMessage(String thingsId, SubCmdRequestMessage cmdRequestMessage) {
        if (thingsCmdRequestMap.containsKey(thingsId)) {
            ThingsCmdRequestList requestList = thingsCmdRequestMap.get(thingsId);
            requestList.addNextCmdMessage(cmdRequestMessage);
        } else {
            ThingsCmdRequestList thingsCmdRequestList = new ThingsCmdRequestList(thingsId, cmdRequestMessage);
            thingsCmdRequestMap.put(thingsId, thingsCmdRequestList);
        }
    }

    /**
     * 校验该数据源的指令队列是否不为空并且状态为ready
     *
     * @param thingsId
     * @return
     */
    public static boolean  checkThingsQueueCanRun(String thingsId) {
        ThingsCmdRequestList thingsCmdRequestList = thingsCmdRequestMap.get(thingsId);
        return !thingsCmdRequestList.isQueueEmpty() && thingsCmdRequestList.isReadyStatus();
    }

    public static SubCmdRequestMessage getNextCmdMessage(String thingsId) {
        ThingsCmdRequestList thingsCmdRequestList = thingsCmdRequestMap.get(thingsId);
        return thingsCmdRequestList.getNextCmdMessage();
    }

    public static void setWaitingStatus(String thingsId) {
        ThingsCmdRequestList cmdRequestList = thingsCmdRequestMap.get(thingsId);
        cmdRequestList.setWaitingStatus();
    }

    public static void setReadyStatus(String thingsId) {
        ThingsCmdRequestList cmdRequestList = thingsCmdRequestMap.get(thingsId);
        cmdRequestList.setReadyStatus();
    }


}
