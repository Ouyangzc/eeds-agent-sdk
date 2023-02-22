package com.elco.eeds.agent.sdk.transfer.handler.properties;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesContext;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Projectname: eeds-agent-sdk
 * @Filename: VirtualPropertiesHandle
 * @Author: lyc
 * @Data:2023/2/7 11:40
 * @Des 虚拟变量操作类
 */
public class VirtualPropertiesHandle {

    public static final Logger logger = LoggerFactory.getLogger(VirtualPropertiesHandle.class);

    /**
     * 常量
     */
    private final static String PREFIX = "var";

    /**
     * 2：虚拟变量
     */
    private final static int VIRTUAL = 2;

    /**
     * 值转换包含字符
     */
    private final static String CHARACTER = ".";

    private final static String ERROR = "error";

    private final static String MATCHER = "[a-zA-Z]+";

    private final static String TRUE = "TRUE";

    private final static String FALSE = "FALSE";

    private final static String INT = "int";

    private final static String UINT = "uint";

    private final static String FLOAT32 = "float32";

    private final static String FLOAT64 = "float64";

    private final static int INT16_MAX_VALUE = 32767;

    private final static int INT16_MIN_VALUE = -32768;

    private final static int INT32_MAX_VALUE = 2147483647;

    private final static int INT32_MIN_VALUE = -2147483648;

    private final static String INT64_MAX_VALUE = "9223372036854775807";

    private final static String INT64_MIN_VALUE = "-9223372036854775808";


    /**
     * FLOAT32可以精确到小数6位，加上小数点和整数一共是8位
     */
    private final static int FLOAT32_MAX_LENGTH = 8;

    /**
     * FLOAT32可以精确到小数15位，加上小数点和整数一共是17位
     */
    private final static int FLOAT64_MAX_LENGTH = 17;

    /**
     * 构建虚拟变量数据
     *
     * @param propertiesContextList 本地json数据
     * @param valueList             实际实时数据集合
     * @param collectTime           采集时间戳
     */
    public static void creatVirtualProperties(List<PropertiesContext> propertiesContextList, List<PropertiesValue> valueList, Long collectTime) {
        // 查询本地json数据是否存在虚拟变量
        List<PropertiesContext> virtualList = propertiesContextList.stream().filter(f -> f.getIsVirtual() == VIRTUAL).collect(Collectors.toList());
        if (ObjectUtil.isNotEmpty(virtualList)) {
            virtualList.stream().forEach(virtualPro -> {
                PropertiesValue propertiesValue = new PropertiesValue();
                BeanUtil.copyProperties(virtualPro, propertiesValue);
                propertiesValue.setAddress("");
                long startTime1 = System.currentTimeMillis();
                // 构建虚拟变量值
                boolean flag = creatValue(virtualPro, propertiesValue, valueList);
                long time1 = System.currentTimeMillis() - startTime1;
                logger.info("虚拟变量数据js引擎处理耗时，time:{}", time1);
                if (flag) {
                    propertiesValue.setTimestamp(collectTime);
                    propertiesValue.setIsVirtual(VIRTUAL);
                    valueList.add(propertiesValue);
                }
            });
        }
    }

    /**
     * 构建虚拟变量值
     *
     * @param propertiesContext 本地变量json数据(虚拟变量)
     * @param propertiesValue   虚拟变量值信息
     * @param valueList         实际变量实时数据集合
     * @return true 需要发送的数据 false 不需要发送的数据
     */
    private static boolean creatValue(PropertiesContext propertiesContext, PropertiesValue propertiesValue, List<PropertiesValue> valueList) {
        // 虚拟变量相关的实际变量ID集合
        String relationIds = propertiesContext.getRelationIds();
        if (ObjectUtil.isNotEmpty(relationIds)) {
            // 根据实际变量计算虚拟变量的值
            return toCalculate(propertiesContext.getType(), propertiesContext.getDefaultValue(), propertiesContext.getExpression(), relationIds, propertiesValue, valueList);
        } else {
            // 赋默认值
            propertiesValue.setValue(propertiesContext.getDefaultValue());
            return true;
        }
    }

    /**
     * 计算虚拟变量值，并赋值到valueList
     *
     * @param type            变量类型
     * @param defaultValue    默认值
     * @param expression      表达式
     * @param relationIds     虚拟变量相关的实际变量ID集合
     * @param propertiesValue 虚拟变量值信息
     * @param valueList       实际变量实时数据集合
     * @return true 需要发送的数据 false 不需要发送的数据
     */
    private static boolean toCalculate(String type, String defaultValue, String expression, String relationIds, PropertiesValue propertiesValue, List<PropertiesValue> valueList) {
        // 虚拟变量相关的实际变量ID集合
        List<String> realIds = Arrays.asList(relationIds.split(","));
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine engine = sem.getEngineByName("js");
        // 记录发送的实际变量数量
        int num = 1;
        for (String temp : realIds) {
            List<PropertiesValue> collect = valueList.stream().filter(f -> f.getPropertiesId().equals(temp)).collect(Collectors.toList());
            if (ObjectUtil.isNotEmpty(collect)) {
                String value = collect.get(0).getValue();
                conversionValue(engine, temp, value);
                num++;
            }
        }

        // num==0 说明该虚拟变量关联的实际变量没有实时数据，不做计算
        // num.get() == realIds.size() 说明该虚拟变量关联的实际变量全部都有实时数据
        // num.get() != realIds.size() 说明该虚拟变量关联的实际变量缺失不是实时数据
        if (num == 1) {
            return false;
        } else {
            if ((num-1) == realIds.size()) {
                try {
                    Object eval = engine.eval(expression);
                    propertiesValue.setValue(conversionType(eval, type, propertiesValue.getPropertiesId()));
                } catch (Exception e) {
                    e.printStackTrace();
                    propertiesValue.setValue(ERROR);
                    logger.error("js引擎处理表达式报错:虚拟点位ID为:{},报错信息:{}", propertiesValue.getPropertiesId(), e);
                }
            } else {
                propertiesValue.setValue(defaultValue);
            }
            return true;
        }

    }

    /**
     * 根据实际变量值转换对应的值对象
     *
     * @param engine           js引擎操作对象
     * @param realPropertiesId 实际变量ID
     * @param value            实际变量值
     */
    private static void conversionValue(ScriptEngine engine, String realPropertiesId, String value) {

        try {
            if (value.indexOf(CHARACTER) > -1) {
                engine.put(PREFIX + "_" + realPropertiesId, Float.parseFloat(value));
            } else if (TRUE.equals(value.toUpperCase()) || FALSE.equals(value.toUpperCase())) {
                engine.put(PREFIX + "_" + realPropertiesId, Boolean.valueOf(value));
            } else {
                engine.put(PREFIX + "_" + realPropertiesId, Integer.valueOf(value));
            }
        } catch (Exception e) {
            e.printStackTrace();
            engine.put(PREFIX + "_" + realPropertiesId, value);
        }

    }

    /**
     * 结果值转换类型
     *
     * @param eval js计算后的值
     * @param type 虚拟变量类型
     * @return
     */
    private static String conversionType(Object eval, String type, String propertiesId) {
        String value = eval.toString();
        if (TRUE.equals(value.toUpperCase()) || FALSE.equals(value.toUpperCase())) {
            return value;
        } else {
            try {
                new BigDecimal(value);
            } catch (Exception e) {
                logger.error("js引擎处理计算错误值,虚拟点位ID为:{},值:{}", propertiesId, value);
                value = ERROR;
            }
        }
        return value;
    }

}
