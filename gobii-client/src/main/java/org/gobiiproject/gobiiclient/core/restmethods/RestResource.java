package org.gobiiproject.gobiiclient.core.restmethods;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.sun.scenario.effect.impl.sw.java.JSWBlend_EXCLUSIONPeer;
import org.apache.http.HttpStatus;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.HttpCore;
import org.gobiiproject.gobiiclient.core.HttpMethodResult;
import org.gobiiproject.gobiimodel.dto.response.PayloadEnvelope;
import org.gobiiproject.gobiimodel.dto.response.Status;
import org.gobiiproject.gobiimodel.types.RestMethodTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Phil on 5/13/2016.
 */
public class RestResource<T> {

    Logger LOGGER = LoggerFactory.getLogger(RestResource.class);

    private RestUri restUri;
    private HttpCore httpCore;
    private ClientContext clientContext;
    private ObjectMapper objectMapper = new ObjectMapper();

    public RestResource(RestUri restUri) {
        this.restUri = restUri;
    }

    public void setParamValue(String paramName, String value) throws Exception {
        restUri.setParamValue(paramName,value);
    }


    private ClientContext getClientContext() throws Exception {

        if (!ClientContext.isInitialized()) {
            throw new Exception("Client context is not initialized");
        }

        clientContext = ClientContext.getInstance(null, false);

        return clientContext;

    }

    private HttpCore getHttp() throws Exception {


        String host = ClientContext.getInstance(null, false).getCurrentCropDomain();
        Integer port = ClientContext.getInstance(null, false).getCurrentCropPort();
        httpCore = new HttpCore(host, port);


        return httpCore;
    }

    private String makeMessageFromHttpResult(String method,
                                             HttpMethodResult httpMethodResult) throws Exception {
        return method.toUpperCase()
                + " method on "
                + this.restUri.makeUrl()
                + " failed with status code "
                + Integer.toString(httpMethodResult.getResponseCode())
                + ": "
                + httpMethodResult.getReasonPhrase();
    }

    private String makeHttpBody(PayloadEnvelope<T> payloadEnvelope) throws Exception {

        String returnVal = null;

        if (payloadEnvelope.getPayload().getData().size() > 0) {
            returnVal = this.objectMapper.writeValueAsString(payloadEnvelope);
        }

        return returnVal;
    }


    public PayloadEnvelope<T> getPayloadFromResponse(Class<T> dtoType,
                                                     RestMethodTypes restMethodType,
                                                     int httpSuccessCode,
                                                     HttpMethodResult httpMethodResult) throws Exception {

        PayloadEnvelope<T> returnVal = new PayloadEnvelope<>();

        try {
            returnVal = new PayloadEnvelope<T>()
                    .fromJson(httpMethodResult.getPayLoad(),
                            dtoType);

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        if (httpMethodResult.getResponseCode() != httpSuccessCode) {

            String message = makeMessageFromHttpResult(restMethodType.toString(), httpMethodResult);

            Status.StatusLevel statusLevel = returnVal.getHeader().getStatus().isSucceeded() ?
                    Status.StatusLevel.WARNING :
                    Status.StatusLevel.ERROR;

            returnVal.getHeader()
                    .getStatus()
                    .addStatusMessage(statusLevel,
                            message);
        }

        return returnVal;
    }

    public PayloadEnvelope<T> get(Class<T> dtoType) throws Exception {

        PayloadEnvelope<T> returnVal;

        HttpMethodResult httpMethodResult =
                getHttp()
                        .get(this.restUri,
                                this.getClientContext().getUserToken());

        returnVal = this.getPayloadFromResponse(dtoType,
                RestMethodTypes.GET,
                HttpStatus.SC_OK,
                httpMethodResult);

        return returnVal;
    }


    public PayloadEnvelope<T> post(Class<T> dtoType,
                                   PayloadEnvelope<T> requestPayload) throws Exception {

        PayloadEnvelope<T> returnVal;

        String postBody = this.makeHttpBody(requestPayload);
        HttpMethodResult httpMethodResult =
                getHttp()
                        .post(this.restUri,
                                postBody,
                                this.getClientContext().getUserToken());

        returnVal = this.getPayloadFromResponse(dtoType,
                RestMethodTypes.POST,
                HttpStatus.SC_CREATED,
                httpMethodResult);

        return returnVal;

    }

    public PayloadEnvelope<T> put(Class<T> dtoType,
                                  PayloadEnvelope<T> requestPayload) throws Exception {

        PayloadEnvelope<T> returnVal;

        String putBody = this.makeHttpBody(requestPayload);
        HttpMethodResult httpMethodResult =
                getHttp()
                        .put(this.restUri,
                                putBody,
                                this.getClientContext().getUserToken());

        returnVal = this.getPayloadFromResponse(dtoType,
                RestMethodTypes.PUT,
                HttpStatus.SC_OK,
                httpMethodResult);

        return returnVal;

    }


    public PayloadEnvelope<T> patch(Class<T> dtoType,
                                    PayloadEnvelope<T> requestPayload) throws Exception {

        PayloadEnvelope<T> returnVal = new PayloadEnvelope<>();

        returnVal.getHeader().getStatus().addStatusMessage(Status.StatusLevel.ERROR, "Method not implemented");


        return returnVal;

    }

    public PayloadEnvelope<T> delete(Class<T> dtoType,
                                     PayloadEnvelope<T> requestPayload) throws Exception {

        PayloadEnvelope<T> returnVal = new PayloadEnvelope<>();

        returnVal.getHeader().getStatus().addStatusMessage(Status.StatusLevel.ERROR, "Method not implemented");


        return returnVal;

    }

    public PayloadEnvelope<T> options(Class<T> dtoType,
                                      PayloadEnvelope<T> requestPayload) throws Exception {

        PayloadEnvelope<T> returnVal = new PayloadEnvelope<>();

        returnVal.getHeader().getStatus().addStatusMessage(Status.StatusLevel.ERROR, "Method not implemented");


        return returnVal;

    }

}