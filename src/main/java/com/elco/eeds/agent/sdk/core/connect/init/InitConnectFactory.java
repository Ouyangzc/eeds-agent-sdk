package com.elco.eeds.agent.sdk.core.connect.init;

import cn.hutool.core.util.ReflectUtil;
import com.elco.eeds.agent.sdk.core.connect.ThingsConnection;
import com.elco.eeds.agent.sdk.core.connect.manager.ConnectManager;
import com.elco.eeds.agent.sdk.core.util.CreatorUtils;

import java.util.List;
import java.util.TreeSet;

/**
 * @author ：ytl
 * @date ：Created in 2022/12/4 15:03
 * @description：
 */
public class InitConnectFactory {


    private static TreeSet<String> CONNECT_PACKAGE_PATH =new TreeSet<>();


    public static void initConnect(){
        List<Class> connectClassList = CreatorUtils.getAllInterfaceAchieveClass(ThingsConnection.class, CONNECT_PACKAGE_PATH);
        connectClassList.stream().forEach(t->{

//            ThingsConnection connect = (ThingsConnection) new ConnectProxy(ReflectUtil.newInstance(t)).getProxyInstance();
            ThingsConnection connect =(ThingsConnection) ReflectUtil.newInstance(t);
            ConnectManager.addConnection(connect.protocolName(),connect);
        });




    }

    public static void addConnectPackagePath(String path){
        CONNECT_PACKAGE_PATH.add(path);
    }


    public void initScheduled(){
//        long initialDelay = 1;
//        long period = 1;
//        ConnectManager.getAllHandler().stream().forEach(t->{
//            if(t.schedule()>0){
//                new MyScheduledExecutor(t.getHandlerName(), initialDelay, period, TimeUnit.SECONDS)
//            }
//
//        });

    }
}
