package com.elco.eeds.agent.sdk.core.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.elco.eeds.agent.sdk.core.bean.agent.BaseConfigEntity;

import java.util.Iterator;
import java.util.Map;

/**
 * @title: MapToJsonConfig
 * @Author wl
 * @Date: 2022/12/8 18:44
 * @Version 1.0
 */
public class MapUtils {

    /**
     * 用全局配置map和私有配置map组合拼成json结构
     * @param mapGlobal
     * @param mapPrivate
     * @return
     */
    public static JSONArray mapToJsonConfig(Map mapGlobal, Map mapPrivate) {
        JSONArray jsonArrayConfig = new JSONArray();
        Iterator<Map.Entry<String, String>> itGlobal = mapGlobal.entrySet().iterator();
        while (itGlobal.hasNext()) {
            Map.Entry<String, String> entryGlobal = itGlobal.next();
            if (!mapPrivate.containsKey(entryGlobal.getKey()) || (mapPrivate.containsKey(entryGlobal.getKey()) && mapPrivate.get(entryGlobal.getKey()) == null)) {
                jsonArrayConfig.add(new BaseConfigEntity(entryGlobal.getKey(), entryGlobal.getValue(), "0"));
            }
        }
        Iterator<Map.Entry<String, String>> itPrivate = mapPrivate.entrySet().iterator();
        while (itPrivate.hasNext()) {
            Map.Entry<String, String> entryPrivate = itPrivate.next();
            if(entryPrivate.getValue() != null) {
                jsonArrayConfig.add(new BaseConfigEntity(entryPrivate.getKey(), entryPrivate.getValue(), "1"));
            }
        }

        return jsonArrayConfig;
    }

}
