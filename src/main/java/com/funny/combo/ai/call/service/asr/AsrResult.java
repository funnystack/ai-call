package com.funny.combo.ai.call.service.asr;

import java.io.Serializable;
import java.util.Date;

public class AsrResult implements Serializable {
    private String sessionId;

    private String extParam;

    private String text;
    /**
     * asr返回的开始时间
     */
    private Date startTime;
    /**
     * asr的结束时间
     */
    private Date completeTime;

    private int status;
    private String statusText;

    /**
     * 阿里的asr task_id
     */
    private String task_id;
    /**
     * asr开始延迟
     */
    private long startLatency;
    /**
     * asr结束延迟
     */
    private long stopLatency;
    private long used;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public long getStopLatency() {
        return stopLatency;
    }

    public void setStopLatency(long stopLatency) {
        this.stopLatency = stopLatency;
    }

    public long getStartLatency() {
        return startLatency;
    }

    public void setStartLatency(long startLatency) {
        this.startLatency = startLatency;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getExtParam() {
        return extParam;
    }

    public void setExtParam(String extParam) {
        this.extParam = extParam;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getUsed() {
        return used;
    }

    public void setUsed(long used) {
        this.used = used;
    }
}
