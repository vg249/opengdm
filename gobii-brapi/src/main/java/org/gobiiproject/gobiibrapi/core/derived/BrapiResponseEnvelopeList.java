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

    @JsonProperty("metadata")
    public BrapiMetaData getBrapiMetaData() {
        return brapiMetaData;
    }

    @JsonProperty("metadata")
    public void setBrapiMetaData(BrapiMetaData brapiMetaData) {
        this.brapiMetaData = brapiMetaData;
    }

    Map<String, Object> result = new HashMap<>();

    @JsonProperty(BrapiJsonKeys.RESULT)
    public Map<String, Object> getResult() {
        return result;
    }

    @JsonProperty(BrapiJsonKeys.RESULT)
    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    @SuppressWarnings("unchecked")
    public List<T_RESPONSE_TYPE_DETAIL> getResultData() {

        List<T_RESPONSE_TYPE_DETAIL> returnVal = new ArrayList<>();

        if(this.result.containsKey(BrapiJsonKeys.RESULT_DATA)) {
            returnVal = (List<T_RESPONSE_TYPE_DETAIL>) this.result.get(BrapiJsonKeys.RESULT_DATA);
        }

        return returnVal;
    }

    public void setResultData(List<T_RESPONSE_TYPE_DETAIL> data) {

        this.result.put(BrapiJsonKeys.RESULT_DATA,data);
    }


}
