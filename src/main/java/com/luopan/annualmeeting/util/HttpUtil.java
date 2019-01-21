package com.luopan.annualmeeting.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;

@Slf4j
public final class HttpUtil {

  private HttpUtil() {

  }

  // 连接超时
  private static final Long CONNECT_TIMEOUT = 10L;
  // 读取超时
  private static final Long READ_TIMEOUT = 20L;
  // 写入超时
  private static final Long WRITE_TIMEOUT = 20L;

  private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");


  private static final OkHttpClient client = new OkHttpClient.Builder()
      .retryOnConnectionFailure(Boolean.TRUE)
      .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
      .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
      .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
      .build();

  /**
   * 向指定URL发送GET方法的请求
   *
   * @param url 指定url
   */
  public static String get(String url) {
    Request request = new Builder().url(url).build();
    try (Response response = client.newCall(request).execute()) {
      return response.body().string();
    } catch (IOException e) {
      log.error("HttpUtil发送get请求异常", e);
    }
    return null;
  }

  /**
   * 向指定 URL 发送POST方法的请求
   *
   * @param url 指定url
   * @param params 参数键值对
   */
  public static String postJson(String url, Map<String, Object> params) {
    RequestBody requestBody = RequestBody.create(JSON, JsonUtil.obj2String(params));
    Request request = new Builder().url(url).post(requestBody).build();
    try (Response response = client.newCall(request).execute()) {
      return response.body().string();
    } catch (IOException e) {
      log.error("HttpUtil发送post/json请求异常", e);
    }
    return null;
  }

  /**
   * 向指定 URL 发送POST方法的请求
   *
   * @param url 指定url
   * @param params 参数键值对
   */
  public static String postForm(String url, Map<String, String> params) {
    Charset charset = Charset.forName("utf-8");
    FormBody.Builder builder = new FormBody.Builder(charset);
    if (params != null) {
      params.forEach(builder::add);
    }
    FormBody formBody = builder.build();
    Request request = new Builder().url(url).post(formBody).build();
    try (Response response = client.newCall(request).execute()) {
      return response.body().string();
    } catch (IOException e) {
      log.error("HttpUtil发送post/form请求异常", e);
    }
    return null;
  }

}
