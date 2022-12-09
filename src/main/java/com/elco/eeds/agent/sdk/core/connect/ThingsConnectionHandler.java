package com.elco.eeds.agent.sdk.core.connect;


import com.elco.eeds.agent.sdk.core.parsing.DataParsing;

/**
 * @author ：ytl
 * @date ：Created in 2022/12/2 15:25
 * @description：
 */
public abstract class ThingsConnectionHandler<T,M extends DataParsing> {

    private  String tingsId;



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

    private M parsing;

    public M getParsing() {
        return parsing;
    }

    public void setParsing(M parsing) {
        this.parsing = parsing;
    }

    public int schedule() {
        return 0;
    }

    ;


    public abstract void read();


    public abstract void write();



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
