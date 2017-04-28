package org.gobiiproject.gobiibrapi.core.derived;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.gobiiproject.gobiibrapi.core.common.BrapiMetaData;
import org.gobiiproject.gobiibrapi.core.json.BrapiJsonKeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 12/19/2016.
 */
public class BrapiResponseEnvelopeList<T_RESPONSE_TYPE_DETAIL> {

    public BrapiResponseEnvelopeList() {}

    private BrapiMetaData brapiMetaData = new BrapiMetaData();

    private List<T_RESPONSE_TYPE_DETAIL> data;

    @JsonProperty("metadata")
    public BrapiMetaData getBrapiMetaData() {
        return brapiMetaData;
    }

    @JsonProperty("metadata")
    public void setBrapiMetaData(BrapiMetaData brapiMetaData) {
        this.brapiMetaData = brapiMetaData;
    }

    Map<String, List<T_RESPONSE_TYPE_DETAIL>> result = new HashMap<>();

    @JsonProperty(BrapiJsonKeys.RESULT)
    public Map<String, List<T_RESPONSE_TYPE_DETAIL>> getResult() {
        return result;
    }

    @JsonProperty(BrapiJsonKeys.RESULT)
    public void setResult(Map<String, List<T_RESPONSE_TYPE_DETAIL>> result) {
        this.result = result;
    }

    public List<T_RESPONSE_TYPE_DETAIL> getData() {

        List<T_RESPONSE_TYPE_DETAIL> returnVal = new ArrayList<>();

        if(this.result.containsKey(BrapiJsonKeys.RESULT_DATA)) {
            returnVal = this.result.get(BrapiJsonKeys.RESULT_DATA);
        }

        return returnVal;
    }

    public void setData(List<T_RESPONSE_TYPE_DETAIL> data) {

        this.result.put(BrapiJsonKeys.RESULT_DATA,data);
    }
}
