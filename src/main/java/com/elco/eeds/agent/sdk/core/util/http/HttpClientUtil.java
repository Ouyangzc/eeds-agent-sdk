package com.elco.eeds.agent.sdk.core.util.http;

import com.elco.eeds.agent.sdk.core.common.enums.ErrorEnum;
import com.elco.eeds.agent.sdk.core.exception.SdkException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public static String sentHttpPostRequest(String url, String token, String content) throws Exception {
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

}
