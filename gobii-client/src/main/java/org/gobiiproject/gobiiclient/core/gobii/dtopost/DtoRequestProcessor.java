package org.gobiiproject.gobiiclient.core.gobii.dtopost;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.common.HttpCore;
import org.gobiiproject.gobiiclient.core.common.HttpMethodResult;
import org.gobiiproject.gobiiapimodel.restresources.gobii.GobiiUriFactory;
import org.gobiiproject.gobiimodel.tobemovedtoapimodel.Header;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Phil on 5/13/2016.
 */
public class DtoRequestProcessor<T extends Header> {

    Logger LOGGER = LoggerFactory.getLogger(DtoRequestProcessor.class);

    public T process(T dtoToProcess,
                     Class<T> dtoType,
                     GobiiControllerType gobiiControllerType,
                     GobiiServiceRequestId requestId) throws Exception {


        T returnVal = null;

        if (GobiiClientContext.isInitialized()) {
            String token = GobiiClientContext.getInstance(null, false).getUserToken();
            if (null == token || token.isEmpty()) {
                throw (new Exception("there is no user token; user must log in"));
            }

            String host = GobiiClientContext.getInstance(null, false).getCurrentCropDomain();
            Integer port = GobiiClientContext.getInstance(null, false).getCurrentCropPort();
            String cropContextRoot = GobiiClientContext.getInstance(null, false).getCurrentCropContextRoot();

            String cropType = GobiiClientContext.getInstance(null, false).getCurrentClientCropType();
            dtoToProcess.setCropType(cropType);


            String url = requestId.getRequestUrl(cropContextRoot, gobiiControllerType.getControllerPath());

            returnVal = this.getTypedHtppResponseForDto(
                    host,
                    port,
                    cropContextRoot,
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
                                        String cropContextRoot,
                                        String url,
                                        Class<T> paramType,
                                        T dtoInstance,
                                        String token) throws Exception {

        T returnVal;

        HttpCore httpCore = GobiiClientContext.getInstance(null,false).getHttp();


        ObjectMapper objectMapper = new ObjectMapper();
        String dtoRequestJson = objectMapper.writeValueAsString(dtoInstance);

        //        JsonObject responseJson = httpCore.getResponseBody(url, dtoRequestJson, token);

        HttpMethodResult httpMethodResult = httpCore.post((new GobiiUriFactory(cropContextRoot)).RestUriFromUri(url),
                dtoRequestJson,
                token);


        returnVal = objectMapper.readValue(httpMethodResult.getPayLoad().toString(), paramType);

        return returnVal;


    } // getTypedHtppResponseForDto()
}
