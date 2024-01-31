package com.elco.eeds.agent.sdk.core.util.http;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import javax.net.ssl.SSLException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.protocol.HttpContext;

/**
 * @ClassName HttpRetryHandler
 * @Description http重试策略
 * @Author OuYang
 * @Date 2024/1/26 11:43
 * @Version 1.0
 */
public class HttpRetryHandler implements HttpRequestRetryHandler {

  private static final int MaxRetryCount = 3;


  @Override
  public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
    if (executionCount > MaxRetryCount) {
      return false;
    }
    if (exception instanceof InterruptedIOException) {
      // Timeout
      return false;
    }
    if (exception instanceof UnknownHostException) {
      // Unknown host
      return false;
    }
    if (exception instanceof ConnectTimeoutException) {
      // Connection refused
      return false;
    }
    if (exception instanceof SSLException) {
      // SSL handshake exception
      return false;
    }
    // 根据服务器响应的状态码，决定是否继续重试
//    if (context.getAttribute("response") instanceof HttpResponse) {
//      HttpResponse response = (HttpResponse) context.getAttribute("response");
//      int statusCode = response.getStatusLine().getStatusCode();
//      if (statusCode == 503) {
//        return true; // 重试503服务不可用状态码
//      }
//    }
    return true;
  }
}
