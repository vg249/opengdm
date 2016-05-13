// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.core;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TypedRestRequest<T> {


    private final Class<T> paramType;
    private HttpCore httpCore = new HttpCore();
    Logger LOGGER = LoggerFactory.getLogger(TypedRestRequest.class);

    @SuppressWarnings("unchecked")
    public TypedRestRequest(Class<T> paramType) {

        this.paramType = paramType;

    } // ctor

    public String getTokenForUser(String userName, String password) throws Exception {
        return httpCore.getTokenForUser(Urls.URL_AUTH,userName, password);
    }

    public T getTypedHtppResponseForDto(String url,
                                        T dtoInstance,
                                        String token) throws Exception {

        T returnVal = null;


        ObjectMapper objectMapper = new ObjectMapper();
        String dtoRequestJson = objectMapper.writeValueAsString(dtoInstance);
        JsonObject responseJson = httpCore.getResponseBody(url, dtoRequestJson, token);

        returnVal = objectMapper.readValue(responseJson.toString(), paramType);

        return returnVal;


    } // getTypedHtppResponseForDto()


    public T getTypedHtppResponse(String url,
                                  JsonObject requestJson,
                                  String token) throws Exception {

        T returnVal = null;

        JsonObject responseJson = httpCore.getResponseBody(url, requestJson.toString(), token);

        ObjectMapper objectMapper = new ObjectMapper();
        returnVal = objectMapper.readValue(responseJson.toString(), paramType);

        return returnVal;

    } // getTypedHtppResponse()

}// ArgumentDAOTest
