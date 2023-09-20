package com.elco.eeds.agent.sdk.core.util;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.sdk.core.bean.agent.Agent;
import com.elco.eeds.agent.sdk.core.common.constant.ConstantFilePath;
import com.elco.eeds.agent.sdk.transfer.beans.data.count.PostDataCount;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CountFileUtils
 * @Description 数据统计文件存储实现类
 * @Author OUYANG
 * @Date 2022/12/9 10:36
 */
public class CountFileUtils {
	public static final Logger logger = LoggerFactory.getLogger(CountFileUtils.class);

	/**
	 * 获取当前统计文件中所有的统计数据
	 *
	 * @return
	 * @throws IOException
	 */
	public static List<PostDataCount> getFileData() throws IOException {
		List<PostDataCount> dataCounts = new ArrayList<>();
		String path = getDataCountFilePath();
		File file = FileUtils.getFile(path);
		if (!file.exists()) {
			return null;
		}
		List<String> readLines = FileUtils.readLines(file, StandardCharsets.UTF_8);
		for (String line : readLines) {
			PostDataCount dataCount = JSON.parseObject(line, PostDataCount.class);
			dataCounts.add(dataCount);
		}
		return dataCounts;
	}


	/**
	 * 删除已完成统计数据
	 *
	 * @param dataCount
	 * @throws IOException
	 */
	public static void delDoneDataFromFile(PostDataCount dataCount) {
		try {
			List<String> dataCounts = new ArrayList<>();
			String path = getDataCountFilePath();
			File file = FileUtils.getFile(path);
			LineIterator lineIterator = FileUtils.lineIterator(file);
			String countId = dataCount.getCountId();
			while (lineIterator.hasNext()) {
				String line = lineIterator.next();
				PostDataCount dataCountRecord = JSON.parseObject(line, PostDataCount.class);
				if (!dataCountRecord.getCountId().equals(countId)) {
					dataCounts.add(line);
				}
			}
			FileUtils.writeLines(file, dataCounts, false);
			//追加到完成文件
			writeAppendForDoneData(JSONUtil.toJsonStr(dataCount));
		} catch (Exception e) {
			logger.error("统计已完成记录，删除异常，异常信息:", e);
		}

	}

	/**
	 * 统计记录追加写
	 *
	 * @param data
	 * @throws IOException
	 */
	public static void writeAppend(String data) throws IOException {
		String filePath = getDataCountFilePath();
		File file = FileUtils.getFile(filePath);
		List<String> datas = new ArrayList<>();
		datas.add(data);
		FileUtils.writeLines(file, datas, true);
	}


	/**
	 * 统计完成记录追加写
	 *
	 * @param data
	 * @throws IOException
	 */
	public static void writeAppendForDoneData(String data) throws IOException {
		String filePath = getDataCountDoneFilePath();
		File file = FileUtils.getFile(filePath);
		List<String> datas = new ArrayList<>();
		datas.add(data);
		FileUtils.writeLines(file, datas, true);

	}

	/**
	 * 更新一条数据
	 *
	 * @param dataCount
	 * @throws IOException
	 */
	public static void writeAppendForOverride(PostDataCount dataCount) throws IOException {
		List<PostDataCount> dataCounts = new ArrayList<>();
		String path = getDataCountFilePath();
		File file = FileUtils.getFile(path);
		LineIterator lineIterator = FileUtils.lineIterator(file);
		String countId = dataCount.getCountId();
		while (lineIterator.hasNext()) {
			String line = lineIterator.next();
			logger.info("writeAppendForOverride,line data:{}", line);
			PostDataCount dataCountRecord = JSON.parseObject(line, PostDataCount.class);
			if (dataCountRecord.getCountId().equals(countId)) {
				//覆盖
				dataCounts.add(dataCount);
			} else {
				dataCounts.add(dataCountRecord);
			}
		}
		FileUtils.writeLines(file, dataCounts, false);
	}

	/**
	 * 获取统计文件
	 *
	 * @return
	 * @throws IOException
	 */
	public static String getDataCountFilePath() throws IOException {
		String baseFolder = Agent.getInstance().getAgentBaseInfo().getBaseFolder() + ConstantFilePath.DATA_COUNT_FOLDER;
		String filePath = AgentFileUtils.getFilePath(baseFolder);
		return filePath + ConstantFilePath.DATA_COUNT_UNDONE_PATH;
	}

	/**
	 * 获取统计完成文件
	 *
	 * @return
	 * @throws IOException
	 */
	public static String getDataCountDoneFilePath() throws IOException {
		String baseFolder = Agent.getInstance().getAgentBaseInfo().getBaseFolder() + ConstantFilePath.DATA_COUNT_FOLDER;
		String filePath = AgentFileUtils.getFilePath(baseFolder);
		return filePath + ConstantFilePath.DATA_COUNT_DONE_PATH;
	}
}
