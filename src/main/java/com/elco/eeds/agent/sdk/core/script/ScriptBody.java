package com.elco.eeds.agent.sdk.core.script;

import cn.hutool.core.util.ObjectUtil;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
        HashMap<String,Object>argMap = new HashMap<>();
        argMap.put("a",1);
        argMap.put("b",2);
        argMap.put("c",3);
        ScriptBody scriptBody =new ScriptBody("a+b+c",argMap);
        System.out.println(scriptBody.doEval());
    }


}
