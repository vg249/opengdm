package org.gobiiproject.gobiiapimodel.restresources;


import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Phil on 9/7/2016.
 */
public class UriFactory {

    private ControllerType controllerType;
    private String cropContextRoot;


    public UriFactory(String cropContextRoot, ControllerType controllerType) {
        this.controllerType = controllerType;
        this.cropContextRoot = cropContextRoot;
    }

    public UriFactory(String cropContextRoot) {

        this.controllerType = ControllerType.GOBII;
        this.cropContextRoot = cropContextRoot;
    }


    private void makeUriWithUriParams(RestUri restUri, List<String> uriParms) {

        for (String currentParam : uriParms) {
            restUri.appendPathVariable(currentParam);
        }

        for (String currentParam : uriParms) {
            restUri.addParam(ResourceParam.ResourceParamType.UriParam, currentParam);
        }

    }

    public RestUri RestUriFromUri(String uri) {
        return new RestUri(uri);
    }


    public RestUri resourceColl(ServiceRequestId serviceRequestId) throws Exception {

        RestUri returnVal;

        returnVal = new RestUri(this.cropContextRoot,
                this.controllerType,
                serviceRequestId);

        return returnVal;

    } //


    public RestUri resourceByUriIdParam(ServiceRequestId serviceRequestId) throws Exception {

        String paramName = "id";
        return new RestUri(this.cropContextRoot,
                this.controllerType,
                serviceRequestId)
                .appendPathVariable(paramName)
                .addParam(ResourceParam.ResourceParamType.UriParam, paramName);
    } //

    public RestUri childResourceByUriIdParam(ServiceRequestId parentServiceRequestId, ServiceRequestId childServiceRequestId) throws Exception {

        String paramName = "id";
        RestUri returnVal = new RestUri(this.cropContextRoot,
                this.controllerType,
                parentServiceRequestId)
                .appendPathVariable(paramName)
                .addParam(ResourceParam.ResourceParamType.UriParam, paramName)
                .appendSegment(childServiceRequestId);


//        this.makeUriWithUriParams(returnVal, Arrays.asList("id"));
//
//        returnVal.appendSegment(RestUri.URL_SEPARATOR + ResourceBuilder.getUrlSegment(childServiceRequestId));

        return returnVal;

    } //

    public RestUri contactsByQueryParams() throws Exception {

        RestUri returnVal = new RestUri(this.cropContextRoot,
                this.controllerType,
                ServiceRequestId.URL_CONTACT_SEARCH);
        ;

        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "email");
        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "lastName");
        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "firstName");

        return returnVal;

    } //

    public RestUri markerssByQueryParams() throws Exception {

        RestUri returnVal = new RestUri(this.cropContextRoot,
                this.controllerType,
                ServiceRequestId.URL_MARKER_SEARCH);

        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "name");
        return returnVal;

    }

    public RestUri nameIdListByQueryParams() throws Exception {

        RestUri returnVal = new RestUri(this.cropContextRoot,
                this.controllerType,
                ServiceRequestId.URL_NAMES);


        this.makeUriWithUriParams(returnVal, Arrays.asList("entity"));

        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "filterType");
        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "filterValue");

        return returnVal;

    } //

    public RestUri fileLoaderPreview() throws Exception {


        RestUri returnVal = new RestUri(this.cropContextRoot,
                this.controllerType,
                ServiceRequestId.URL_FILE_LOAD);

        this.makeUriWithUriParams(returnVal, Arrays.asList("directoryName"));
        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "fileFormat");

        return returnVal;

    } //
}


