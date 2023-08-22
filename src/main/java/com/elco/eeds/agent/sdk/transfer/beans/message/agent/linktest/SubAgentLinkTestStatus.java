package com.elco.eeds.agent.sdk.transfer.beans.message.agent.linktest;

import java.io.Serializable;

/**
 * @title: SubAgentLinkTestStatus
 * @Author wl
 * @Date: 2022/12/20 15:58
 * @Version 1.0
 */
public class SubAgentLinkTestStatus implements Serializable {

    private String msg;

    public SubAgentLinkTestStatus(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
