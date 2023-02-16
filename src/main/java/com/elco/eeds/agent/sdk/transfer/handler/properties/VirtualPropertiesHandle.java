package com.elco.eeds.agent.sdk.transfer.handler.properties;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesContext;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import com.elco.eeds.agent.sdk.core.connect.ThingsConnectionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
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
     * @param propertiesContextList 本地json数据
     * @param valueList 实际实时数据集合
     * @param collectTime 采集时间戳
     */
    public static void creatVirtualProperties(List<PropertiesContext> propertiesContextList, List<PropertiesValue> valueList, Long collectTime){
        if(ObjectUtil.isNotEmpty(valueList)){
            // 查询本地json数据是否存在虚拟变量
            List<PropertiesContext> proList = propertiesContextList.stream().filter(f -> f.getIsVirtual() == VIRTUAL).collect(Collectors.toList());
            if(ObjectUtil.isNotEmpty(proList)){
                proList.stream().forEach(temp->{
                    PropertiesValue propertiesValue = new PropertiesValue();
                    BeanUtil.copyProperties(temp, propertiesValue);
                    long startTime1 = System.currentTimeMillis();
                    // 构建虚拟变量值
                    creatValue(temp, propertiesValue, valueList);
                    long time1 = System.currentTimeMillis()-startTime1;
                    logger.debug("虚拟变量数据js引擎处理耗时，time:{}",time1);
                    propertiesValue.setTimestamp(collectTime);
                    propertiesValue.setIsVirtual(VIRTUAL);
                    valueList.add(propertiesValue);
                });
            }
        }
    }

    /**
     * 构建虚拟变量值
     * @param propertiesContext 本地变量json数据
     * @param propertiesValue 虚拟变量值信息
     * @param valueList 实际变量实时数据集合
     * @return
     */
    private static void creatValue(PropertiesContext propertiesContext, PropertiesValue propertiesValue, List<PropertiesValue> valueList){
        // 虚拟变量相关的实际变量ID集合
        String relationIds = propertiesContext.getRelationIds();
        if(ObjectUtil.isNotEmpty(relationIds)){
            // 根据实际变量计算虚拟变量的值
            toCalculate(propertiesContext.getType(), propertiesContext.getDefaultValue(), propertiesContext.getExpression(), relationIds, propertiesValue, valueList);
        } else {
            // 赋默认值
            propertiesValue.setValue(propertiesContext.getDefaultValue());
        }
    }

    /**
     * 计算虚拟变量值，并赋值到valueList
     * @param type 变量类型
     * @param defaultValue 默认值
     * @param expression 表达式
     * @param relationIds 虚拟变量相关的实际变量ID集合
     * @param propertiesValue 虚拟变量值信息
     * @param valueList 实际变量实时数据集合
     */
    private static void toCalculate(String type, String defaultValue, String expression, String relationIds, PropertiesValue propertiesValue, List<PropertiesValue> valueList){
        // 虚拟变量相关的实际变量ID集合
        List<String> realIds = Arrays.asList(relationIds.split(","));
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine engine = sem.getEngineByName("js");
        realIds.stream().forEach(temp->{
            List<PropertiesValue> collect = valueList.stream().filter(f -> f.getPropertiesId().equals(temp)).collect(Collectors.toList());
            if(ObjectUtil.isNotEmpty(collect)){
                String value = collect.get(0).getValue();
                conversionValue(engine, temp, value);
            }
        });
        try {
            Object eval = engine.eval(expression);
            propertiesValue.setValue(conversionType(eval, type));
        } catch (Exception e) {
            e.printStackTrace();
            propertiesValue.setValue(defaultValue);
        }
    }

    /**
     * 根据实际变量值转换对应的值对象
     * @param engine js引擎操作对象
     * @param realPropertiesId 实际变量ID
     * @param value 实际变量值
     */
    private static void conversionValue(ScriptEngine engine, String realPropertiesId, String value) {

        try {
            if(value.indexOf(CHARACTER)>-1){
                engine.put(PREFIX+"_"+realPropertiesId, Float.parseFloat(value));
            } else if(TRUE.equals(value.toUpperCase()) || FALSE.equals(value.toUpperCase())){
                engine.put(PREFIX+"_"+realPropertiesId, Boolean.valueOf(value));
            } else {
                engine.put(PREFIX+"_"+realPropertiesId, Integer.valueOf(value));
            }
        } catch (Exception e){
            e.printStackTrace();
            engine.put(PREFIX+"_"+realPropertiesId, value);
        }

    }

    /**
     * 结果值转换类型
     * @param eval js计算后的值
     * @param type 虚拟变量类型
     * @return
     */
    private static String conversionType(Object eval, String type){
        String value = "";
        if(TRUE.equals(eval.toString().toUpperCase()) || FALSE.equals(eval.toString().toUpperCase())){
            value = eval.toString();
        } else {
            if(type.indexOf(INT) > -1 || type.indexOf(UINT) > -1){
//                Double evalD = (Double)eval;
                if(eval.toString().indexOf(CHARACTER) > -1){
                    value = eval.toString().substring(0,eval.toString().indexOf(CHARACTER));
                } else {
                    value = eval.toString();
                }
            } else {
                value = eval.toString();
            }
//            if(type.indexOf(FLOAT32) > -1) {
//                String evalStr = eval.toString();
//                if(evalStr.length() > FLOAT32_MAX_LENGTH){
//                    value = evalStr.substring(0,FLOAT32_MAX_LENGTH);
//                } else {
//                    value = eval.toString();
//                }
//            } else if(type.indexOf(FLOAT64) > -1) {
//                String evalStr = eval.toString();
//                if(evalStr.length() > FLOAT64_MAX_LENGTH){
//                    value = evalStr.substring(0,FLOAT64_MAX_LENGTH);
//                } else {
//                    value = eval.toString();
//                }
//            }
        }
        return value;
    }

    public static void main(String[] args) {
        String  a  = "1.123456910101016";
        System.out.println(a.substring(0,a.indexOf(".")));
    }

}
