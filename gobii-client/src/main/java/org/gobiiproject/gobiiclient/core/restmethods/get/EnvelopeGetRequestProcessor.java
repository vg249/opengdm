package org.gobiiproject.gobiiclient.core.restmethods.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.HttpCore;
import org.gobiiproject.gobiiclient.core.Urls;
import org.gobiiproject.gobiiclient.core.restmethods.post.EnvelopeRestRequest;
import org.gobiiproject.gobiimodel.dto.response.RequestEnvelope;
import org.gobiiproject.gobiimodel.dto.response.ResultEnvelope;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.dto.types.ServiceRequestId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Phil on 5/13/2016.
 */
public class EnvelopeGetRequestProcessor<T> {

    Logger LOGGER = LoggerFactory.getLogger(EnvelopeGetRequestProcessor.class);


    public ResultEnvelope<T> processGetRequest(GetRequest getRequest,
                                               Class<T> DtoType) throws Exception {

        ResultEnvelope<T> returnVal = null;

        if (ClientContext.isInitialized()) {

            String token = ClientContext.getInstance(null, false).getUserToken();
            if (null == token || token.isEmpty()) {
                throw (new Exception("there is no user token; user must log in"));
            }

            String host = ClientContext.getInstance(null, false).getCurrentCropDomain();
            Integer port = ClientContext.getInstance(null, false).getCurrentCropPort();


//            EnvelopeGetRequest<T> envelopeGetRequest = new EnvelopeGetRequest<>(host, port, DtoType);
//            returnVal = envelopeGetRequest.getTypedHtppResponseForDtoEnvelope(getRequest,
//                    token);


            HttpCore httpCore = new HttpCore(host, port);
            JsonObject responseJson = httpCore.getFromGet(getRequest, token);
            ObjectMapper objectMapper = new ObjectMapper();
            returnVal = objectMapper.readValue(responseJson.toString(), ResultEnvelope.class);


            // The Jackson object mapper doesn't seem to have a means for knowing that the embedded list
            // is supposed to be cast to the DTO type. There's probably a more architectural way of doing
            // this -- e.g., a custom deserialization mechanism. But this gets the job done. Most importantly,
            // by properly casting this list of DTO objects, we prevent the Java client from caring too badly
            // about the envelope request semantics.
            JsonArray jsonArray = responseJson.get("result").getAsJsonObject().get("data").getAsJsonArray();
            String arrayAsString = jsonArray.toString();
            List<T> resultItemList = objectMapper.readValue(arrayAsString,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, DtoType));

            returnVal.getResult().setData(resultItemList);

            return returnVal;

        }

        return returnVal;
    }

}
