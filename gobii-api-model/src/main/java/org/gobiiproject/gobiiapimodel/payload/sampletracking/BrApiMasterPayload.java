package org.gobiiproject.gobiiapimodel.payload.sampletracking;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BrApiMasterPayload<T> {

    private BrApiMetaData metaData = new BrApiMetaData();
    private T result;

    public BrApiMasterPayload(T result) {
        this.result = result;
    }


    public T getResult() {
        return this.result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public BrApiMetaData getMetaData() {
        return this.metaData;
    }

    public void setMetaData(BrApiMetaData metaData) {
        this.metaData = metaData;
    }

}
