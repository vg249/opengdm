package org.gobiiproject.gobiiapimodel.payload.sampletracking;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BrApiResult<T> {

    private List<T> data;

    private List<T> calls;

    public List<T> getData() {
        return this.data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public List<T> getCalls() {
        return calls;
    }

    public void setCalls(List<T> calls) {
        this.calls = calls;
    }
}
