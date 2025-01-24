package com.test.springboot;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class CasRestClient {

    private static final String CAS_BASE_URL = "https://localhost:8443/cas";
    private static final String SERVICE_URL = "https://www.apereo.org";

    public static void main(String[] args) throws IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        // 1. 获取 TGT
        String tgt = getTgt("test", "123456");
        System.out.println("TGT: " + tgt);

        // 2. 获取 ST
        String st = getSt(tgt, SERVICE_URL);
        System.out.println("ST: " + st);

        // 3. 验证 ST
        String userAttributes = validateSt(st, SERVICE_URL);
        System.out.println("User Attributes: " + userAttributes);

        // 4. 注销 ST
        logoutSt(st);
        System.out.println("ST logged out");

        // 5. 注销 TGT
        logoutTgt(tgt);
        System.out.println("TGT logged out");
    }

    private static String getTgt(String username, String password) throws IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        String url = CAS_BASE_URL + "/v1/tickets";
        HttpPost request = new HttpPost(url);
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        request.setEntity(new StringEntity("username=" + username + "&password=" + password));

        try (CloseableHttpClient client = createInsecureHttpClient()) {
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 201) {
                return response.getFirstHeader("Location").getValue();
            } else {
                throw new RuntimeException("Failed to get TGT: " + response.getStatusLine());
            }
        }
    }

    private static String getSt(String tgt, String serviceUrl) throws IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        String url = tgt;
        HttpPost request = new HttpPost(url);
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        request.setEntity(new StringEntity("service=" + serviceUrl));

        try (CloseableHttpClient client = createInsecureHttpClient()) {
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity());
            } else {
                throw new RuntimeException("Failed to get ST: " + response.getStatusLine());
            }
        }
    }

    private static String validateSt(String st, String serviceUrl) throws IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        String url = CAS_BASE_URL + "/p3/serviceValidate?ticket=" + st + "&service=" + serviceUrl;
        HttpGet request = new HttpGet(url);

        try (CloseableHttpClient client = createInsecureHttpClient()) {
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity());
            } else {
                throw new RuntimeException("Failed to validate ST: " + response.getStatusLine());
            }
        }
    }

    private static void logoutTgt(String tgt) throws IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        String url = tgt;
        HttpDelete request = new HttpDelete(url);

        try (CloseableHttpClient client = createInsecureHttpClient()) {
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed to logout TGT: " + response.getStatusLine());
            }
        }
    }

    private static void logoutSt(String st) throws IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        String url = CAS_BASE_URL + "/v1/tickets/" + st;
        HttpDelete request = new HttpDelete(url);

        try (CloseableHttpClient client = createInsecureHttpClient()) {
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed to logout ST: " + response.getStatusLine());
            }
        }
    }

    private static CloseableHttpClient createInsecureHttpClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException, KeyManagementException {

//        return HttpClients.createDefault();
        SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(null, (certificate, authType) -> true)
                .build();

        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        return HttpClients.custom()
                .setSSLSocketFactory(socketFactory)
                .build();
    }
}