package org.gobiiproject.gobiibrapi.core.derived2;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Phil on 5/1/2017.
 */
public class BrapiResponseEnvelopeDetail<T_CALL_RESPONSE extends BrapiResponseDataList> extends BrapResponseEnvelope {

    private T_CALL_RESPONSE result;
    @JsonProperty("result")
    public T_CALL_RESPONSE getResult() {
        return this.result;
    }

    @JsonProperty("result")
    public void setResult(T_CALL_RESPONSE result) {
        this.result = result;
    }
}


