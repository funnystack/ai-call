package com.funny.combo.ai.call.service.asr;

import com.funny.combo.ai.call.common.BaseResult;

public interface AsrService {

    BaseResult<AsrResult> asr(AsrRequest asrRequest);
}
