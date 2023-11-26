package com.x7ubi.kurswahl.response.admin.classes;

import com.x7ubi.kurswahl.response.common.ResultResponse;

public class TapeResultResponse extends ResultResponse {
    private TapeResponse tapeResponse;

    public TapeResultResponse() {
    }

    public TapeResponse getTapeResponse() {
        return tapeResponse;
    }

    public void setTapeResponse(TapeResponse tapeResponse) {
        this.tapeResponse = tapeResponse;
    }
}
