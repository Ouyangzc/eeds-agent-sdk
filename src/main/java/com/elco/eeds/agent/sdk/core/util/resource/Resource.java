package com.elco.eeds.agent.sdk.core.util.resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName Resource
 * @Description 资源接口定义
 * @Author OuYang
 * @Date 2023/7/14 13:49
 * @Version 1.0
 */
public interface Resource {
    /**
     * 获取inputstream
     * @return
     * @throws IOException
     */
    InputStream getStream()throws IOException;
}
