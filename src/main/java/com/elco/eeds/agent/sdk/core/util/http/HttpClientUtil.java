package com.elco.eeds.agent.sdk.core.util.http;

import cn.hutool.core.io.resource.ClassPathResource;
import com.elco.eeds.agent.sdk.core.common.enums.ErrorEnum;
import com.elco.eeds.agent.sdk.core.exception.SdkException;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

/**
 * @title: HttpClient
 * @Author wl
 * @Date: 2022/12/6 14:40
 * @Version 1.0
 */
public class HttpClientUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    private static final String APPLICATION_JSON = "application/json";
    private static final String CHARSET_UTF_8 = "UTF-8";
    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String HTTPS_URL = "https";
    private static final String HTTP_URL = "http";
    public static String post(String url, String token, String content) throws Exception {
        URL requestUrl = new URL(url);
        URI uri = requestUrl.toURI();
        if(uri.getScheme().equals(HTTPS_URL)){
            return httpsPost(url,token,content);
        }else {
            return httpPost(url,token,content);
        }
    }


    public static String httpPost(String url, String token, String content) throws Exception {
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
            httpPost.addHeader(AUTHORIZATION, BEARER + token);

            // 绑定到请求 Entry
            StringEntity se = new StringEntity(content, CHARSET_UTF_8);
            se.setContentType(CONTENT_TYPE_TEXT_JSON);
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
            httpPost.setEntity(se);
            // 发送请求
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);
            // 得到应答的字符串，这也是一个 JSON 格式保存的数据
            String resp = EntityUtils.toString(httpResponse.getEntity(), CHARSET_UTF_8);
            logger.debug("response: {}", resp);
            return resp;
        } catch (Exception e) {
            logger.error("请求错误：{}", e);
            logger.error("url: {}, token: {}, content: {}", url, token, content);
            e.printStackTrace();
            throw new SdkException(ErrorEnum.HTTP_REQUEST_ERROR.code());
        }
    }


    public static String httpsPost(String url, String token, String content) throws Exception {
        try {
            //https配置
            //加载客户端证书，jar 包，或者使用 Dockerfile 打包到镜像的方式可能就会报错，获取不到，使用如下流的方式进行获取，就不会有问题
            ClassPathResource classPathResource = new ClassPathResource("client.jks");
            InputStream inputStream = classPathResource.getStream();
            File inuModel = new File("/opt/data/elco/config");
            FileUtils.copyToFile(inputStream, inuModel);
            SSLContext sslContext = SSLContextBuilder.create()
                    .loadTrustMaterial(inuModel, "123456".toCharArray())
                    .build();
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
            httpPost.addHeader(AUTHORIZATION, BEARER + token);

            // 绑定到请求 Entry
            StringEntity se = new StringEntity(content, CHARSET_UTF_8);
            se.setContentType(CONTENT_TYPE_TEXT_JSON);
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
            httpPost.setEntity(se);
            HttpResponse httpResponse=  HttpClients.custom().setSSLSocketFactory(socketFactory).build().execute(httpPost);
            // 得到应答的字符串，这也是一个 JSON 格式保存的数据
            String resp = EntityUtils.toString(httpResponse.getEntity(), CHARSET_UTF_8);
            logger.debug("response: {}", resp);
            return resp;
        } catch (Exception e) {
            logger.error("请求错误：{}", e);
            logger.error("url: {}, token: {}, content: {}", url, token, content);
            e.printStackTrace();
            throw new SdkException(ErrorEnum.HTTP_REQUEST_ERROR.code());
        }
    }

}
