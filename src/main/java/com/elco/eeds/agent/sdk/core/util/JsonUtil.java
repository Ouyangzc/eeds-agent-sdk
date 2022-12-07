package com.elco.eeds.agent.sdk.core.util;

import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * @title: JsonUtil
 * @Author wl
 * @Date: 2022/12/7 16:51
 * @Version 1.0
 */
public class JsonUtil {

    /**
     *
     * @param jsonArray
     * @return
     */
    public static String[] jsonArray2StringArray(JSONArray jsonArray) {
        List<String> list = new ArrayList<>();
        String[] stringArray;
        for (Object o : jsonArray) {
            list.add((String) o);
        }
        return list.toArray(new String[list.size()]);
    }

}
