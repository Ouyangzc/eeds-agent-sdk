package com.elco.eeds.agent.sdk.core.util.http;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.elco.eeds.agent.sdk.core.util.AgentResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @title: IpUtil
 * @Author wl
 * @Date: 2022/12/7 13:43
 * @Version 1.0
 */
public class IpUtil {
    private static final Logger logger = LoggerFactory.getLogger(IpUtil.class);

    public static String getLocalIpAddress() {
        String configIp = AgentResourceUtils.getAgentConfigLocalIp();
        return StrUtil.isBlank(configIp) ? getLocalIp() : configIp;
    }

    public static String getLocalIp() {
        List<InetAddress> addressList = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if (networkInterface.isVirtual() || !networkInterface.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress address = inetAddresses.nextElement();
                    if (!address.isLinkLocalAddress() && !address.isLoopbackAddress() && address.isSiteLocalAddress() && address instanceof Inet4Address) {
                        addressList.add(address);
                    }
                }
            }

        } catch (SocketException e) {
            logger.error("获取本地IP地址失败,", e);
        }
        if (ObjectUtil.isEmpty(addressList)) {
            return "unknown";
        }
        logger.info("本地IP地址列表:{}", addressList);
        return addressList.get(addressList.size()-1).getHostAddress();
    }
}
