package com.elco.eeds.agent.sdk.core.util.read.parameterfile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @title: UrlResource
 * @Author wl
 * @Date: 2022/12/6 11:09
 * @Version 1.0
 */
public class UrlResource implements Resource {

    private final URL url;

    public UrlResource(URL url) {
        this.url = url;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(url.getFile());
    }
}
