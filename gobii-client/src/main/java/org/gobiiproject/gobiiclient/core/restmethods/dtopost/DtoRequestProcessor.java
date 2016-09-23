package org.gobiiproject.gobiiclient.core.restmethods.dtopost;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.HttpCore;
import org.gobiiproject.gobiiclient.core.HttpMethodResult;
import org.gobiiproject.gobiiclient.core.ResourceBuilder;
import org.gobiiproject.gobiiclient.core.restmethods.UriFactory;
import org.gobiiproject.gobiimodel.dto.response.Header;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.dto.types.ServiceRequestId;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Phil on 5/13/2016.
 */
public class DtoRequestProcessor<T extends Header> {

    Logger LOGGER = LoggerFactory.getLogger(DtoRequestProcessor.class);

    public T process(T dtoToProcess,
                     Class<T> dtoType,
                     ControllerType controllerType,
                     ServiceRequestId requestId) throws Exception {


        T returnVal = null;

        if (ClientContext.isInitialized()) {
            String token = ClientContext.getInstance(null, false).getUserToken();
            if (null == token || token.isEmpty()) {
                throw (new Exception("there is no user token; user must log in"));
            }

            String host = ClientContext.getInstance(null, false).getCurrentCropDomain();
            Integer port = ClientContext.getInstance(null, false).getCurrentCropPort();

            String cropType = ClientContext.getInstance(null, false).getCurrentClientCropType();
            dtoToProcess.setCropType(cropType);


            String url = ResourceBuilder.getRequestUrl(controllerType,
                    requestId);

            returnVal = this.getTypedHtppResponseForDto(
                    host,
                    port,
                    url,
                    dtoType,
                    dtoToProcess,
                    token);
        } else {
            throw new Exception("Uninitialized client context");

        }

        return returnVal;
    }

    public T getTypedHtppResponseForDto(String host,
                                        Integer port,
                                        String url,
                                        Class<T> paramType,
                                        T dtoInstance,
                                        String token) throws Exception {

        T returnVal;

        HttpCore httpCore = new HttpCore(host, port);


        ObjectMapper objectMapper = new ObjectMapper();
        String dtoRequestJson = objectMapper.writeValueAsString(dtoInstance);

        //        JsonObject responseJson = httpCore.getResponseBody(url, dtoRequestJson, token);

        HttpMethodResult httpMethodResult = httpCore.post(UriFactory.RestUriFromUri(url), dtoRequestJson, token);


        returnVal = objectMapper.readValue(httpMethodResult.getPayLoad().toString(), paramType);

        return returnVal;


    } // getTypedHtppResponseForDto()
}
