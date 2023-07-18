package com.elco.eeds.agent.sdk.core.util.resource;

import java.io.*;

/**
 * @ClassName FileSystemResource
 * @Description 文件资源类
 * @Author OuYang
 * @Date 2023/7/14 13:58
 * @Version 1.0
 */
public class FileSystemResource implements Resource, Serializable {
    private final File file;

    private final String path;

    public FileSystemResource(File file) {
        this.file = file;
        this.path = file.getPath();
    }
    public FileSystemResource(String path) {
        this.path = path;
        this.file = new File(path);
    }

    @Override
    public InputStream getStream() throws IOException {
        return new FileInputStream(this.file);
    }
}
