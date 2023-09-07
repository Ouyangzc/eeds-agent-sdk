package com.elco.eeds.agent.sdk.core.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentBaseInfo;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesContext;
import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesValue;
import com.elco.eeds.agent.sdk.core.common.constant.ConstantFilePath;
import com.elco.eeds.agent.sdk.core.common.enums.ErrorEnum;
import com.elco.eeds.agent.sdk.core.connect.ThingsConnectionHandler;
import com.elco.eeds.agent.sdk.core.connect.manager.ConnectManager;
import com.elco.eeds.agent.sdk.core.exception.SdkException;
import com.elco.eeds.agent.sdk.core.parsing.DataParsing;
import com.elco.eeds.agent.sdk.transfer.beans.data.OriginalPropertiesValueMessage;
import com.elco.eeds.agent.sdk.transfer.beans.data.sync.DataSyncServerRequest;
import com.elco.eeds.agent.sdk.transfer.beans.things.ThingsDriverContext;
import com.elco.eeds.agent.sdk.transfer.handler.properties.VirtualPropertiesHandle;
import com.elco.eeds.agent.sdk.transfer.quartz.DataFileJob;
import com.elco.eeds.agent.sdk.transfer.service.things.ThingsSyncNewServiceImpl;
import com.elco.eeds.agent.sdk.transfer.service.things.ThingsSyncServiceImpl;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName RealTimeDataMessageFileUtils
 * @Description 实时数据文件类
 * @Author OUYANG
 * @Date 2022/12/20 13:58
 */
public class RealTimeDataMessageFileUtils {

    public static final Logger logger = LoggerFactory.getLogger(RealTimeDataMessageFileUtils.class);

    public static Map<String, File> fileMap = new ConcurrentHashMap<>(128);
    public static Map<String, Map<File, File>> fileReadMap = new ConcurrentHashMap<>();
    private static final int REAL = 1;

    /**
     * 获取该数据源的写文件
     *
     * @param thingsId 数据源ID
     * @return 文件对象
     */
    private static File getCurrentWriteFile(String thingsId, Long collectTime) {
        File file = fileMap.get(thingsId);
        if (!ObjectUtil.isEmpty(file)) {
            //文件名
            String fileName = file.getName().replace(ConstantFilePath.FILE_FORMAT_JSON, "");
            boolean sameDay = DateUtil.isSameDay(DateUtil.date(Long.valueOf(fileName)), DateUtil.date(collectTime));
            if (sameDay && file.exists()) {
                //采集时间和文件归属于同一天,返回已有file对象
                return file;
            } else {
                //采集时间和文件不归属于同一天
                String filePath = getNewFilePath(thingsId);
                File newFile = new File(filePath);
                fileMap.put(thingsId, newFile);
                //加载到过期文件map缓存
                DataFileJob.saveFileToMap(newFile);
                return newFile;
            }
        }
        String filePath = getNewFilePath(thingsId);
        File newFile = new File(filePath);
        fileMap.put(thingsId, newFile);
        //加载到过期文件map缓存
        DataFileJob.saveFileToMap(newFile);
        return newFile;
    }

    private static String getNewFilePath(String thingsId) {
        long timeMillis = System.currentTimeMillis();
        String dateStr = DateUtil.date().toDateStr();
        AgentBaseInfo agentBaseInfo = Agent.getInstance().getAgentBaseInfo();
        String baseFolder = agentBaseInfo.getBaseFolder();
        String fileFolder = baseFolder + ConstantFilePath.PROPERTIES_DATA_FOLDER + File.separator + thingsId + File.separator + dateStr;
        File file = new File(fileFolder);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileFullPath = fileFolder + File.separator + timeMillis + ConstantFilePath.FILE_FORMAT_JSON;
        return fileFullPath;
    }

