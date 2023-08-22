package com.elco.eeds.agent.sdk.core.util.resource;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.elco.eeds.agent.sdk.core.util.FileUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName ResourceUtils
 * @Description 资源工具类
 * @Author OuYang
 * @Date 2023/7/14 14:04
 * @Version 1.0
 */
public class ResourceUtils {

    public static final String CLASSPATH_URL_PREFIX = "classpath:";

    public static InputStream getStream(String resurce) throws IOException {
        return getResourceObj(resurce).getStream();
    }

    public static Resource getResourceObj(String path) {
        if (StrUtil.isNotBlank(path)) {
            if (path.startsWith(URLUtil.FILE_URL_PREFIX) || FileUtil.isAbsolutePath(path)) {
                return new FileSystemResource(path);
            }
        }
        if (path.startsWith(CLASSPATH_URL_PREFIX)) {
            path = path.substring(CLASSPATH_URL_PREFIX.length());
        }
        return new ClassPathResource(path);
    }
}
