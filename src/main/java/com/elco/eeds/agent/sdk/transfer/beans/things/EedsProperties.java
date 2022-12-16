package com.elco.eeds.agent.sdk.transfer.beans.things;

import java.util.Map;

/**
 * @ClassName EedsProperties
 * @Description 数据源变量类
 * @Author OUYANG
 * @Date 2022/12/16 9:12
 */
public class EedsProperties extends BaseProperties {
    /**
     * 扩展map
     */
    private Map<String, String> extraMap;

    public Map<String, String> getExtraMap() {
        return extraMap;
    }

    public void setExtraMap(Map<String, String> extraMap) {
        this.extraMap = extraMap;
    }
}
