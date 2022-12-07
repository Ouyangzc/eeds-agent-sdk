package com.elco.eeds.agent.sdk.core.util.read.parameterfile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @title: Resource
 * @Author wl
 * @Date: 2022/12/6 11:09
 * @Version 1.0
 * @Description: 资源定位接口
 */
public interface Resource {

    /**
     * 获取Stream流
     * @return
     */
    InputStream getInputStream() throws IOException;

}
