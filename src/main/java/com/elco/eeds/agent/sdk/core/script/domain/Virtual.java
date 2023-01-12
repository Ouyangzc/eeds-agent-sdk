package com.elco.eeds.agent.sdk.core.script.domain;

import java.util.HashMap;
import java.util.List;

/**
 * @author ：ytl
 * @date ：Created in 2023/1/11 11:37
 * @description： 虚拟点位
 */
public class Virtual {


    private HashMap<String,Object> argsMap = new HashMap<>();
    /**
     * 变量地址
     */
    private String address;

    /**
     * 关联的点位
     */
    private List<PropertiesInfo> argsList;
    /**
     * js 表达式
     */
    private String scriptBody;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<PropertiesInfo> getArgsList() {
        return argsList;
    }

    public void setArgsList(List<PropertiesInfo> argsList) {
        this.argsList = argsList;
    }

    public String getScriptBody() {
        return scriptBody;
    }

    public void setScriptBody(String scriptBody) {
        this.scriptBody = scriptBody;
    }
    

    public HashMap<String,Object>getArgsMap(){
        this.getArgsList().stream().forEach(t->{
            argsMap.put(t.getVariableName(),0);
        });
        return argsMap;
    }
}
