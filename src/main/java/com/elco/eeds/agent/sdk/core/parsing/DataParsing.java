package com.elco.eeds.agent.sdk.core.parsing;

import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesContext;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;

import java.util.List;

/**
 * @author ：ytl
 * @date ：Created in 2022/12/6 10:03
 * @description：
 */
public abstract class DataParsing {
    public abstract List<PropertiesValue> parsing(List<PropertiesContext> properties, String msg);

    public String parsingCommand(String original){
        return original;
    }



}
