package com.example.asztar.wetklinikmobileapp.ApiConn;

import android.content.Context;

import com.example.asztar.wetklinikmobileapp.Token;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class ApiConnection {
    private HttpsURLConnection urlConnection;
    private String reqMethod;
    private String request;
    private String baseUrl;
    private boolean authorize = false;
    private HashMap<String, String> reqBody = new HashMap<>();
    private Token token = Token.getInstance();
    private HashMap<String, String> reqHeader = new HashMap<>();
    private String putenc = "";

    public HttpsURLConnection getUrlProvider() {
        return urlConnection;
    }

    public void setUrlProvider(HttpsURLConnection urlConnection) {
        this.urlConnection = urlConnection;
    }

    public String getReqMethod() {
        return reqMethod;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public boolean isAuthorize() {
        return authorize;
    }

    public void setAuthorize(boolean authorize) {
        this.authorize = authorize;
    }

    public void setReqMethod(String reqMethod) {
        this.reqMethod = reqMethod;
    }


    public ApiConnection(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public RestResponse execute() {
        try {
            if (reqMethod == "PUT")
                putenc = getPutString(reqBody);
            createUrl();
            urlConnection.setRequestMethod(reqMethod);
            reqMethod = urlConnection.getRequestMethod();
            if (authorize) {
                urlConnection.addRequestProperty("AUTHORIZATION", "Bearer " + token.getAccess_token());
            }
            for (Map.Entry<String, String> entry : reqHeader.entrySet()) {
                urlConnection.addRequestProperty(entry.getKey(), entry.getValue());
            }

            urlConnection.setDoOutput(false);
            if (reqMethod == "POST" && !reqBody.isEmpty()) {
                urlConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setDoOutput(true);
                String ecd = getDataString(reqBody);
                OutEncodeUrl((ecd), urlConnection);
            }
            urlConnection.connect();
            Integer respCode = urlConnection.getResponseCode();
            String modelString = "";
            if (respCode == 200)
                modelString = GetModelString(urlConnection);
            urlConnection.disconnect();
            return new RestResponse(respCode, modelString);
        } catch (Exception e) {
            e.getMessage();
        }
        urlConnection.disconnect();
        return new RestResponse(-1, null);
    }

    private String getDataString(HashMap<String, String> encoded) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : encoded.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    private String getPutString(HashMap<String, String> encoded) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> entry : encoded.entrySet()) {
            result.append("?");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    public void addUrlSegment(String parameter, String value) {
        request = request.replace(parameter, value);
    }

    public void addHeaderParameter(String parameter, String value) {
        reqHeader.put(parameter, value);
    }

    public void addBodyParameter(String parameter, String value) {
        reqBody.put(parameter, value);
    }

    private void createUrl() {
        try {
            URL _url = new URL(baseUrl + request + putenc);
            HttpsURLConnection urlConnection = (HttpsURLConnection) _url.openConnection();
            this.urlConnection = urlConnection;
        } catch (Exception e) {
        }
    }

    private void OutEncodeUrl(String encodedURL, HttpsURLConnection urlConnection) throws IOException {
        OutputStream out = null;
        out = new BufferedOutputStream(urlConnection.getOutputStream());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.write(encodedURL);
        writer.flush();
        writer.close();
        out.close();
    }

    private String GetModelString(HttpsURLConnection urlConnection) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader((urlConnection.getInputStream())));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}



