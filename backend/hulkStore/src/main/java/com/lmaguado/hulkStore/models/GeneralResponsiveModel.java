package com.lmaguado.hulkStore.models;

import org.springframework.http.HttpStatus;

public class GeneralResponsiveModel {
    private HttpStatus status;
    private Integer code;
    private String message;
    private Object data;

    public HttpStatus getStatus() {
        return status != null ? status : HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public Integer getCode() {
        return code != null ? code : -1;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message != null ? message : "";
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}