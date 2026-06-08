package com.whebtos.e_chiro.models;

public class ApiResponse {

    private String Message;

    private Object object;

    private String status;

    private String accessToken;

    private String accessTokenExpiryDate;

    public String getAccessTokenExpiryDate() {
        return accessTokenExpiryDate;
    }

    public void setAccessTokenExpiryDate(String accessTokenExpiryDate) {
        this.accessTokenExpiryDate = accessTokenExpiryDate;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
