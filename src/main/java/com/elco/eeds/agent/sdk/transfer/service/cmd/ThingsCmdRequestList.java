package com.elco.eeds.agent.sdk.transfer.service.cmd;

import com.elco.eeds.agent.sdk.core.common.constant.CmdConstant;
import com.elco.eeds.agent.sdk.transfer.beans.message.cmd.SubCmdRequestMessage;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * @ClassName ThingsCmdRequestList
 * @Description 数据源指令下发集合对象
 * @Author OuYang
 * @Date 2023/8/15 13:55
 * @Version 1.0
 */
public class ThingsCmdRequestList implements Serializable {

    /**
     * 数据源ID
     */
    private final String thingsId;

    /**
     * 指令集合
     */
    private Queue<SubCmdRequestMessage> cmdRequestMessageQueue;

    /**
     * @see com.elco.eeds.agent.sdk.core.common.constant.CmdConstant#WAITING
     */
    private Integer status;

    public ThingsCmdRequestList(String thingsId, SubCmdRequestMessage cmdRequestMessage) {
        this.thingsId = thingsId;
        this.cmdRequestMessageQueue = new ArrayDeque<>();
        this.cmdRequestMessageQueue.add(cmdRequestMessage);
        this.status = CmdConstant.READY;
    }

    public String getThingsId() {
        return thingsId;
    }

    public Queue<SubCmdRequestMessage> getCmdRequestMessageQueue() {
        return cmdRequestMessageQueue;
    }

    public boolean isQueueEmpty() {
        return this.cmdRequestMessageQueue.isEmpty();
    }


    public boolean isReadyStatus() {
        return this.status.equals(CmdConstant.READY);
    }

    public SubCmdRequestMessage getNextCmdMessage() {
        if (isReadyStatus() && !isQueueEmpty()) {
            return this.cmdRequestMessageQueue.poll();
        }
        return null;
    }

    public void addNextCmdMessage(SubCmdRequestMessage cmdRequestMessage) {
        this.cmdRequestMessageQueue.add(cmdRequestMessage);
    }

    public void setWaitingStatus() {
        this.status = CmdConstant.WAITING;
    }

    public void setReadyStatus() {
        this.status = CmdConstant.READY;
    }


    public static void main(String[] args) {
        ArrayDeque<String> deque = new ArrayDeque<>();
        deque.add("1");
        deque.add("2");
        deque.add("3");
        System.out.println(deque.poll() + " last: " + deque.toString());
        System.out.println(deque.poll() + " last: " + deque.toString());
        System.out.println(deque.poll() + " last: " + deque.toString());
        System.out.println(deque.poll() + " last: " + deque.toString());
    }
}
