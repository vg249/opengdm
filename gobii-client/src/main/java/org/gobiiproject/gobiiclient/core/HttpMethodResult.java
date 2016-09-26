package org.gobiiproject.gobiiclient.core;

import com.google.gson.JsonObject;

/**
 * Created by Phil on 9/21/2016.
 */
public class HttpMethodResult {

    int responseCode;

    String reasonPhrase;
    JsonObject payLoad;

    public int getResponseCode() {
        return responseCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }


    public void setResponse(int responseCode, String reasonPhrase) {
        this.responseCode = responseCode;
        this.reasonPhrase = reasonPhrase;
    }

    public JsonObject getPayLoad() {
        return payLoad;
    }

    public void setPayLoad(JsonObject payLoad) {
        this.payLoad = payLoad;
    }
}
