# eeds-sdk说明
## 项目层级目录说明
```java
├──java
  ├──com
    ├──elco
      ├──eeds
        ├──agent
          ├──sdk
            ├──core sdk核心包
              ├──bean 实体类
              ├──com.elco.eeds.agent.sdk.common 公共类
                └──EedsErrorEnum.java eeds错误码
              ├──exception 异常
                └──EedsSdkException.java
              ├──service 业务逻辑处理
              ├──util 工具包
            ├──transfer  与server端的交互
            └──Main.java  主启动类
├──resources
  └──logback.xml
```
