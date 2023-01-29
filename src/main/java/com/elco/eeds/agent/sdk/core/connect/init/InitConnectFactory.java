package com.elco.eeds.agent.sdk.core.connect.init;

import cn.hutool.core.util.ReflectUtil;
import com.elco.eeds.agent.sdk.core.connect.ThingsConnection;
import com.elco.eeds.agent.sdk.core.connect.manager.ConnectManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TreeSet;

/**
 * @author ：ytl
 * @date ：Created in 2022/12/4 15:03
 * @description：
 */
public class InitConnectFactory {
    public static final Logger logger = LoggerFactory.getLogger(InitConnectFactory.class);

    private static TreeSet<String> CONNECT_PACKAGE_PATH =new TreeSet<>();



    public static void initConnect(){
        logger.info("开始初始化协议");
        logger.info("协议地址：{}",CONNECT_PACKAGE_PATH);
        CONNECT_PACKAGE_PATH.stream().forEach(t->{
            ThingsConnection connect = ReflectUtil.newInstance(t);
            logger.info("添加协议：{}",t);
            ConnectManager.addConnection(connect.protocolName(),t);
        });

//        List<Class> connectClassList = CreatorUtils.getAllInterfaceAchieveClass(ThingsConnection.class, CONNECT_PACKAGE_PATH);
//
//        connectClassList.stream().forEach(t->{
//
////            ThingsConnection connect = (ThingsConnection) new ConnectProxy(ReflectUtil.newInstance(t)).getProxyInstance();
//            ThingsConnection connect =(ThingsConnection) ReflectUtil.newInstance(t);
//            logger.info("添加协议：{}",t.getName());
//            ConnectManager.addConnection(connect.protocolName(),t.getName());
//        });




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
