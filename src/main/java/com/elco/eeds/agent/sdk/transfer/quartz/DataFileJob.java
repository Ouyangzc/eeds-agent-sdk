package com.elco.eeds.agent.sdk.transfer.quartz;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentBaseInfo;
import com.elco.eeds.agent.sdk.core.common.constant.ConstantFilePath;
import org.apache.commons.io.FileUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName DataFileJob
 * @Description 过期文件删除任务
 * @Author ouyang
 * @Date 2023/2/21 14:56
 * @Version 1.0
 */
public class DataFileJob implements Job {
    public static Logger logger = LoggerFactory.getLogger(DataFileJob.class);

    public static Map<ExpireFileKey, List<FileInfo>> expireFileMap = new ConcurrentHashMap<>();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.debug("执行定时任务:{}", DataFileJob.class.getName());
        // 缓存周期
        AgentBaseInfo agentBaseInfo = Agent.getInstance().getAgentBaseInfo();
        Integer agentBaseFileCycle = Integer.valueOf(agentBaseInfo.getDataCacheCycle());
//        RealTimeDataMessageFileUtils.removeDayFile(Integer.valueOf(agentBaseFileCycle));
//		RealTimeDataMessageFileUtils.removeMinuteFile(Integer.valueOf(agentBaseFileCycle));
//        delExpireMinuteFile(agentBaseFileCycle);
        long startTime = System.currentTimeMillis();
        delExpireDayFile(agentBaseFileCycle);
        logger.info("删除文件，定时任务执行时间,ms:{}", System.currentTimeMillis() - startTime);

    }

    /**
     * 文件过期删除(单位:天)
     *
     * @param agentBaseFileCycle
     */
    public static void delExpireDayFile(Integer agentBaseFileCycle) {
        delExpireFile(agentBaseFileCycle, DateField.DAY_OF_MONTH);
    }

    public static void delExpireHourFile(Integer agentBaseFileCycle) {
        delExpireFile(agentBaseFileCycle, DateField.HOUR);
    }

    public static void delExpireMinuteFile(Integer agentBaseFileCycle) {
        delExpireFile(agentBaseFileCycle, DateField.MINUTE);
    }

    public static void delExpireFile(Integer agentBaseFileCycle, DateField dateField) {
        try {
            DateTime dateEnd = DateUtil.offset(DateUtil.date(), dateField, -agentBaseFileCycle);
            long nowTimestamp = dateEnd.getTime();
            ExpireFileKey expireFileKey = new ExpireFileKey(nowTimestamp);
            Iterator<Map.Entry<ExpireFileKey, List<FileInfo>>> entryIterator = expireFileMap.entrySet().iterator();
            while (entryIterator.hasNext()) {
                Map.Entry<ExpireFileKey, List<FileInfo>> fileKeyListEntry = entryIterator.next();
                ExpireFileKey key = fileKeyListEntry.getKey();
                if (key.compare(expireFileKey)) {
                    List<FileInfo> fileList = fileKeyListEntry.getValue();
                    Iterator<FileInfo> fileInfoIterator = fileList.iterator();
                    while (fileInfoIterator.hasNext()) {
                        FileInfo fileInfo = fileInfoIterator.next();
                        Long fileCreatTimeStamp = fileInfo.getFileName();
                        if (nowTimestamp >= fileCreatTimeStamp) {
                            File file = new File(fileInfo.getFilePath());
                            if (!file.exists()) {
                                //删除缓存
                                fileInfoIterator.remove();
                            } else {
                                FileUtils.deleteQuietly(file);
                                logger.info("删除文件:{},当前时间KEY:{},文件Key:{},时间戳：{},文件生成时间戳:{}", fileInfo.getFilePath(), expireFileKey.getKey(), key.getKey(), nowTimestamp, fileInfo.getFileName());
                                //空文件夹删除
                                if (new File(fileInfo.getParentFilePath()).listFiles().length == 0) {
                                    logger.info("文件目录为空，删除目录:{}", fileInfo.getParentFilePath());
                                    FileUtils.deleteDirectory(new File(fileInfo.getParentFilePath()));
                                }
                                fileInfoIterator.remove();
                            }
                        }
                    }
                    if (fileList.size() <= 0) {
                        entryIterator.remove();
                    }
                }
            }
        } catch (Exception e) {
            logger.error("删除文件发生异常，异常信息：{}", e);
        }
    }


    public static void saveFileToMap(File file) {
        //添加过期文件到时间轮中
        Long fileCreateTime = Long.valueOf(file.getName().replace(ConstantFilePath.FILE_FORMAT_JSON, ""));
        ExpireFileKey expireFileKey = new ExpireFileKey(fileCreateTime);
        if (expireFileMap.containsKey(expireFileKey)) {
            FileInfo fileInfo = new FileInfo(file);
            expireFileMap.get(expireFileKey).add(fileInfo);
        } else {
            List<FileInfo> fileList = new ArrayList<>();
            FileInfo fileInfo = new FileInfo(file);
            fileList.add(fileInfo);
            expireFileMap.put(expireFileKey, fileList);
        }

    }

    /**
     * 获取该时间戳的key
     *
     * @param timestamp
     * @return
     */
    public static String getExpireFileKey(Long timestamp) {
        DateTime dateTime = DateUtil.date(timestamp);
        String dateStr = DateUtil.formatDate(dateTime);
        int startHour = DateUtil.hour(dateTime, true);
        int endHour = startHour + 1;
        return dateStr + "-" + startHour + "-" + endHour;
    }


}

