package com.wurainren.football_invest.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public class OKHttp3Utils {
    public static int DEFAULT_TIME_OUT = 10;

    /**
     * 全局实例可以保持http1.1 连接复用，线程池复用， 减少tcp的网络连接，关闭，
     * 如果每次一个请求，在高并发下，thread增多到1W，close_wait持续增加到6k。
     */
    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
            .connectionPool(new ConnectionPool(50, 5, TimeUnit.MINUTES))
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build();

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final MediaType FORM_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8");


    /**
     * 不同timeout的连接池
     */
    public static ConcurrentHashMap<Integer, OkHttpClient> cacheClients = new ConcurrentHashMap();


    public static OkHttpClient getHttpClient(int timeout) {

        if (timeout == 0 || DEFAULT_TIME_OUT == timeout) {
            return OK_HTTP_CLIENT;
        } else {
            OkHttpClient okHttpClient = cacheClients.get(timeout);
            if (okHttpClient == null) {
                return syncCreateClient(timeout);
            }
            return okHttpClient;
        }
    }

    private static synchronized OkHttpClient syncCreateClient(int timeout) {
        OkHttpClient okHttpClient;

        okHttpClient = cacheClients.get(timeout);
        if (okHttpClient != null) {
            return okHttpClient;
        }

        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .build();
        cacheClients.put(timeout, okHttpClient);
        return okHttpClient;

    }

    public static String get(String url, int timeout, Map<String,String> haderParams, Map<String, String> bodyParams) throws Exception {
        String urlNew = url;
        // 设置HTTP请求参数
        urlNew += setParams(bodyParams);
        Headers setHeaders = setHeaders(haderParams);
        Request request = new Request
                .Builder()
                .url(urlNew)
                .get()
                .headers(setHeaders)
                .build();
        return getHttpClient(timeout).newCall(request).execute().body().string();

    }

    /**
     * 设置请求头
     * @param url
     * @param timeout
     * @param haderMap
     * @return
     * @throws Exception
     */
    public static String get(String url, int timeout, Map<String,String> haderMap) throws Exception {
        Headers setHeaders = setHeaders(haderMap);
        Request request = new Request
                .Builder()
                .url(url)
                .get()
                .headers(setHeaders)
                .build();
        return getHttpClient(timeout).newCall(request).execute().body().string();

    }

    public static Headers setHeaders(Map<String, String> headersParams) {
        Headers headers = null;
        okhttp3.Headers.Builder headersbuilder = new okhttp3.Headers.Builder();
        if (Objects.nonNull(headersParams)) {
            Iterator<String> iterator = headersParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                headersbuilder.add(key, headersParams.get(key));
            }
        }
        headers = headersbuilder.build();
        return headers;
    }

    /**
     * 添加参数
     *
     * @param params
     * @return
     */
    public static String setParams(Map<String, String> params) {
        //1.添加请求参数
        //遍历map中所有参数到builder
        if (params != null && params.size() > 0) {
            StringBuffer stringBuffer = new StringBuffer("?");
            for (String key : params.keySet()) {
                if (!ObjectUtils.isEmpty(key) && !ObjectUtils.isEmpty(params.get(key))) {
                    //如果参数不是null并且不是""，就拼接起来
                    stringBuffer.append("&");
                    stringBuffer.append(key);
                    stringBuffer.append("=");
                    stringBuffer.append(params.get(key));
                }
            }

            return stringBuffer.toString();
        } else {
            return "";
        }
    }

    /**
     * GET请求
     *
     * @param url
     * @return Optional<String>
     */
    public static String get(String url, int timeout) throws Exception {
        Request request = new Request
                .Builder()
                .url(url)
                .build();
        return getHttpClient(timeout).newCall(request).execute().body().string();

    }

    public static String get(String url) throws Exception {
        return get(url, 0);
    }

    /**
     * POST请求，参数为json格式。
     *
     * @param url
     * @param json
     * @return Optional<String>
     */
    public static String post(String url, String json, int timeout) throws Exception {
        long start = System.currentTimeMillis();
        try {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder().url(url).post(body).build();
            return getHttpClient(timeout).newCall(request).execute().body().string();
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("request url {} ,total time {} ms", url, (System.currentTimeMillis() - start));

        }


    }


    public static String post(String url, String json) throws Exception {
        return post(url, json, 0);
    }


    public static String postByFormType(String url, String form) throws Exception {
        long start = System.currentTimeMillis();
        try {
            RequestBody body = RequestBody.create(FORM_TYPE, form);
            Request request = new Request.Builder().url(url).post(body).build();
            return getHttpClient(0).newCall(request).execute().body().string();

        } catch (Exception e) {
            throw e;
        } finally {
            log.info("request url {} ,total time {} ms", url, (System.currentTimeMillis() - start));

        }
    }


    /**
     * 根据不同的类型和requestbody类型来接续参数
     *
     * @param url
     * @param mediaType
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static String post(String url, MediaType mediaType, InputStream inputStream) throws Exception {
        RequestBody body = createRequestBody(mediaType, inputStream);
        Request request = new Request.Builder().url(url).post(body).build();
        return OK_HTTP_CLIENT.newCall(request).execute().body().string();
    }

    private static RequestBody createRequestBody(final MediaType mediaType, final InputStream inputStream) {
        return new RequestBody() {
            // @Nullable
            @Override
            public MediaType contentType() {
                return mediaType;
            }

            @Override
            public long contentLength() throws IOException {
                try {
                    return inputStream.available();
                } catch (IOException e) {
                    return 0;
                }
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source = null;
                try {
                    source = Okio.source(inputStream);
                    sink.writeAll(source);
                } finally {
                    Util.closeQuietly(source);
                }
            }
        };
    }
}
