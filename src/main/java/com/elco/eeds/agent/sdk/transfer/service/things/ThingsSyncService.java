package com.elco.eeds.agent.sdk.transfer.service.things;

import com.elco.eeds.agent.sdk.core.bean.properties.PropertiesContext;
import com.elco.eeds.agent.sdk.transfer.beans.things.EedsThings;
import com.elco.eeds.agent.sdk.transfer.beans.things.ThingsSyncRequest;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName ThingsSyncService
 * @Description 数据源同步接口
 * @Author OUYANG
 * @Date 2022/12/16 9:03
 */
public interface ThingsSyncService {

    /**
     * 程序启动数据源同步
     * @param request
     * @param token
     * @return
     */
    List<EedsThings> getSetupSyncData(ThingsSyncRequest request, String token);

    /**
     * 请求获取同步数据
     * @param request
     * @param token
     * @return
     */
    List<EedsThings> getSyncData(ThingsSyncRequest request, String token);

    /**
     * 数据整理
     *
     * @param convertData
     * @return
     */
    List<PropertiesContext> convertData(List<EedsThings> convertData);

    /**
     * 保存数据源信息到本地
     *
     * @throws Exception
     */
    boolean saveToLocalFile(String thingsData) throws Exception;


    /**
     * 获取本地数据源信息
     *
     * @return
     * @throws IOException
     */
    String getLocalThings() throws IOException;


    /**
     * 处理同步数据
     * @param propertiesContexts
     * @return
     */
    boolean handleSyncThingsData(List<PropertiesContext> propertiesContexts);
}
