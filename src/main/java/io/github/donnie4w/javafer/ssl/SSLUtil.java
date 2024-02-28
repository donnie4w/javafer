/*
 * Copyright (c) , donnie <donnie4w@gmail.com>
 * All rights reserved.
 * https://github.com/donnie4w/javafer
 */
package io.github.donnie4w.javafer.ssl;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class SSLUtil {

    protected static X509TrustManager newX509ExtendedTrustManager() {
        X509TrustManager tm = new X509ExtendedTrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }
            @Override

            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {

            }

            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {

            }
        };
        return tm;
    }


    protected static SSLContext newSSLContext(InputStream trustStoreStream, String keyStorePassword) throws CertificateException, KeyStoreException, NoSuchAlgorithmException, IOException, KeyManagementException {
        SSLContext ctx = SSLContext.getInstance("TLSv1.2");
        if (trustStoreStream==null||keyStorePassword==null){
            ctx.init(null, new TrustManager[]{newX509ExtendedTrustManager()}, new SecureRandom());
        }else{
            ctx.init(null, new TrustManager[]{newX509TrustManagerByStream(trustStoreStream,keyStorePassword)}, new SecureRandom());
        }
        return ctx;
    }

    protected static PoolingHttpClientConnectionManager newHttpClientConnectionManager(InputStream trustStoreStream, String keyStorePassword) throws Exception {
        SSLConnectionSocketFactory ssf = newSSLConnectionSocketFactory( trustStoreStream,  keyStorePassword);
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", ssf).build();
        PoolingHttpClientConnectionManager poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        poolConnManager.setMaxTotal(100);
        return poolConnManager;
    }


    public static SSLConnectionSocketFactory newSSLConnectionSocketFactory(InputStream trustStoreStream, String keyStorePassword) throws CertificateException, KeyStoreException, NoSuchAlgorithmException, IOException, KeyManagementException {
           return  new SSLConnectionSocketFactory(newSSLContext(trustStoreStream,keyStorePassword), NoopHostnameVerifier.INSTANCE);
    }


    public static X509TrustManager newX509TrustManagerByKerStore(String trustedkeystore, String keyStorePassword) throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException {
        return  newX509TrustManagerByStream(new FileInputStream(trustedkeystore),keyStorePassword);
    }

    public static X509TrustManager newX509TrustManagerByStream(InputStream trustStoreStream, String keyStorePassword) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        char[] trustStorePassword = keyStorePassword.toCharArray();
        trustStore.load(trustStoreStream, trustStorePassword);

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);

        X509TrustManager defaultTrustManager = null;
        for (TrustManager tm : trustManagerFactory.getTrustManagers()) {
            if (tm instanceof X509TrustManager) {
                defaultTrustManager = (X509TrustManager) tm;
                break;
            }
        }

        if (defaultTrustManager == null) {
            throw new IllegalStateException("No X509TrustManager found");
        }
        return  defaultTrustManager;
    }

}
