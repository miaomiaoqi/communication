package com.miaoqi.webserver.connector;

public enum HttpStatus {
    
    /**
     * 正常响应
     */
    SC_OK(200, "OK"),
    /**
     * 资源未找到
     */
    SC_NOT_FOUND(404, "File Not Found");

    private int statusCode;
    private String reason;

    HttpStatus(int code, String reason) {
        this.statusCode = code;
        this.reason = reason;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getReason() {
        return this.reason;
    }

}