    public static List<PropertiesValue> getFileData(DataSyncServerRequest request, Long startTime, Long endTime, List<PropertiesContext> propertiesContextList) {
        List<PropertiesValue> result = new ArrayList<>();
        String thingsId = request.getThingsId();
        try {
            Map<File, File> fileMap = fileReadMap.get(thingsId);
            if (ObjectUtil.isEmpty(fileMap)) {
                logger.info("数据同步：未获取到数据源文件:{}", thingsId);
                return result;
            }
            ThingsDriverContext driverContext = ThingsSyncNewServiceImpl.THINGS_DRIVER_CONTEXT_MAP.get(thingsId);
            for (File key : fileMap.keySet()) {
                Long fileStartTime = Long.valueOf(key.getName().replace(ConstantFilePath.FILE_FORMAT_JSON, "")) - 1000L;
                Long fileEndTime = key.lastModified() + 1000L;
                if (startTime < fileEndTime && endTime > fileStartTime) {
                    //必定有重合
                    List<String> dataList = FileUtils.readLines(key, StandardCharsets.UTF_8);
                    for (String data : dataList) {
                        //调用解析方法
                        OriginalPropertiesValueMessage originalMessage = JSON.parseObject(data, OriginalPropertiesValueMessage.class);
                        Long collectTime = originalMessage.getCollectTime();
                        if (startTime <= collectTime && collectTime < endTime) {
                            String message = originalMessage.getMessage();
                            ThingsConnectionHandler handler = ConnectManager.getHandler(thingsId);
                            DataParsing dataParsing;
                            if (ObjectUtil.isEmpty(handler)) {
                                dataParsing = Agent.getInstance().getDataParsing();
                                logger.info("数据同步：数据源已断开:{},获取默认解析实例:{}", thingsId, dataParsing.toString());
                                if (ObjectUtil.isEmpty(dataParsing)) {
                                    throw new SdkException(ErrorEnum.THINGS_CONNECT_NOT_EXIST);
                                }
                            } else {
                                dataParsing = handler.getParsing();
                            }
                            if (ObjectUtil.isEmpty(driverContext)) {
                                driverContext = new ThingsDriverContext();
                                BeanUtil.copyProperties(request, driverContext);
                            }
                            List<PropertiesValue> propertiesValueList = dataParsing.parsing(driverContext, propertiesContextList, message);
                            logger.info("数据同步：原始数据文件,file:{},数据源:{},获取数据大小，size:{}，driverContext:{}", key, thingsId, propertiesValueList.size(), JSONUtil.toJsonStr(driverContext));
                            propertiesValueList.forEach(pv -> {
                                pv.setTimestamp(collectTime);
                                pv.setIsVirtual(REAL);
                            });
                            VirtualPropertiesHandle.creatVirtualProperties(propertiesContextList, propertiesValueList, collectTime);
                            result.addAll(propertiesValueList);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.info("数据同步：获取原始报文解析错误,error:{}", e);
        }
        logger.info("数据同步：同步数据源id:{},开始时间:{},结束时间：{},获取同步原始报文大小:{}", thingsId, startTime, endTime, result.size());
        return result;
    }


    public static volatile boolean lock = false;

    /**
     * 需求说明
     * 按照Server端设定文件大小，分割每个文件
     * 以时间戳为文件名（开始时间），文件的修改时间（截至时间）
     *
     * @param thingsId 数据源ID 文件目录
     * @param data     数据
     */
    public static void writeAppend(String thingsId, String data, Long collectTime) {
        //写文件
        try {
            File file = getCurrentWriteFile(thingsId, collectTime);
            //文件大小
            Long fileLength = FileUtil.getFileLength(file);

            AgentBaseInfo agentBaseInfo = Agent.getInstance().getAgentBaseInfo();
            String dataCacheFileSize = agentBaseInfo.getDataCacheFileSize();
            Long fileSize = FileUtil.getFileSize(dataCacheFileSize);
            if (fileLength > fileSize) {
                // 多并发下 再校验一下文件是不是被替换了
                File oldFile = getCurrentWriteFile(thingsId, collectTime);
                if (oldFile.getName().equals(file.getName())) {
                    //需创建新文件
                    String filePath = getNewFilePath(thingsId);
                    file = new File(filePath);
                    fileMap.put(thingsId, file);
                    //加载到过期文件map缓存
                    DataFileJob.saveFileToMap(file);
                }
            }
            StringBuffer dataBuffer = new StringBuffer(data);
            dataBuffer.append("\r\n");
            NIOFileUtils fileUtils = new NIOFileUtils(file.getAbsolutePath());
            fileUtils.writeLines(dataBuffer.toString(), dataBuffer.toString().getBytes().length, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据时间删除文件和缓存
     *
     * @param agentBaseFileCycle
     */
    public static void removeFile(String agentBaseFileCycle) {
        int offset = Integer.valueOf("-" + agentBaseFileCycle);
        DateTime newDate2 = DateUtil.offsetDay(new Date(), offset);
        long nowTime = newDate2.getTime();
        AgentBaseInfo agentBaseInfo = Agent.getInstance().getAgentBaseInfo();
        String baseFolder = agentBaseInfo.getBaseFolder();
        String fileFolder = baseFolder + ConstantFilePath.PROPERTIES_DATA_FOLDER;
        removeFile(new File(fileFolder), nowTime);
    }

    public static void removeFile(File dir, Long nowTime) {
        // 判断是否存在目录
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 读取目录下的所有目录文件信息
        String[] files = dir.list();
        // 循环，添加文件名或回调自身
        for (int i = 0; i < files.length; i++) {
            File file = new File(dir, files[i]);
            // 如果文件
            if (file.isFile()) {
                //文件名 时间戳
                long fileEndTime = file.lastModified();
                if (nowTime >= fileEndTime) {
                    FileUtils.deleteQuietly(file);
                }
            } else {
                // 如果是目录，回调自身继续查询
                removeFile(file, nowTime);
            }
        }
    }

    /**
     * 以天为周期删除文件
     *
     * @param agentBaseFileCycle
     */
    public static void removeDayFile(Integer agentBaseFileCycle) {
        try {
            int offset = Integer.valueOf("-" + agentBaseFileCycle);
            DateTime newDate2 = DateUtil.offsetDay(new Date(), offset);
            long nowTime = newDate2.getTime();
            DateTime dateEnd = DateUtil.offset(DateUtil.date(), DateField.DAY_OF_MONTH, -agentBaseFileCycle);
            AgentBaseInfo agentBaseInfo = Agent.getInstance().getAgentBaseInfo();
            String baseFolder = agentBaseInfo.getBaseFolder();
            String fileFolder = baseFolder + ConstantFilePath.PROPERTIES_DATA_FOLDER;
            removeFile(new File(fileFolder), dateEnd, nowTime);
        } catch (Exception e) {
            logger.error("删除数据文件失败，异常信息:{}", e.getMessage());
        }
    }


    public static void removeFile(File dir, DateTime dateEnd, Long nowTime) throws IOException {
        // 判断是否存在目录
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 读取目录下的所有目录文件信息
        String[] files = dir.list();
        // 循环，添加文件名或回调自身
        for (int i = 0; i < files.length; i++) {
            File file = new File(dir, files[i]);
            if (file.isDirectory()) {
                // 如果是目录，
                String name = file.getName();
                if (name.contains("-")) {
                    DateTime dateTime = DateUtil.parse(name);
                    int compare = DateUtil.compare(dateTime, dateEnd);
                    if (compare < 1) {
                        File dataFiles = new File(dir, files[i]);
                        File[] listFiles = dataFiles.listFiles();
                        for (File dataFile : listFiles) {
                            //文件名 时间戳
                            long fileEndTime = dataFile.lastModified();
                            if (nowTime >= fileEndTime) {
                                logger.info("删除文件:{}", dataFile.getAbsolutePath());
                                FileUtils.deleteQuietly(dataFile);
                            }
                        }
                        if (dataFiles.listFiles().length <= 0) {
                            logger.info("文件目录为空，删除目录:{}", dataFiles.getAbsolutePath());
                            FileUtils.deleteDirectory(dataFiles);
                        }
                    }
                }
                // 如果是目录，回调自身继续查询
                removeFile(file, dateEnd, nowTime);
            }
        }
    }


    /**
     * 以分钟为周期删除文件
     *
     * @param agentBaseFileCycle
     */
    public static void removeMinuteFile(Integer agentBaseFileCycle) {
        try {
            int offset = Integer.valueOf("-" + agentBaseFileCycle);
            DateTime newDate2 = DateUtil.offsetMinute(new Date(), offset);
            long nowTime = newDate2.getTime();
            DateTime dateEnd = DateUtil.offset(DateUtil.date(), DateField.MINUTE, -agentBaseFileCycle);
            AgentBaseInfo agentBaseInfo = Agent.getInstance().getAgentBaseInfo();
            String baseFolder = agentBaseInfo.getBaseFolder();
            String fileFolder = baseFolder + ConstantFilePath.PROPERTIES_DATA_FOLDER;
            removeFile(new File(fileFolder), dateEnd, nowTime);
        } catch (Exception e) {
            logger.error("删除数据文件失败，异常信息:{}", e.getMessage());
        }
    }
}