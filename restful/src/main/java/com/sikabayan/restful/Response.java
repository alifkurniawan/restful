package com.sikabayan.restful;

/**
 * Created by alifkurniawan on 6/23/16.
 */

public class Response {
    private String content;
    private int statusCode;


    public Response(String content, int statusCode) {
        this.content = content;
        this.statusCode = statusCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isSuccess() {
        return statusCode == 200;
    }
}