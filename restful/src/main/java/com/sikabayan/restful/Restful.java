package com.sikabayan.restful;


import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by alifkurniawan on 6/23/16.
 */
public class Restful {

    private static Restful ourInstance;
    private int timeOut = 500;
    private String authorization;
    private String urlBase;

    private Restful(String authorization, String urlBase) {
        this.authorization = authorization;
        this.urlBase = urlBase;
    }

    public static Restful getInstance(String authorization, String urlBase) {
        if (ourInstance == null)
            ourInstance = new Restful(authorization, urlBase);
        return ourInstance;
    }

    private static String readResult(HttpURLConnection conn) throws Exception {
        InputStream in = new BufferedInputStream(conn.getInputStream());
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String nextLine;
        while ((nextLine = reader.readLine()) != null) {
            sb.append(nextLine);
        }
        return sb.toString();
    }

    public Response get(String method, HashMap<String, String> header, HashMap<String, String> parameters) {

        String content = "";
        int statusCode = 0;

        // Add Parameters
        String params = "";
        if (parameters != null && parameters.size() != 0) {
            params = "?";
            for (Map.Entry<String, String> param : parameters.entrySet()) {
                if (params.length() > 1) {
                    params += "&";
                }

                params += param.getKey() + "=" + param.getValue();
            }
        }
        try {
            URL url = new URL(this.urlBase + method + params);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(this.timeOut);
            statusCode = conn.getResponseCode();


            // Set Header
            if (header != null && parameters.size() != 0) {
                for (Map.Entry<String, String> head : header.entrySet())
                    conn.setRequestProperty(head.getKey(), head.getValue());
            }
            conn.setRequestProperty("Authorization", this.authorization);

            // Read Result
            content = readResult(conn);

            return new Response(content, statusCode);
        } catch (Exception e) {
            return new Response(content, statusCode);
        }
    }

    public Response post(String method, JSONObject jsonObject) {
        String content = "";
        int statusCode = 0;

        try {
            URL url = new URL(this.urlBase + method);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(this.timeOut);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", this.authorization);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(jsonObject.toString());
            wr.flush();

            // Read Result
            content = this.readResult(conn);

            return new Response(content, statusCode);

        } catch (Exception e) {
            return new Response(content, 0);
        }
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }



}
