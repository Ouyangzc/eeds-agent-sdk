package com.elco.eeds.agent.sdk.core.script.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import com.elco.eeds.agent.sdk.core.script.JsInvokeService;
import com.elco.eeds.agent.sdk.core.script.ScriptBody;
import com.elco.eeds.agent.sdk.core.script.domain.Virtual;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ：ytl
 * @date ：Created in 2023/1/11 10:33
 * @description：
 */
public class JsInvokeServiceImpl implements JsInvokeService {
    @Override
    public void eval(String scriptBody, String... argNames) {

    }

    @Override
    public String execute(Virtual virtual, List<PropertiesValue> propertiesValueList) {
        virtual.getArgsList().stream().forEach(t -> {
            List<PropertiesValue> list = propertiesValueList.stream().filter(p -> p.getAddress().equals(t.getAddress()))
                    .collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(list)) {
                PropertiesValue value = list.get(0);
                Object arg;
                arg=value.getValue();








                if (t.getType().equals("int")) {
                    arg=Integer.valueOf(value.getValue());
                }
                virtual.getArgsMap().put(t.getVariableName(),arg);
            }

        });
        ScriptBody scriptBody = new ScriptBody(virtual.getScriptBody(), virtual.getArgsMap());
        return String.valueOf(scriptBody.doEval());
    }
}
