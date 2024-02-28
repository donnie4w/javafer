/*
 * Copyright 2023 tldb Author. All Rights Reserved.
 * email: donnie4w@gmail.com
 * https://githuc.com/donnie4w/javafer
 */
package io.github.donnie4w.javafer.http;

import io.github.donnie4w.javafer.ssl.SSLUtil;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.logging.Logger;

public class HttpClient {
    public static Logger logger = Logger.getLogger("HttpClient");
    public static int connectionRequestTimeout = 5000;
    private final InputStream trustStoreStream;
    private final String keyStorePassword;

    public HttpClient(InputStream trustStoreStream, String keyStorePassword) {
        this.trustStoreStream = trustStoreStream;
        this.keyStorePassword = keyStorePassword;
    }

    public byte[] post(String url, byte[] bs, Map<String, String> headerMap) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new ByteArrayEntity(bs));
        if (headerMap != null) {
            headerMap.forEach((key, value) -> httpPost.setHeader(key, value));
        }
        try (CloseableHttpResponse response = getHttpClient(url,this.trustStoreStream,this.keyStorePassword).execute(httpPost)) {
            return EntityUtils.toByteArray(response.getEntity());
        }
    }




    public static CloseableHttpClient createHttpClient(InputStream trustStoreStream, String keyStorePassword) throws CertificateException, KeyStoreException, NoSuchAlgorithmException, IOException, KeyManagementException {
        RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout).build();
        return HttpClients.custom().setSSLSocketFactory(SSLUtil.newSSLConnectionSocketFactory(trustStoreStream,keyStorePassword)).setDefaultRequestConfig(config).build();
    }

    public static CloseableHttpClient createHttpClientForHttp() {
        RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout).build();
        return HttpClients.custom().setDefaultRequestConfig(config).build();
    }


    /**
     * defaultPost：Bypassing client certificate validation
     */
    public static byte[] defaultPost(String url, byte[] bs, Map<String, String> headerMap) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new ByteArrayEntity(bs));
        if (headerMap != null) {
            headerMap.forEach((key, value) -> httpPost.setHeader(key, value));
        }
        try (CloseableHttpResponse response = getHttpClient(url,null,null).execute(httpPost)) {
            return EntityUtils.toByteArray(response.getEntity());
        }
    }

    public static byte[] get(String url, Map<String, String> headerMap) throws Exception {
        HttpGet httpGet = new HttpGet(url);

        if (headerMap != null) {
            headerMap.forEach((key, value) -> httpGet.setHeader(key, value));
        }

        try (CloseableHttpResponse response = getHttpClient(url,null,null).execute(httpGet)) {
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() >= 200 && statusLine.getStatusCode() < 300) {
                return EntityUtils.toByteArray(response.getEntity());
            } else {
                throw new Exception("HTTP error code : " + statusLine.getStatusCode());
            }
        }
    }


    private static CloseableHttpClient getHttpClient(String urlStr,InputStream trustStoreStream, String keyStorePassword) throws CertificateException, KeyStoreException, NoSuchAlgorithmException, IOException, KeyManagementException, URISyntaxException {
        URI uri = new URI(urlStr);
        boolean isHttps = uri.getScheme().equals("https");
        if (isHttps) {
            return createHttpClient(trustStoreStream,keyStorePassword);
        } else {
            return createHttpClientForHttp();
        }
    }
}