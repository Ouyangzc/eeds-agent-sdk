package com.elco.eeds.agent.sdk.core.parsing;

import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesContext;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import com.elco.eeds.agent.sdk.transfer.beans.things.ThingsDriverContext;

import java.util.List;

/**
 * @author ：ytl
 * @date ：Created in 2022/12/6 10:03
 * @description：
 */
public abstract class DataParsing {
    public abstract List<PropertiesValue> parsing(ThingsDriverContext context,List<PropertiesContext> properties, String msg);

    public String parsingCommand(String original){
        return original;
    }



}
