package com.elco.eeds.agent.sdk.core.connect.proxy;

import com.elco.eeds.agent.sdk.Main;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ：ytl
 * @date ：Created in 2022/12/16 11:50
 * @description：连接动态代理
 */
public class ConnectProxy {
    public static final Logger logger = LoggerFactory.getLogger(Main.class);
    private Object target;

    public ConnectProxy(Object target) {
        this.target = target;
    }
    public Object getProxyInstance() {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                        Object invoke = method.invoke(target, args);


                        if("connect".equals(method.getName())){
                            Boolean bool = (Boolean)invoke;
                            if(bool){
                                logger.debug("创建连接成功：{}",method.getName());
//                                ThingsStatusMessage.sendConnectMsg("1","2");
                            }
                        }
                        return invoke;
                    }

                });
    }
}
