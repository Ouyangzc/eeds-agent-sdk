package com.elco.eeds.agent.sdk.core.connect;


import com.elco.eeds.agent.sdk.core.parsing.DataParsing;
import com.elco.eeds.agent.sdk.transfer.beans.things.ThingsDriverContext;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author ：ytl
 * @date ：Created in 2022/12/2 15:25
 * @description：
 */
public abstract class ThingsConnectionHandler<T,M extends DataParsing>{

    public ThingsConnectionHandler(){
        attach();
    }
    public void attach(){

        //1、返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type
        Type genType = getClass().getGenericSuperclass();
        //2、泛型参数
        Type[] types = ((ParameterizedType) genType).getActualTypeArguments();
        //3、因为BasePresenter 有两个泛型 数组有两个
        try {
            //
            parsing= (M) ((Class)types[1]).newInstance();
            //这里需要强转得到的是实体类类路径
//            如果types[1].getClass().newInstance();并不行，得到的是泛型类型
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }




    private  String tingsId;

    private ThingsDriverContext context;

    public ThingsDriverContext getContext() {
        return context;
    }

    public void setContext(ThingsDriverContext context) {
        this.context = context;
    }

    public String getTingsId() {
        return tingsId;
    }

    public void setTingsId(String tingsId) {
        this.tingsId = tingsId;
    }

    private String handlerName;

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }


    public T getMaster() {
        return master;
    }

    public void setMaster(T master) {
        this.master = master;
    }


    private T master;

    private M parsing ;

    public M getParsing() {
        return parsing;
    }

    public void setParsing(M parsing) {
        this.parsing = parsing;
    }

    public int schedule() {
        return 0;
    }

    public void command(String topic,String command){
        this.write(topic,this.parsing.parsingCommand(command));
    }


    public abstract void read();


    public abstract void write(String topic,String msg);



    public void execute(String msg){



// parseMsg(Map<String,List<PropertiesContext>> propertiesContexts,String message);
        // 执行数据解析
        // get 数据源ID的点位  return List<PropertiesContext>

        this.getParsing().parsing();
        //   parseMsg(List<PropertiesContext> propertiesContexts,String message)  return List<PropertiesValue>
        // 执行本地缓存
        // local.save(msg)
        // 执行统计以及数据上报
            //report(List<PropertiesValue> )

    }

    public void execute(String clientId,String topic ,String msg){



// parseMsg(Map<String,List<PropertiesContext>> propertiesContexts,String message);
        // 执行数据解析
        // get 数据源ID的点位  return List<PropertiesContext>

        this.getParsing().parsing();
        //   parseMsg(List<PropertiesContext> propertiesContexts,String message)  return List<PropertiesValue>
        // 执行本地缓存
        // local.save(msg)
        // 执行统计以及数据上报
        //report(List<PropertiesValue> )

    }

}
