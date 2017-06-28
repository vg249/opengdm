package org.gobiiproject.gobiiclient.core.common;

import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;

import java.net.URI;


/**
 * Created by Phil on 9/21/2016.
 */
public class HttpMethodResult {


    HttpMethodResult(HttpResponse httpResponse) {
        this.responseCode = httpResponse.getStatusLine().getStatusCode();
        this.reasonPhrase = httpResponse.getStatusLine().getReasonPhrase();
    }

    int responseCode;
    String reasonPhrase;
    JsonObject payLoad;
    URI uri;
    String token;
    String message;

    public int getResponseCode() {
        return responseCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public JsonObject getPayLoad() {
        return payLoad;
    }

    public void setPayLoad(JsonObject payLoad) {
        this.payLoad = payLoad;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
