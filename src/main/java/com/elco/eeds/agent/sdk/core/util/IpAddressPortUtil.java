package com.elco.eeds.agent.sdk.core.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @title: IpAddressPortUtil
 * @Author wl
 * @Date: 2022/12/5 14:20
 * @Version 1.0
 */
public class IpAddressPortUtil {

    public static String getIpAddress() throws UnknownHostException {
        InetAddress localHost = InetAddress.getLocalHost();
        return localHost.toString().split("/")[1];
    }
}
