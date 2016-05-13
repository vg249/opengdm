// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.core;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import org.gobiiproject.gobiimodel.ConfigFileReader;
import org.gobiiproject.gobiimodel.ConfigSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;


public class RestRequest<T> {


    private final Class<T> paramType;
    private HttpCore httpCore = new HttpCore();
    Logger LOGGER = LoggerFactory.getLogger(RestRequest.class);

    @SuppressWarnings("unchecked")
    public RestRequest(Class<T> paramType) {

        this.paramType = paramType;

    } // ctor

    public String getTokenForUser(String userName, String password) throws Exception {
        return httpCore.getTokenForUser(userName, password);
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
