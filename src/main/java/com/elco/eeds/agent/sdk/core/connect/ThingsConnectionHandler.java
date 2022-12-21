package com.elco.eeds.agent.sdk.core.connect;


import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesContext;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import com.elco.eeds.agent.sdk.core.parsing.DataParsing;
import com.elco.eeds.agent.sdk.transfer.beans.things.EedsThings;
import com.elco.eeds.agent.sdk.transfer.beans.things.ThingsDriverContext;
import com.elco.eeds.agent.sdk.transfer.service.data.RealTimePropertiesValueService;
import com.elco.eeds.agent.sdk.transfer.service.things.ThingsSyncServiceImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author ：ytl
 * @date ：Created in 2022/12/2 15:25
 * @description：
 */
public abstract class ThingsConnectionHandler<T,M extends DataParsing>{


    private ThingsConnection thingsConnection;

    public ThingsConnection getThingsConnection() {
        return thingsConnection;
    }

    public void setThingsConnection(ThingsConnection thingsConnection) {
        this.thingsConnection = thingsConnection;
    }

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




    private  String thingsId;

    public String getThingsId() {
        return thingsId;
    }

    public void setThingsId(String thingsId) {
        this.thingsId = thingsId;
    }

    private ThingsDriverContext context;

    public ThingsDriverContext getContext() {
        return context;
    }

    public void setContext(ThingsDriverContext context) {
        this.context = context;
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

    public void command(EedsThings things,String command){
        this.write(things,this.parsing.parsingCommand(command));
    }


    public abstract void read(List<PropertiesContext> properties);


    public abstract void write(EedsThings things,String msg);

    /**
     * 执行模板方法
     * @param thingsId
     * @param msg
     * @param collectTime
     */
    public void execute(String thingsId,String msg,Long collectTime){
        List<PropertiesValue> valueList = this.getParsing()
                .parsing(this.context,ThingsSyncServiceImpl.getThingsPropertiesContextList(thingsId), msg);
        RealTimePropertiesValueService.recRealTimePropertiesValue(msg,thingsId,collectTime,valueList);
    }






}
