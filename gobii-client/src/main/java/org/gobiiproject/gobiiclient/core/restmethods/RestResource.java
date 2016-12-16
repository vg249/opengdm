package org.gobiiproject.gobiiclient.core.restmethods;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.HttpCore;
import org.gobiiproject.gobiiclient.core.HttpMethodResult;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;

import org.gobiiproject.gobiimodel.types.RestMethodTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Phil on 5/13/2016.
 */
public class RestResource<T> {

    Logger LOGGER = LoggerFactory.getLogger(RestResource.class);

    private RestUri restUri;
    private ObjectMapper objectMapper = new ObjectMapper();
    private PayloadResponse<T> payloadResponse = null;
    private RestResourceUtils restResourceUtils;

    public RestResource(RestUri restUri) {
        this.restUri = restUri;
        this.restResourceUtils = new RestResourceUtils();
        this.payloadResponse = new PayloadResponse<>(this.restUri);
    }

    public void setParamValue(String paramName, String value) throws Exception {
        restUri.setParamValue(paramName, value);
    }


    private ClientContext getClientContext() throws Exception {

        return this.restResourceUtils.getClientContext();
    }

    private HttpCore getHttp() throws Exception {

        return this.restResourceUtils.getHttp();
    }


    private String makeHttpBody(PayloadEnvelope<T> payloadEnvelope) throws Exception {

        String returnVal = null;

        if (payloadEnvelope.getPayload().getData().size() > 0) {
            returnVal = this.objectMapper.writeValueAsString(payloadEnvelope);
        }

        return returnVal;
    }


    public PayloadEnvelope<T> get(Class<T> dtoType) throws Exception {

        PayloadEnvelope<T> returnVal;

        HttpMethodResult httpMethodResult =
                getHttp()
                        .get(this.restUri,
                                this.getClientContext().getUserToken());

        returnVal = this.payloadResponse.getPayloadFromResponse(dtoType,
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

        returnVal = this.payloadResponse.getPayloadFromResponse(dtoType,
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

        returnVal = this.payloadResponse.getPayloadFromResponse(dtoType,
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