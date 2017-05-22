package org.gobiiproject.gobiiclient.core.common;

import com.google.gson.JsonObject;

import java.net.URI;


/**
 * Created by Phil on 9/21/2016.
 */
public class HttpMethodResult {

    int responseCode;

    String reasonPhrase;
    JsonObject jsonPayload;
    StringBuilder plainPayload = new StringBuilder();
    URI uri;

    public int getResponseCode() {
        return responseCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public URI getUri() {
        return uri;
    }

    public void setResponse(int responseCode, String reasonPhrase, URI uri) {
        this.responseCode = responseCode;
        this.reasonPhrase = reasonPhrase;
        this.uri = uri;
    }

    public JsonObject getJsonPayload() {
        return jsonPayload;
    }

    public void setJsonPayload(JsonObject jsonPayload) {
        this.jsonPayload = jsonPayload;
    }

    public StringBuilder getPlainPayload() {
        return plainPayload;
    }

    public void setPlainPayload(StringBuilder plainPayload) {
        this.plainPayload = plainPayload;
    }
}
