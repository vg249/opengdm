package org.gobiiproject.gobiiclient.core;

import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.dto.types.ServiceRequestId;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Phil on 5/13/2016.
 */
public class DtoRequestProcessor<T> {

    Logger LOGGER = LoggerFactory.getLogger(DtoRequestProcessor.class);

    private TypedRestRequest<T> makeTypedRequest(Class<T> DtoType) {

        TypedRestRequest<T> returnVal = null;

        try {
            String baseUrl = ClientContext.getInstance().getCurrentCropDomain();
            Integer port = ClientContext.getInstance().getCurrentCropPort();
            returnVal = new TypedRestRequest<>(baseUrl, port, DtoType);
        } catch (Exception e) {
            LOGGER.error("error configuring request", e);
        }

        return returnVal;

    }

    public T process(T dtoToProcess, Class<T> DtoType,
                     ControllerType controllerType,
                     ServiceRequestId requestId) throws Exception {

        T returnVal = null;

        TypedRestRequest<T> typedRestRequest = makeTypedRequest(DtoType);

        String token = ClientContext.getInstance().getUserToken();

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
