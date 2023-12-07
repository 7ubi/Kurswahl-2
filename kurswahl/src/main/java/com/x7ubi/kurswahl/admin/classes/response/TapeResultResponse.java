package com.x7ubi.kurswahl.admin.classes.response;

import com.x7ubi.kurswahl.common.response.ResultResponse;

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
