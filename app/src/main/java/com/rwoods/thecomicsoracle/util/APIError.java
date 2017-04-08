package com.rwoods.thecomicsoracle.util;

/**
 * Created by rwoods on 3/7/2016.
 */
public class APIError {

    private int statusCode;
    private String message;

    public APIError() {
    }

    public int status() {
        return statusCode;
    }

    public String message() {
        return message;
    }
}