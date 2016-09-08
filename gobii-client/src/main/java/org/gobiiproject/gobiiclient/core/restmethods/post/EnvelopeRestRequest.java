// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.core.restmethods.post;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.HttpCore;
import org.gobiiproject.gobiimodel.dto.response.RequestEnvelope;
import org.gobiiproject.gobiimodel.dto.response.ResultEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class EnvelopeRestRequest<T> {


    private final Class<T> paramType;
    private HttpCore httpCore = null;
    Logger LOGGER = LoggerFactory.getLogger(EnvelopeRestRequest.class);

    public EnvelopeRestRequest(String baseUrl,
                               Integer port,
                               Class<T> paramType) {

        this.paramType = paramType;

        httpCore = new HttpCore(baseUrl, port);

    } // ctor



    public ResultEnvelope<T> getTypedHtppResponseForDtoEnvelope(String url,
                                                                RequestEnvelope<T> requestEnvelope,
                                                                String token) throws Exception {
        ResultEnvelope<T> returnVal = null;




        if (ClientContext.isInitialized()) {
            String gobiiCropType = ClientContext.getInstance(null, false).getCurrentClientCropType();
        }


        ObjectMapper objectMapper = new ObjectMapper();
        String dtoRequestJson = objectMapper.writeValueAsString(requestEnvelope);
        JsonObject responseJson = httpCore.getResponseBody(url, dtoRequestJson, token);

        returnVal = objectMapper.readValue(responseJson.toString(), ResultEnvelope.class);


        // The Jackson object mapper doesn't seem to have a means for knowing that the embedded list
        // is supposed to be cast to the DTO type. There's probably a more architectural way of doing
        // this -- e.g., a custom deserialization mechanism. But this gets the job done. Most importantly,
        // by properly casting this list of DTO objects, we prevent the Java client from caring too badly
        // about the envelope request semantics.
        JsonArray jsonArray = responseJson.get("result").getAsJsonObject().get("data").getAsJsonArray();
        String arrayAsString = jsonArray.toString();
        List<T> resultItemList = objectMapper.readValue(arrayAsString ,
                objectMapper.getTypeFactory().constructCollectionType(List.class, paramType));
        returnVal.getResult().setData(resultItemList);

        return returnVal;


    } // getTypedHtppResponseForDto()


}// ArgumentDAOTest
