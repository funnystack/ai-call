package com.funny.combo.ai.call.service.asr;

import java.io.Serializable;

public class AsrPromise implements Serializable {

    private AsrRequest asrRequest;

    private AsrResult asrResult;

    public AsrPromise(AsrRequest asrRequest) {
        this.asrRequest = asrRequest;
    }

    public AsrRequest getAsrRequest() {
        return asrRequest;
    }

    public void setAsrRequest(AsrRequest asrRequest) {
        this.asrRequest = asrRequest;
    }

    public AsrResult getAsrResult() {
        return asrResult;
    }

    public void setAsrResult(AsrResult asrResult) {
        this.asrResult = asrResult;
    }
}
