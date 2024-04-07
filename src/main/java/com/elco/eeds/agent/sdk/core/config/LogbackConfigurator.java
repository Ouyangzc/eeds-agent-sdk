package com.elco.eeds.agent.sdk.core.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.util.FileSize;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.elco.eeds.agent.sdk.core.bean.agent.AgentLoggingFileProperties;
import com.elco.eeds.agent.sdk.core.util.AgentResourceUtils;
import org.slf4j.LoggerFactory;

/**
 * @ClassName LogbackConfigurator
 * @Description LogbackConfigurator
 * @Author OuYang
 * @Date 2024/3/13 16:15
 * @Version 1.0
 */
public class LogbackConfigurator extends ContextAwareBase implements LoggerContextListener,
    LifeCycle {

  private org.slf4j.Logger logger = LoggerFactory.getLogger(LogbackConfigurator.class);
  /**
   * 默认保留天数: 7天
   */
  public static final Integer DEFAULT_MAX_HISTORY = 7;
  /**
   * 默认文件大小：250MB
   */
  public static final String DEFAULT_FILE_SIZE = "250MB";
  /**
   * 默认总文件大小：2GB
   */
  public static final String DEFAULT_TOTAL_SIZE_CAP = "2GB";

  public static final String LOGGER_ROOT_NAME = "root";

  public static final String LOGGER_INFO_APPENDER = "FILEInfoLog";
  public static final String LOGGER_ERROR_APPENDER = "FILEErrorLog";
  public static final String LOGGER_DEBUG_APPENDER = "FILEDebugLog";
  private boolean isStarted = false;

  @Override
  public void start() {
    if (isStarted) {
      return;
    }
    try {
      AgentLoggingFileProperties properties = AgentResourceUtils.getLoggingFileSize();
      if (null == properties) {
        return;
      }
      if (StrUtil.isEmpty(properties.getMaxFileSize()) && ObjectUtil.isEmpty(
          properties.getMaxHistory())) {
        //走默认配置
        return;
      }
      Integer maxHistory =
          properties.getMaxHistory() == null ? DEFAULT_MAX_HISTORY : properties.getMaxHistory();
      String fileSize =
          properties.getMaxFileSize() == null ? DEFAULT_FILE_SIZE : properties.getMaxFileSize();
      String totalSizeCap = properties.getTotalSizeCap() == null ? DEFAULT_TOTAL_SIZE_CAP
          : properties.getTotalSizeCap();
      LoggerContext context = (LoggerContext) getContext();
      Logger rootLogger = context.getLogger(LOGGER_ROOT_NAME);

      reBuildAppenderRollingPolicy(rootLogger, LOGGER_INFO_APPENDER, FileSize.valueOf(fileSize),
          maxHistory, FileSize.valueOf(totalSizeCap));
      reBuildAppenderRollingPolicy(rootLogger, LOGGER_ERROR_APPENDER, FileSize.valueOf(fileSize),
          maxHistory, FileSize.valueOf(totalSizeCap));
      reBuildAppenderRollingPolicy(rootLogger, LOGGER_DEBUG_APPENDER, FileSize.valueOf(fileSize),
          maxHistory, FileSize.valueOf(totalSizeCap));
    } catch (Exception e) {
      logger.error("自定义配置日志文件失败,异常:", e);
    }

    isStarted = true;
  }

  /**
   * 重新构建滚动策略
   *
   * @param rootLogger
   * @param appenderName
   * @param fileSize
   * @param maxHistory
   */
  private void reBuildAppenderRollingPolicy(Logger rootLogger, String appenderName,
      FileSize fileSize, Integer maxHistory, FileSize totalSizeCap) {
    FileAppender appender = (FileAppender) rootLogger.getAppender(appenderName);
    if (appender instanceof RollingFileAppender) {
      RollingFileAppender rollingFileAppender = (RollingFileAppender) appender;
      SizeAndTimeBasedRollingPolicy rollingPolicy = (SizeAndTimeBasedRollingPolicy) rollingFileAppender.getRollingPolicy();
      rollingPolicy.stop();
      rollingPolicy.setMaxFileSize(fileSize);
      rollingPolicy.setMaxHistory(maxHistory);
      rollingPolicy.setTotalSizeCap(totalSizeCap);
      rollingPolicy.start();
    }
  }


  @Override
  public void stop() {

  }

  @Override
  public boolean isStarted() {
    return isStarted;
  }

  @Override
  public boolean isResetResistant() {
    return true;
  }

  @Override
  public void onStart(LoggerContext loggerContext) {

  }

  @Override
  public void onReset(LoggerContext loggerContext) {

  }

  @Override
  public void onStop(LoggerContext loggerContext) {

  }

  @Override
  public void onLevelChange(Logger logger, Level level) {

  }
}
