package org.gobiiproject.gobiiapimodel.restresources;


import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;

/**
 * Created by Phil on 9/7/2016.
 */
public class UriFactory {

    private  final String DELIM_PARAM_BEGIN = "{";
    private  final String DELIM_PARAM_END = "}";
    private  final char URL_SEPARATOR = '/';

    private String cropContextRoot;
    public UriFactory(String cropContextRoot) {

        this.cropContextRoot = cropContextRoot;
        if( null != this.cropContextRoot ) {
            if (this.cropContextRoot.lastIndexOf(URL_SEPARATOR) != this.cropContextRoot.length() - 1) {
                this.cropContextRoot = this.cropContextRoot + URL_SEPARATOR;
            }
        }
    }



    private  String appendPathVariable(String requestUrl, String paramName) {

        String returnVal = requestUrl;

        if (returnVal.charAt(returnVal.length() - 1) != URL_SEPARATOR) {
            returnVal += URL_SEPARATOR;
        }

        returnVal += DELIM_PARAM_BEGIN + paramName + DELIM_PARAM_END;

        return returnVal;

    }

    public RestUri RestUriFromUri(String uri) {
        return new RestUri(uri,DELIM_PARAM_BEGIN, DELIM_PARAM_END);
    }


    public  RestUri contacts() throws Exception {

        return new RestUri(ResourceBuilder.getRequestUrl(ControllerType.BRAPI,
                this.cropContextRoot,
                ServiceRequestId.URL_CONTACTS),
                DELIM_PARAM_BEGIN, DELIM_PARAM_END);

    } // resourceByUriIdParam();

    public  RestUri loaderInstructionFiles() throws Exception {

        return new RestUri(ResourceBuilder.getRequestUrl(ControllerType.BRAPI,
                this.cropContextRoot,
                ServiceRequestId.URL_FILE_LOAD_INSTRUCTIONS),
                DELIM_PARAM_BEGIN, DELIM_PARAM_END);

    } // resourceByUriIdParam();

    public  RestUri organization() throws Exception {

        return new RestUri(ResourceBuilder.getRequestUrl(ControllerType.BRAPI,
                this.cropContextRoot,
                ServiceRequestId.URL_ORGANIZATION),
                DELIM_PARAM_BEGIN, DELIM_PARAM_END);

    } // resourceByUriIdParam();

    public RestUri resourceColl(ServiceRequestId serviceRequestId) throws Exception {

        RestUri returnVal;

        String baseUrl = ResourceBuilder.getRequestUrl(ControllerType.BRAPI,
                this.cropContextRoot,
                serviceRequestId);

        returnVal = new RestUri(baseUrl, DELIM_PARAM_BEGIN, DELIM_PARAM_END);

        return returnVal;

    } // resourceByUriIdParam();



    public RestUri resourceByUriIdParam(ServiceRequestId serviceRequestId) throws Exception {

        RestUri returnVal;

        String baseUrl = ResourceBuilder.getRequestUrl(ControllerType.BRAPI,
                this.cropContextRoot,
                serviceRequestId);
        String parameterizedUrl = appendPathVariable(baseUrl, "id");
        returnVal = new RestUri(parameterizedUrl, DELIM_PARAM_BEGIN, DELIM_PARAM_END);
        returnVal.addParam(ResourceParam.ResourceParamType.UriParam, "id");

        return returnVal;

    } //

    public  RestUri contactsByQueryParams() throws Exception {

        RestUri returnVal;

        String baseUrl = ResourceBuilder.getRequestUrl(ControllerType.BRAPI,
                this.cropContextRoot,
                ServiceRequestId.URL_CONTACT_SEARCH);
        returnVal = new RestUri(baseUrl, DELIM_PARAM_BEGIN, DELIM_PARAM_END);
        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "email");
        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "lastName");
        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "firstName");

        return returnVal;

    } // resourceByUriIdParam();

    public  RestUri organizationsByQueryParams() throws Exception {

        RestUri returnVal;

        String baseUrl = ResourceBuilder.getRequestUrl(ControllerType.BRAPI,
                this.cropContextRoot,
                ServiceRequestId.URL_ORGANIZATION_SEARCH);
        returnVal = new RestUri(baseUrl, DELIM_PARAM_BEGIN, DELIM_PARAM_END);
        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "name");

        return returnVal;

    } // resourceByUriIdParam();

    public RestUri instructionsByInstructionFileName() throws Exception {

        RestUri returnVal;

        String baseUrl = ResourceBuilder.getRequestUrl(ControllerType.BRAPI,
                this.cropContextRoot,
                ServiceRequestId.URL_FILE_LOAD_INSTRUCTIONS);
        String parameterizedUrl = appendPathVariable(baseUrl, "instructionFileName");
        returnVal = new RestUri(parameterizedUrl, DELIM_PARAM_BEGIN, DELIM_PARAM_END);
        returnVal.addParam(ResourceParam.ResourceParamType.UriParam, "instructionFileName");

        return returnVal;

    } //

    public RestUri nameIdList() throws Exception {

        RestUri returnVal;

        String baseUrl = ResourceBuilder.getRequestUrl(ControllerType.BRAPI,
                this.cropContextRoot,
                ServiceRequestId.URL_NAMES);

        String parameterizedUrl = appendPathVariable(baseUrl, "entity");

        returnVal = new RestUri(parameterizedUrl, DELIM_PARAM_BEGIN, DELIM_PARAM_END);

        returnVal.addParam(ResourceParam.ResourceParamType.UriParam, "entity");

        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "filterType");
        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "filterValue");

        return returnVal;

    } //

}
