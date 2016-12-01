package org.gobiiproject.gobiiapimodel.restresources;


import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Phil on 9/7/2016.
 */
public class UriFactory {

    private final String DELIM_PARAM_BEGIN = "{";
    private final String DELIM_PARAM_END = "}";
    private final char URL_SEPARATOR = '/';

    private String cropContextRoot;

    public UriFactory(String cropContextRoot) {

        this.cropContextRoot = cropContextRoot;
        if (null != this.cropContextRoot) {
            if (this.cropContextRoot.lastIndexOf(URL_SEPARATOR) != this.cropContextRoot.length() - 1) {
                this.cropContextRoot = this.cropContextRoot + URL_SEPARATOR;
            }
        }
    }


    private String appendPathVariable(String requestUrl, String paramName) {

        String returnVal = requestUrl;

        if ((0 == returnVal.length()) ||
                (returnVal.charAt(returnVal.length() - 1) != URL_SEPARATOR)) {

            returnVal += URL_SEPARATOR;
        }

        returnVal += DELIM_PARAM_BEGIN + paramName + DELIM_PARAM_END;

        return returnVal;

    }

    private RestUri makeUriWithUriParams(String baseUri, List<String> uriParms) {

        String uriParamSuffix = "";
        for (String currentParam : uriParms) {
            uriParamSuffix = this.appendPathVariable(uriParamSuffix, currentParam);
        }

        String uri = baseUri + uriParamSuffix;

        RestUri returnVal = new RestUri(uri, DELIM_PARAM_BEGIN, DELIM_PARAM_END);
        for (String currentParam : uriParms) {
            returnVal.addParam(ResourceParam.ResourceParamType.UriParam, currentParam);
        }

        return returnVal;

    }

    public RestUri RestUriFromUri(String uri) {
        return new RestUri(uri, DELIM_PARAM_BEGIN, DELIM_PARAM_END);
    }


    public RestUri createLoaderDirectory() throws Exception {

        RestUri returnVal;

        String baseUrl = ResourceBuilder.getRequestUrl(ControllerType.BRAPI,
                this.cropContextRoot,
                ServiceRequestId.URL_FILE_LOAD);

        returnVal = this.makeUriWithUriParams(baseUrl, Arrays.asList("directoryName"));

        return returnVal;

    } //

    public RestUri resourceColl(ServiceRequestId serviceRequestId) throws Exception {

        RestUri returnVal;

        String baseUrl = ResourceBuilder.getRequestUrl(ControllerType.BRAPI,
                this.cropContextRoot,
                serviceRequestId);

        returnVal = new RestUri(baseUrl, DELIM_PARAM_BEGIN, DELIM_PARAM_END);

        return returnVal;

    } //


    public RestUri resourceByUriIdParam(ServiceRequestId serviceRequestId) throws Exception {

        RestUri returnVal;

        String baseUrl = ResourceBuilder.getRequestUrl(ControllerType.BRAPI,
                this.cropContextRoot,
                serviceRequestId);
        returnVal = this.makeUriWithUriParams(baseUrl, Arrays.asList("id"));

        return returnVal;

    } //

    public RestUri contactsByQueryParams() throws Exception {

        RestUri returnVal;

        String baseUrl = ResourceBuilder.getRequestUrl(ControllerType.BRAPI,
                this.cropContextRoot,
                ServiceRequestId.URL_CONTACT_SEARCH);
        returnVal = new RestUri(baseUrl, DELIM_PARAM_BEGIN, DELIM_PARAM_END);
        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "email");
        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "lastName");
        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "firstName");

        return returnVal;

    } //

    public RestUri markerssByQueryParams() throws Exception {

        RestUri returnVal;

        String baseUrl = ResourceBuilder.getRequestUrl(ControllerType.BRAPI,
                this.cropContextRoot,
                ServiceRequestId.URL_MARKER_SEARCH);
        returnVal = new RestUri(baseUrl, DELIM_PARAM_BEGIN, DELIM_PARAM_END);
        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "name");
        return returnVal;

    }

    public RestUri nameIdListByQueryParams() throws Exception {

        RestUri returnVal;

        String baseUrl = ResourceBuilder.getRequestUrl(ControllerType.BRAPI,
                this.cropContextRoot,
                ServiceRequestId.URL_NAMES);


        returnVal = this.makeUriWithUriParams(baseUrl, Arrays.asList("entity"));

        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "filterType");
        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "filterValue");

        return returnVal;

    } //

    public RestUri fileLoaderPreview() throws Exception {


        RestUri returnVal;

        String baseUrl = ResourceBuilder.getRequestUrl(ControllerType.BRAPI,
                this.cropContextRoot,
                ServiceRequestId.URL_FILE_LOAD);

        returnVal = this.makeUriWithUriParams(baseUrl, Arrays.asList("directoryName"));
        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "fileFormat");

        return returnVal;

    } //
}
