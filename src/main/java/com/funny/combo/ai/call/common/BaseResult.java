package com.funny.combo.ai.call.common;

import java.io.Serializable;

/**
 * 接口返回数据格式
 *
 * @author funnystack
 */
public class BaseResult<T> implements Serializable {
    public static final Integer SC_OK = 0;

    private static final long serialVersionUID = 1L;
    /**
     * 返回处理消息
     */
    private String message = "操作成功！";

    /**
     * 返回代码
     */
    private Integer returncode = 0;

    /**
     * 返回数据对象 data
     */
    private T result;

    /**
     * 时间戳
     */
    private long timestamp = System.currentTimeMillis();

    public BaseResult() {

    }


    public BaseResult<T> success(String message) {
        this.message = message;
        this.returncode = SC_OK;
        return this;
    }


    public static <T> BaseResult<T> OK() {
        BaseResult<T> r = new BaseResult<T>();
        r.setReturncode(SC_OK);
        r.setMessage("成功");
        return r;
    }

    public static <T> BaseResult<T> OK(T data) {
        BaseResult<T> r = new BaseResult<T>();
        r.setReturncode(SC_OK);
        r.setResult(data);
        return r;
    }

    public static <T> BaseResult<T> OK(String msg, T data) {
        BaseResult<T> r = new BaseResult<T>();
        r.setReturncode(SC_OK);
        r.setMessage(msg);
        r.setResult(data);
        return r;
    }

    public static <T> BaseResult<T> error(String msg) {
        return error(500, msg);
    }

    public static <T> BaseResult<T> error(int code, String msg) {
        BaseResult<T> r = new BaseResult<T>();
        r.setReturncode(code);
        r.setMessage(msg);
        return r;
    }

    public BaseResult<T> error500(String message) {
        this.message = message;
        this.returncode = 500;
        return this;
    }

    public void setFailure(String message) {
        this.returncode = -1;
        this.message = message;
    }

    public boolean isSuccess() {
        return returncode != null && returncode.equals(SC_OK) ? true : false;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getReturncode() {
        return returncode;
    }

    public void setReturncode(Integer returncode) {
        this.returncode = returncode;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
