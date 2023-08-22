package com.elco.eeds.agent.sdk.core.util.resource;

import com.elco.eeds.agent.sdk.core.util.ClassUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * @ClassName ClassPathResource
 * @Description 类路径资源类
 * @Author OuYang
 * @Date 2023/7/14 13:59
 * @Version 1.0
 */
public class ClassPathResource implements Resource, Serializable {

    private final String path;

    private ClassLoader classLoader;

    public ClassPathResource(String path) {
        this(path, null);
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
        this.path = path;
        this.classLoader = (classLoader == null ? ClassUtils.getDefaultClassLoader() : classLoader);
    }

    @Override
    public InputStream getStream() throws IOException {
        InputStream inputStream = classLoader.getResourceAsStream(path);
        if (null != inputStream) {
            return inputStream;
        }
        throw new FileNotFoundException(this.path + "cannot be opened because it does not exist");
    }
}
