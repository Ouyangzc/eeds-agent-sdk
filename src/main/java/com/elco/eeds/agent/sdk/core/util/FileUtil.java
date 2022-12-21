package com.elco.eeds.agent.sdk.core.util;

import cn.hutool.core.util.ObjectUtil;
import com.elco.eeds.agent.sdk.core.common.constant.ConstantFilePath;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @version V1.0
 * @className FileUtil
 * @description 文件工具包
 * @date 2022/09/06 13:30
 **/
public class FileUtil {


    /**
     * 读取目录下的所有文件
     *
     * @param dir       目录
     * @param fileNames 保存文件名的集合
     * @return
     */
    public static void loadFile(File dir, List<String> fileNames) {
        // 判断是否存在目录
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 读取目录下的所有目录文件信息
        String[] files = dir.list();
        //缓存file时间
        Map<File, File> map = new HashMap<>();
        // 循环，添加文件名或回调自身
        for (int i = 0; i < files.length; i++) {
            File file = new File(dir, files[i]);
            // 如果文件
            if (file.isFile()) {
                String thingsId = dir.getName();
                //文件名 时间戳
                Long currentFileName = Long.valueOf(files[i].replace(ConstantFilePath.FILE_FORMAT_JSON, "").trim());
                File cacheFile = RealTimeDataMessageFileUtils.fileMap.get(thingsId);
                if (ObjectUtil.isEmpty(cacheFile)) {
                    RealTimeDataMessageFileUtils.fileMap.put(thingsId, file);
                } else {
                    Long currentTime = Long.valueOf(cacheFile.getName().replace(ConstantFilePath.FILE_FORMAT_JSON, "").trim());
                    if (currentFileName > currentTime) {
                        RealTimeDataMessageFileUtils.fileMap.put(thingsId, file);
                    }
                }
                map.put(file, file);
                fileNames.add(dir + File.separator + file.getName());
                RealTimeDataMessageFileUtils.fileReadMap.put(thingsId, map);
            } else {
                // 如果是目录，回调自身继续查询
                loadFile(file, fileNames);
            }
        }
    }

    /**
     * 返回文件大小
     *
     * @param file 文件
     * @return 文件大小 单位byte
     * 1KB = 1024B
     * 1MB = 1024KB
     */
    public static Long getFileLength(File file) {
        return file.length();
    }

    /**
     * 获取大小
     *
     * @param fileSize 存储文件大小 单位MB
     * @return 字节 1*1024*1024
     */
    public static Long getFileSize(String fileSize) {
        long size = Long.valueOf(fileSize) * 1048576L;
        return size;
    }



}
