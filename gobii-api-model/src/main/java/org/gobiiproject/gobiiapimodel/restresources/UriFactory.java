package org.gobiiproject.gobiiapimodel.restresources;


import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Phil on 9/7/2016.
 */
public class UriFactory {

    private final char URL_SEPARATOR = '/';

    private ControllerType controllerType;
    private String cropContextRoot;


    private void delimitCropContextRoot() {

        if (null != this.cropContextRoot) {
            if (this.cropContextRoot.lastIndexOf(URL_SEPARATOR) != this.cropContextRoot.length() - 1) {
                this.cropContextRoot = this.cropContextRoot + URL_SEPARATOR;
            }
        }
    }

    public UriFactory(String cropContextRoot, ControllerType controllerType) {
        this.controllerType = controllerType;
        this.cropContextRoot = cropContextRoot;
        this.delimitCropContextRoot();
    }

    public UriFactory(String cropContextRoot) {

        this.controllerType = ControllerType.GOBII;
        this.cropContextRoot = cropContextRoot;
        this.delimitCropContextRoot();
    }



    private RestUri makeUriWithUriParams(String baseUri, List<String> uriParms) {

        RestUri returnVal = new RestUri(baseUri);


        for (String currentParam : uriParms) {
            returnVal.appendPathVariable(currentParam);
        }

        for (String currentParam : uriParms) {
            returnVal.addParam(ResourceParam.ResourceParamType.UriParam, currentParam);
        }

        return returnVal;

    }

    public RestUri RestUriFromUri(String uri) {
        return new RestUri(uri);
    }


    public RestUri resourceColl(ServiceRequestId serviceRequestId) throws Exception {

        RestUri returnVal;

        String baseUrl = ResourceBuilder.getRequestUrl(this.controllerType,
                this.cropContextRoot,
                serviceRequestId);

        returnVal = new RestUri(baseUrl);

        return returnVal;

    } //


    public RestUri resourceByUriIdParam(ServiceRequestId serviceRequestId) throws Exception {

        RestUri returnVal;

        String baseUrl = ResourceBuilder.getRequestUrl(this.controllerType,
                this.cropContextRoot,
                serviceRequestId);
        returnVal = this.makeUriWithUriParams(baseUrl, Arrays.asList("id"));

        return returnVal;

    } //

    public RestUri childResourceByUriIdParam(ServiceRequestId parentServiceRequestId, ServiceRequestId childServiceRequestId) throws Exception {

        RestUri returnVal;

        String baseUrl = ResourceBuilder.getRequestUrl(this.controllerType,
                this.cropContextRoot,
                parentServiceRequestId);
        returnVal = this.makeUriWithUriParams(baseUrl, Arrays.asList("id"));

        returnVal.appendSegment(this.URL_SEPARATOR + ResourceBuilder.getUrlSegment(childServiceRequestId));

        return returnVal;

    } //

    public RestUri contactsByQueryParams() throws Exception {

        RestUri returnVal;

        String baseUrl = ResourceBuilder.getRequestUrl(this.controllerType,
                this.cropContextRoot,
                ServiceRequestId.URL_CONTACT_SEARCH);
        returnVal = new RestUri(baseUrl);
        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "email");
        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "lastName");
        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "firstName");

        return returnVal;

    } //

    public RestUri markerssByQueryParams() throws Exception {

        RestUri returnVal;

        String baseUrl = ResourceBuilder.getRequestUrl(this.controllerType,
                this.cropContextRoot,
                ServiceRequestId.URL_MARKER_SEARCH);
        returnVal = new RestUri(baseUrl);
        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "name");
        return returnVal;

    }

    public RestUri nameIdListByQueryParams() throws Exception {

        RestUri returnVal;

        String baseUrl = ResourceBuilder.getRequestUrl(this.controllerType,
                this.cropContextRoot,
                ServiceRequestId.URL_NAMES);


        returnVal = this.makeUriWithUriParams(baseUrl, Arrays.asList("entity"));

        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "filterType");
        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "filterValue");

        return returnVal;

    } //

    public RestUri fileLoaderPreview() throws Exception {


        RestUri returnVal;

        String baseUrl = ResourceBuilder.getRequestUrl(this.controllerType,
                this.cropContextRoot,
                ServiceRequestId.URL_FILE_LOAD);

        returnVal = this.makeUriWithUriParams(baseUrl, Arrays.asList("directoryName"));
        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "fileFormat");

        return returnVal;

    } //
}


