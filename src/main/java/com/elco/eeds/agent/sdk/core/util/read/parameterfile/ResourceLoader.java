package com.elco.eeds.agent.sdk.core.util.read.parameterfile;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @title: ResourceLoader
 * @Author wl
 * @Date: 2022/12/6 11:09
 * @Version 1.0
 */
public class ResourceLoader {

    public URL getResource(String location) {
        return this.getClass().getClassLoader().getResource(location);
    }

    /**
     * 绝对路径
     * @param location
     * @return
     * @throws MalformedURLException
     */
    public URL getResourceByAbsolutePath(String location) throws MalformedURLException {
        File file = new File(location);
        return file.toURI().toURL();
    }
}
