package com.elco.eeds.agent.sdk.core.util.http;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @title: IpUtil
 * @Author wl
 * @Date: 2022/12/7 13:43
 * @Version 1.0
 */
public class IpUtil {

    public static String getLocalIpAddress() throws UnknownHostException {
        InetAddress localHost = InetAddress.getLocalHost();
        return localHost.toString().split("/")[1];
    }
}
