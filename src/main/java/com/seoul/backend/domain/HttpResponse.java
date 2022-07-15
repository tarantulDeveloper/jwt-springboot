package com.seoul.backend.domain;

import org.springframework.http.HttpStatus;

public class HttpResponse {
    private int httStatusCode; // 200... - 500...
    private HttpStatus httpStatus;
    private String reason;
    private String message;

    public HttpResponse(){
    }

    public HttpResponse(int httStatusCode, HttpStatus httpStatus, String reason, String message) {
        this.httStatusCode = httStatusCode;
        this.httpStatus = httpStatus;
        this.reason = reason;
        this.message = message;
    }

    public int getHttStatusCode() {
        return httStatusCode;
    }

    public void setHttStatusCode(int httStatusCode) {
        this.httStatusCode = httStatusCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
