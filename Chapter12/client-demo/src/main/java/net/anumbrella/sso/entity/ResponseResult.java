package net.anumbrella.sso.entity;

import java.io.Serializable;

public class ResponseResult implements Serializable {

    private String msg;

    private int code;

    public ResponseResult(String msg, int code) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
