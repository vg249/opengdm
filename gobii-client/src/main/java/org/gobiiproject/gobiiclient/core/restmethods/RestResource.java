package org.gobiiproject.gobiiclient.core.restmethods;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.HttpCore;
import org.gobiiproject.gobiiclient.core.HttpMethodResult;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;

import org.gobiiproject.gobiiapimodel.types.RestMethodTypes;
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
        restUri.setParamValue(paramName, value);
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
        String clientContextRoot = ClientContext.getInstance(null, false).getCurrentCropContextRoot();
        httpCore = new HttpCore(host, port, clientContextRoot);


        return httpCore;
    }

    private String makeMessageFromHttpResult(String method,
                                             HttpMethodResult httpMethodResult) throws Exception {

        return (makeMessageFromHttpResult(method, httpMethodResult, null));
    }


    private String makeMessageFromHttpResult(String method,
                                             HttpMethodResult httpMethodResult,
                                             String additionalReason) throws Exception {
        return method.toUpperCase()
                + " method on "
                + this.restUri.makeUrl()
                + " failed with status code "
                + Integer.toString(httpMethodResult.getResponseCode())
                + ": "
                + httpMethodResult.getReasonPhrase()
                + ": "
                + additionalReason;
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

        if (HttpStatus.SC_NOT_FOUND != httpMethodResult.getResponseCode()) {

            if (HttpStatus.SC_BAD_REQUEST != httpMethodResult.getResponseCode()) {

                try {

                    returnVal = new PayloadEnvelope<T>()
                            .fromJson(httpMethodResult.getPayLoad(),
                                    dtoType);

                    // it's possible that you can have codes other than success, and still have valid response
                    // body
                    if (httpMethodResult.getResponseCode() != httpSuccessCode) {

                        String message = makeMessageFromHttpResult(restMethodType.toString(), httpMethodResult);

                        GobiiStatusLevel gobiiStatusLevel = returnVal.getHeader().getStatus().isSucceeded() ?
                                GobiiStatusLevel.WARNING :
                                GobiiStatusLevel.ERROR;

                        returnVal.getHeader()
                                .getStatus()
                                .addStatusMessage(gobiiStatusLevel,
                                        message);
                    }


                } catch (Exception e) {
                    returnVal.getHeader().getStatus().addException(e);
                }

            } else {

                returnVal.getHeader()
                        .getStatus()
                        .addStatusMessage(GobiiStatusLevel.ERROR,
                                makeMessageFromHttpResult(restMethodType.toString(),
                                        httpMethodResult,
                                        "One or more client DTOs may be out of date with those of the server"));
            }

        } else {
            returnVal.getHeader()
                    .getStatus()
                    .addStatusMessage(GobiiStatusLevel.ERROR,
                            makeMessageFromHttpResult(restMethodType.toString(),
                                    httpMethodResult,
                                    "The specified URI may be unknown to the server"));
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

        returnVal.getHeader().getStatus().addStatusMessage(GobiiStatusLevel.ERROR, "Method not implemented");


        return returnVal;

    }

    public PayloadEnvelope<T> delete(Class<T> dtoType,
                                     PayloadEnvelope<T> requestPayload) throws Exception {

        PayloadEnvelope<T> returnVal = new PayloadEnvelope<>();

        returnVal.getHeader().getStatus().addStatusMessage(GobiiStatusLevel.ERROR, "Method not implemented");


        return returnVal;

    }

    public PayloadEnvelope<T> options(Class<T> dtoType,
                                      PayloadEnvelope<T> requestPayload) throws Exception {

        PayloadEnvelope<T> returnVal = new PayloadEnvelope<>();

        returnVal.getHeader().getStatus().addStatusMessage(GobiiStatusLevel.ERROR, "Method not implemented");


        return returnVal;

    }

}