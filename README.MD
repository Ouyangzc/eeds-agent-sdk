# eeds-sdk说明
## 项目层级目录说明
```java
├──java
  ├──com
    ├──elco
      ├──eeds
        ├──agent
          ├──sdk
            ├──common  Server端结构返回体
              ├──entity
                └──ResponseResult.java Server端结构返回体
              ├──enums
                └──SysCodeEnum.java Server端返回编码
            ├──core
              ├──bean  实体类
                ├──agent 客户端模块实体类
                  └──Agent.java 
                  └──AgentBaseInfo.java 
                  └──AgentMqAuthInfo.java 
                  └──AgentMqInfo.java 
                  └──AgentMqSecurityInfo.java 
                  └──BaseConfigEntity.java  
                  └──ConfigBase.java  
                  └──ConfigGlobal.java 
                  └──ConfigPrivate.java 
                ├──properties 变量模块实体类
                  └──PropertiesContext.java
                  └──PropertiesValue.java
              ├──common
                ├──constant 常量类
                  ├──client 客户端常量
                    └──ConstantClientType.java
                  └──ConstantCommon.java  基础常量
                  └──ConstantCount.java   统计常量
                  └──ConstantFilePath.java  文件路径常量
                  └──ConstantThings.java  数据源常量
                  ├──http HTTP路径常量
                    └──ConstantHttpApiPath.java
                  ├──message 报文常量
                    └──ConstantMethod.java
                    └──ConstantOperation.java
                    └──ConstantTopic.java
                ├──enums 枚举类
                  └──ErrorEnum.java 错误枚举值
              ├──connect  数据源连接类
                ├──init
                  └──InitConnectFactory.java
                ├──manager
                  └──ConnectManager.java
                ├──proxy
                  └──ConnectProxy.java
                ├──status
                  └──ConnectionStatus.java
                └──ThingsConnection.java
                └──ThingsConnectionHandler.java
              ├──exception
                └──SdkException.java
              ├──parsing
                └──DataParsing.java
              ├──service
              ├──start  启动类包
                └──AgentRegisterService.java
                └──AgentStarter.java
                └──AgentStartProperties.java
                └──IAgentRegisterService.java
                └──Logo.java
              ├──util 工具类包
                └──AgentFileExtendUtils.java
                └──AgentFileUtils.java
                └──CountFileUtils.java
                └──CreatorUtils.java
                └──DateUtils.java
                └──FileUtil.java
                ├──http
                  └──HttpClientUtil.java
                  └──HttpsClientFactory.java
                  └──IpUtil.java
                └──IpAddressPortUtil.java
                └──JsonUtil.java
                └──MapUtils.java
                ├──read
                  ├──parameterfile
                    └──AgentConfigYamlReader.java
                    └──Resource.java
                    └──ResourceLoader.java
                    └──UrlResource.java
                └──RealTimeDataMessageFileUtils.java
                └──ReflectUtils.java
                └──ReplaceTopicAgentId.java
                └──ThingsFileUtils.java
            └──Main.java
            ├──transfer 与server端交互包
              ├──beans 实体类
                ├──agent 客户端模块
                  └──AgentRegisterRequest.java
                  └──AgentTokenRequest.java
                ├──data 数据模块
                  ├──count  统计
                    └──PostDataCount.java
                    └──ThingsDataCount.java
                  └──OriginalPropertiesValueMessage.java
                  ├──sync 同步
                    └──DataSyncFinishResult.java
                    └──DataSyncServerRequest.java
                ├──message 报文包
                  ├──agent 客户端报文
                    ├──command
                      └──AgentCommandMessage.java
                      └──SubAgentCommandContent.java
                      └──SubAgentCommandMessage.java
                      └──SubAgentCommandRspMessage.java
                      └──SubAgentCommandThings.java
                    ├──linktest
                      └──AgentLinkTestMessage.java
                      └──ExtraMap.java
                      └──SubAgentLinkTestData.java
                      └──SubAgentLinkTestMessage.java
                      └──SubAgentLinkTestStatus.java
                      └──SubAgentLinkTestThingsData.java
                  └──BaseMessage.java
                  ├──config 客户端配置
                    └──AgentConfigMessage.java
                    └──SubAgentConfigMessage.java
                  ├──connect 客户端连接
                    └──SubThingsConnectStatusMessage.java
                    └──ThingsStatusMessage.java
                  ├──data 数据
                    ├──count
                      ├──confirm
                        └──DataCountConfirmMessage.java
                        └──SubDataCountConfirmMessage.java
                      ├──post
                        └──DataCountMessage.java
                        └──SubDataCountMessage.java
                    ├──realTime
                      └──DataRealTimePropertiesMessage.java
                      └──SubDataRealTimePropertiesMessage.java
                    ├──sync
                      ├──cancel
                        └──DataSyncCancelMessage.java
                        └──SubDataSyncCancelMessage.java
                      ├──confirm
                        └──DataSyncConfirmMessage.java
                        └──SubDataSyncConfirmMessage.java
                      ├──data
                        └──DataSyncPropertiesValueMessage.java
                        └──SubDataSyncPropertiesValueMessage.java
                      ├──finish
                        └──DataSyncFinishMessage.java
                        └──SubDataSyncFinishMessage.java
                      ├──request
                        └──DataSyncRequestMessage.java
                        └──SubDataSyncRequestMessage.java
                  ├──heart
                    └──AgentHeartMessage.java
                    └──SubAgentHeartMessage.java
                  ├──things  数据源报文
                    └──SubThingsConnectStatusMessage.java
                    └──SubThingsHeartBeatMessage.java
                    └──SubThingsSyncIncrMessage.java
                    └──ThingsConnectStatusMessage.java
                    └──ThingsHeartBeatMessage.java
                    └──ThingsSyncIncrMessage.java
                  ├──token
                    └──AgentTokenMessage.java
                    └──AgentTokenRspMessage.java
                    └──SubAgentTokenMessage.java
                    └──SubAgentTokenRspMessage.java
                ├──things 数据源实体类
                  └──BaseProperties.java
                  └──BaseThings.java
                  └──EedsProperties.java
                  └──EedsThings.java
                  └──ThingsDriverContext.java
                  └──ThingsSyncRequest.java
              ├──config
                └──TransferApi.java
                └──TransferApiProperties.java
              ├──handler  报文处理包
                ├──agent
                  └──AgentCommandDistributeMessageHandler.java
                  └──AgentConfigGlobalMessageHandler.java
                  └──AgentConfigLocalMessageHandler.java
                  └──AgentHeartMessageHandler.java
                  └──AgentLinkTestMessageHandler.java
                  └──AgentTokenMessageHandler.java
                ├──data
                  ├──count
                    └──DataCountConfirmMessageHandler.java
                  ├──sync
                    └──DataSyncCancelMessageHandler.java
                    └──DataSyncRequestMessageHandler.java
                └──IReceiverMessageHandler.java
                ├──things
                  └──ThingsSyncIncrMessageHandler.java
              ├──quartz 定时任务包
                └──CountJob.java
                └──CountScheduler.java
              ├──service 报文处理逻辑包
                ├──agent
                  └──AgentRequestHttpService.java
                ├──data
                  ├──count
                    └──CountDataHolder.java
                    └──DataCountService.java
                    └──DataCountServiceImpl.java
                  └──RealTimePropertiesValueService.java
                  ├──sync
                    └──DataSyncService.java
                ├──things
                  └──ThingsConnectStatusMqService.java
                  └──ThingsHeartBeatMqService.java
                  └──ThingsRequestHttpService.java
                  └──ThingsService.java
                  └──ThingsServiceImpl.java
                  └──ThingsSyncService.java
                  └──ThingsSyncServiceImpl.java
├──resources
  └──client.jks
  └──logback.xml
```
# dev-1.1.0 feature</br>
feature1 : 变量变动通知  
说明：变量点位得增删,通知到客户端，适用于对变量得单独订阅  

# dev-1.1.0.1 feature</br>
feature1 : 数据源状态全部由SDK进行管理,CONNECTED,DISCONNECT
feature2 : 客户端新增状态，INIT,READY,LOAD,RUNNING