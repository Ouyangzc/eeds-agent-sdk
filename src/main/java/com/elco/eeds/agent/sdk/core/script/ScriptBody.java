package com.elco.eeds.agent.sdk.core.script;

import cn.hutool.core.util.ObjectUtil;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import com.elco.eeds.agent.sdk.core.script.domain.PropertiesInfo;
import com.elco.eeds.agent.sdk.core.script.domain.Virtual;
import com.elco.eeds.agent.sdk.core.script.impl.JsInvokeServiceImpl;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.*;

/**
 * @author ：ytl
 * @date ：Created in 2023/1/9 14:49
 * @description：
 */
public class ScriptBody {

    public ScriptBody(){

    }

    public ScriptBody(String scriptBody,HashMap<String,Object>argMap){
        this.scriptBody =scriptBody;
        this.argMap = argMap;
    }

    public static  ScriptEngine engine;
    static {
        //获得脚本引擎对象
        ScriptEngineManager sem = new ScriptEngineManager();
        engine = sem.getEngineByName("js");
    }

    private String scriptBody;

    private HashMap<String,Object>argMap;

    public String getScriptBody() {
        return scriptBody;
    }

    public void setScriptBody(String scriptBody) {
        this.scriptBody = scriptBody;
    }

    public HashMap<String, Object> getArgMap() {
        return argMap;
    }

    public void setArgMap(HashMap<String, Object> argMap) {
        this.argMap = argMap;
    }

    public Object doEval(){
        if(ObjectUtil.isNotEmpty(this.argMap)){
            Iterator<Map.Entry<String, Object>> iterator = this.argMap.entrySet().iterator();
            while(iterator.hasNext()){
                //获取元素时使用的是EntryIterator中的next方法，在这个方法内部会调用父类的nextNode方法
                Map.Entry<String, Object> current = iterator.next();
                engine.put(current.getKey(), current.getValue());
            }
        }


        Object res =null;
        try {
            res = engine.eval(this.scriptBody);
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    public static void main(String[] args) {
        PropertiesInfo propertiesInfo1 =new PropertiesInfo();
        propertiesInfo1.setAddress("201");
        propertiesInfo1.setPropertiesId("01");
        propertiesInfo1.setVariableName("a_01");

        PropertiesInfo propertiesInfo2 =new PropertiesInfo();
        propertiesInfo2.setAddress("202");
        propertiesInfo2.setPropertiesId("02");
        propertiesInfo2.setVariableName("a_02");


        PropertiesInfo propertiesInfo3 =new PropertiesInfo();
        propertiesInfo3.setAddress("203");
        propertiesInfo3.setPropertiesId("03");
        propertiesInfo3.setVariableName("a_03");

        List<PropertiesInfo> argsList = new ArrayList<>();
        argsList.add(propertiesInfo1);
        argsList.add(propertiesInfo2);
        argsList.add(propertiesInfo3);

        Virtual virtual = new Virtual();

        virtual.setAddress("101");
        virtual.setScriptBody("a_01+a_02+a_03");
        virtual.setArgsList(argsList);

        PropertiesValue propertiesValue1= new PropertiesValue();
        propertiesValue1.setAddress("201");
        propertiesValue1.setValue("20");

        PropertiesValue propertiesValue2= new PropertiesValue();
        propertiesValue2.setAddress("202");
        propertiesValue2.setValue("30");


        PropertiesValue propertiesValue3= new PropertiesValue();
        propertiesValue3.setAddress("203");
        propertiesValue3.setValue("60");


        List<PropertiesValue> propertiesValueList = new ArrayList<>();
        propertiesValueList.add(propertiesValue1);
        propertiesValueList.add(propertiesValue2);
        propertiesValueList.add(propertiesValue3);

        JsInvokeService jsInvokeService = new JsInvokeServiceImpl();

        String execute = jsInvokeService.execute(virtual, propertiesValueList);
        System.out.println(execute);
    }


}
