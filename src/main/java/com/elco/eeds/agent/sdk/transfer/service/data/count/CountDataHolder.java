package com.elco.eeds.agent.sdk.transfer.service.data.count;

import com.alibaba.fastjson.JSON;
import com.elco.eeds.agent.sdk.core.util.CountFileUtils;
import com.elco.eeds.agent.sdk.transfer.beans.data.count.PostDataCount;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CountDataHolder
 * @Description 统计记录缓存持有者
 * @Author OUYANG
 * @Date 2022/12/9 10:22
 */
public class CountDataHolder {
    /**
     * 待发送队列
     */
    private List<PostDataCount> waitingCountDatas;
    /**
     * 要发送数据
     */
    private PostDataCount doPostData;

    public CountDataHolder() {
        this.waitingCountDatas = new ArrayList<>();
    }

    /**
     * 设置发送数据
     *
     * @param doPostData
     */
    public void setDoPostData(PostDataCount doPostData) {
        this.doPostData = doPostData;
    }

    /**
     * 获取发送统计记录
     *
     * @return
     */
    public PostDataCount getDoPostData() {
        return this.doPostData;
    }

    /**
     * 从当前排队数据中获取第一条
     *
     * @return
     */
    public PostDataCount getLastPostData() {
        if (waitingCountDatas.isEmpty()) {
            int size = this.waitingCountDatas.size() - 1;
            PostDataCount postDataCount = this.waitingCountDatas.get(size);
            this.waitingCountDatas.remove(size);
            return postDataCount;
        }
        return null;
    }

    /**
     * 设置最后一条为要发送数据
     *
     * @return
     */
    public void setLastDataTODoPostData() {
        //没有要发送的数据
        PostDataCount lastPostData = getLastPostData();
        if (null != lastPostData) {
            setDoPostData(lastPostData);
        }
    }

    /**
     * 将统计数据加入到等待队列中
     *
     * @param postDataCount
     */
    public void addPostCountDataToWait(PostDataCount postDataCount) {
        this.waitingCountDatas.add(postDataCount);
    }

    public void setWaitingCountDatas(List<PostDataCount> waitingCountDatas) {
        this.waitingCountDatas = waitingCountDatas;
    }

    /**
     * 获取统计数据
     *
     * @return
     * @throws IOException
     */
    public List<PostDataCount> getCountDataFormFile() throws IOException {
        return CountFileUtils.getFileData();
    }

    /**
     * 移动统计到完成文件中
     *
     * @param doneData
     */
    public void moveCountDataToDoneFile(PostDataCount doneData) {
        CountFileUtils.delDoneDataFromFile(doneData);
    }

    /**
     * 状态变更 覆盖写
     *
     * @param dataCount
     * @throws IOException
     */
    public void overrideCountDataToFile(PostDataCount dataCount) throws IOException {
        CountFileUtils.writeAppendForOverride(dataCount);
    }

    /**
     * 未发送统计记录加入到文件中
     *
     * @param dataCount
     * @throws IOException
     */
    public void countDataAppendToFile(PostDataCount dataCount) throws IOException {
        String data = JSON.toJSONString(dataCount);
        CountFileUtils.writeAppend(data);
    }
}
