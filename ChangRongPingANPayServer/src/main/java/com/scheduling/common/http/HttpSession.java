package com.scheduling.common.http;


import com.scheduling.common.http.exception.HttpSessionException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;


public class HttpSession {
    public static final Logger logger = Logger.getLogger(HttpSession.class);

    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String POST_CONTENT_TYPE = "text/plain";
    private HttpClient client = null;
    private URI uri = null;
    private Credentials credentials = null;

    public HttpSession(URI uri, Credentials credentials) {
        this.uri = uri;
        this.credentials = credentials;
    }

    public JSONObject sendAndReceive(JSONObject message) {
        logger.info("进入 sendAndReceive>>>>" + message);
        PostMethod method = new PostMethod(this.uri.toString());
        try {
            method.setRequestHeader("Content-Type", JSON_CONTENT_TYPE);

            RequestEntity requestEntity = new StringRequestEntity(message.toString(), JSON_CONTENT_TYPE, null);
            method.setRequestEntity(requestEntity);

            getHttpClient().executeMethod(method);
            int statusCode = method.getStatusCode();
            logger.info("访问状态>>>" + statusCode);
            if (statusCode != 200) {
                throw new HttpSessionException("HTTP Status - " + HttpStatus.getStatusText(statusCode) + " (" + statusCode + ")" + "-content:" + method.getResponseBodyAsString());
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), "UTF-8"));
            StringBuffer stringBuffer = new StringBuffer();
            String str0 = "";
            while ((str0 = reader.readLine()) != null) {
                stringBuffer.append(str0);
            }
            String str = stringBuffer.toString();

            logger.info("sendAndReceive>>>> response >>>>" + str);

            JSONTokener tokener = new JSONTokener(str);
            Object rawResponseMessage = tokener.nextValue();
            JSONObject response = (JSONObject) rawResponseMessage;

            if (response == null) {
                throw new HttpSessionException("Invalid response type");
            }

            logger.info("sendAndrReceive>>>> resppose.info>>>>" + response.toString());
            return response;

        } catch (Exception e) {
            logger.error("httpSession请求失败:" + e);
            throw new HttpSessionException(e);
        } finally {
            method.releaseConnection();
        }
    }

    private HttpClient getHttpClient() {
        if (this.client == null) {
            MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
            this.client = new HttpClient(connectionManager);
            this.client.getState().setCredentials(AuthScope.ANY, this.credentials);
            this.client.getHttpConnectionManager().getParams().setConnectionTimeout(20000);
            this.client.getHttpConnectionManager().getParams().setSoTimeout(20000);
        }
        return this.client;
    }

    public String call(String URL, String METHOD) {
        String result = null;
        HttpURLConnection conn = null;
        try {
            URL target = new URL(URL);
            conn = (HttpURLConnection) target.openConnection();
            conn.setRequestMethod(METHOD);
            conn.setRequestProperty("Accept", JSON_CONTENT_TYPE);
            if (200 != conn.getResponseCode()) {
                throw new RuntimeException("failed, error code is " + conn.getResponseCode());
            }
            if (conn.getInputStream() != null) {
                InputStream in = conn.getInputStream();
                byte[] buffer = new byte[1024];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                for (int len = 0; (len = in.read(buffer)) > 0; ) {
                    baos.write(buffer, 0, len);
                }
                result = new String(baos.toByteArray(), "utf-8");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return result;
    }
}
