package com.sikabayan.restful;



import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by alifkurniawan on 6/23/16.
 */
public class Restful {

    private String authorization;
    private String urlBase;

    private static Restful ourInstance;

    public static Restful getInstance(String authorization, String urlBase) {
        if (ourInstance == null)
            ourInstance = new Restful(authorization, urlBase);
        return ourInstance;
    }

    private Restful(String authorization, String urlBase) {
        this.authorization = authorization;
        this.urlBase = urlBase;
    }

    public Response get(String method, HashMap<String, String> header, HashMap<String, String> parameters) {

        String content = "";
        int statusCode = 0;

        // Add Parameters
        String params = "";
        if (parameters!=null || parameters.size() !=0) {
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
            statusCode = conn.getResponseCode();
            conn.setRequestProperty("Authorization", this.authorization);
            InputStream is = new BufferedInputStream(conn.getInputStream());
            content = readStream(is);
            return new Response(content, statusCode);
        } catch (Exception e) {
            return new Response(content, statusCode);
        }
    }

    public Response post(String url, String method, HashMap<String, String> header, HashMap<String, String> parameters) {
        String content = "";
        int statusCode = 0;

        return new Response(content, statusCode);
    }

    private static String readStream(InputStream in) throws Exception {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String nextLine;
        while ((nextLine = reader.readLine()) != null) {
            sb.append(nextLine);
        }
        return sb.toString();
    }



}
