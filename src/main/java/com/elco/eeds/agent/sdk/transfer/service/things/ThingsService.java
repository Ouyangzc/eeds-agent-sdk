package com.elco.eeds.agent.sdk.transfer.service.things;

import com.elco.eeds.agent.sdk.transfer.beans.things.EedsProperties;
import com.elco.eeds.agent.sdk.transfer.beans.things.EedsThings;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName ThingsService
 * @Description 数据源接口
 * @Author OUYANG
 * @Date 2022/12/19 8:42
 */
public interface ThingsService {
    /**
     * 加载数据源
     * @return
     * @throws IOException
     */
    String getThingsFile() throws IOException;

    /**
     * 存储数据源
     * @param data
     * @throws IOException
     */
    void saveThingsFile(String data) throws IOException;

    /**
     * 添加数据源
     *
     * @param addThings
     */
    void addThings(EedsThings addThings);

    /**
     * 删除数据源
     *
     * @param delThings
     */
    void delThings(EedsThings delThings);

    /**
     * 编辑数据源
     *
     * @param editThings
     */
    void editThings(EedsThings editThings);

    /**
     * 添加变量
     *
     * @param thingsId
     * @param addProperties
     */
    void addProperties(String thingsId, EedsProperties addProperties, List<EedsThings> syncThingsList);

    /**
     * 删除变量
     *
     * @param thingsId
     * @param delProperties
     */
    void delProperties(String thingsId, EedsProperties delProperties);


    /**
     * 编辑变量
     *
     * @param things
     * @param editProperties
     */
    void editProperties(EedsThings things);
}
