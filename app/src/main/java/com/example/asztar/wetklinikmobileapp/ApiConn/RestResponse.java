package com.example.asztar.wetklinikmobileapp.ApiConn;

public class RestResponse {

    private Integer ResponseCode;
    private String Response;

    public RestResponse(Integer responseCode, String response) {
        this.ResponseCode = responseCode;
        this.Response = response;
    }

    public Integer getResponseCode() {
        return ResponseCode;
    }

    public String getResponse() {
        return Response;
    }
}
