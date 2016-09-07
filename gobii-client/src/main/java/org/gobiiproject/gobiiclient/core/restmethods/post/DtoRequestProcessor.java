package org.gobiiproject.gobiiclient.core.restmethods.post;

import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.Urls;
import org.gobiiproject.gobiimodel.dto.response.Header;
import org.gobiiproject.gobiimodel.dto.response.RequestEnvelope;
import org.gobiiproject.gobiimodel.dto.response.ResultEnvelope;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.dto.types.ServiceRequestId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Phil on 5/13/2016.
 */
public class DtoRequestProcessor<T extends Header> {

    Logger LOGGER = LoggerFactory.getLogger(DtoRequestProcessor.class);

    public T process(T dtoToProcess, Class<T> DtoType,
                     ControllerType controllerType,
                     ServiceRequestId requestId) throws Exception {

        String token = ClientContext.getInstance(null, false).getUserToken();
        String serviceDomain = ClientContext.getInstance(null, false).getCurrentCropDomain();
        Integer servicePort = ClientContext.getInstance(null, false).getCurrentCropPort();

        return this.process(dtoToProcess,
                DtoType,
                controllerType,
                requestId,
                serviceDomain,
                servicePort,
                token);

    }


    public ResultEnvelope<T> processEnvelope(RequestEnvelope<T> requestEnvelope, Class<T> DtoType,
                                             ControllerType controllerType,
                                             ServiceRequestId requestId) throws Exception {

        String token = ClientContext.getInstance(null, false).getUserToken();
        String host = ClientContext.getInstance(null, false).getCurrentCropDomain();
        Integer port = ClientContext.getInstance(null, false).getCurrentCropPort();

        ResultEnvelope<T> returnVal = null;

        TypedRestRequest<T> typedRestRequest= new TypedRestRequest<>(host, port, DtoType);


        if (null == token || token.isEmpty()) {
            throw (new Exception("there is no user token; user must log in"));
        }

        String url = Urls.getRequestUrl(controllerType,
                requestId);

        returnVal = typedRestRequest.getTypedHtppResponseForDtoEnvelope(url,
                requestEnvelope,
                token);

        return returnVal;
    }


    public T process(T dtoToProcess,
                     Class<T> DtoType,
                     ControllerType controllerType,
                     ServiceRequestId requestId,
                     String host,
                     Integer port,
                     String token) throws Exception {

        T returnVal = null;

        TypedRestRequest<T> typedRestRequest= new TypedRestRequest<>(host, port, DtoType);


        if (null == token || token.isEmpty()) {
            throw (new Exception("there is no user token; user must log in"));
        }

        String url = Urls.getRequestUrl(controllerType,
                requestId);

        returnVal = typedRestRequest.getTypedHtppResponseForDto(url,
                dtoToProcess,
                token);

        return returnVal;

    }

}