class ExpireFileKey {
    private String key;

    private Date startDate;

    private Date endDate;


    public ExpireFileKey(Long timestamp) {
        this.key = getExpireFileKey(timestamp);
        this.startDate = getStartHour(timestamp);
        this.endDate = getEndHour(timestamp);

    }


    public String getKey() {
        return key;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public static String getExpireFileKey(Long timestamp) {
        DateTime dateTime = DateUtil.date(timestamp);
        String dateStr = DateUtil.formatDate(dateTime);
        int startHour = DateUtil.hour(dateTime, true);
        int endHour = startHour + 1;
        return dateStr + "-" + startHour + "-" + endHour;
    }

    public Date getStartHour(Long timestamp) {
        DateTime dateTime = DateUtil.date(timestamp);
        dateTime.setField(DateField.MINUTE, 0);
        dateTime.setField(DateField.SECOND, 0);
        dateTime.setField(DateField.MILLISECOND, 0);
        return dateTime;
    }

    public Date getEndHour(Long timestamp) {
        DateTime dateTime = DateUtil.date(timestamp);
        DateTime time = dateTime.offset(DateField.HOUR, 1);
        time.setField(DateField.MINUTE, 0);
        time.setField(DateField.SECOND, 0);
        time.setField(DateField.MILLISECOND, 0);
        return time;
    }

    public boolean compare(ExpireFileKey key) {
        int compare = DateUtil.compare(this.endDate, key.getStartDate());
        if (compare <= 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExpireFileKey that = (ExpireFileKey) o;
        if (!key.equals(that.key)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    public static void main(String[] args) {
//        ExpireFileKey expireFileKey = new ExpireFileKey(1678933819340L);
//        ExpireFileKey expireFileKey2 = new ExpireFileKey(1678933819340L);
//        if (expireFileKey2.equals(expireFileKey)) {
//            System.out.println("哈哈");
//        }
//        if (expireFileKey.hashCode() == expireFileKey2.hashCode()) {
//            System.out.println("哇哈哈");
//        }
//        if (expireFileKey.compare(expireFileKey2)) {
//            System.out.println("即哈哈");
//        }
        DateTime dateEnd = DateUtil.offset(DateUtil.date(), DateField.HOUR, -0);
        System.out.println(dateEnd);
    }
}

class FileInfo {
    /**
     * 文件路径
     */
    private String filePath;
    /**
     * 文件上层目录
     */
    private String parentFilePath;
    /**
     * 文件名 : 文件创建时间戳
     */
    private Long fileName;

    public FileInfo(File file) {
        this.filePath = file.getAbsolutePath();
        this.parentFilePath = file.getParentFile().getAbsolutePath();
        this.fileName = Long.valueOf(file.getName().replace(ConstantFilePath.FILE_FORMAT_JSON, ""));
    }

    public String getFilePath() {
        return filePath;
    }

    public String getParentFilePath() {
        return parentFilePath;
    }

    public Long getFileName() {
        return fileName;
    }
}


