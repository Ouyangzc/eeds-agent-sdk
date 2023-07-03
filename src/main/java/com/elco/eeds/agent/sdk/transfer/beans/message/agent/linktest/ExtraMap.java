package com.elco.eeds.agent.sdk.transfer.beans.message.agent.linktest;

import java.io.Serializable;

/**
 * @title: ExtraMap
 * @Author wl
 * @Date: 2022/12/20 16:01
 * @Version 1.0
 */
public class ExtraMap implements Serializable {

    /**
     * 过滤条件
     */
    private String filter;

    public ExtraMap(String filter) {
        this.filter = filter;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
