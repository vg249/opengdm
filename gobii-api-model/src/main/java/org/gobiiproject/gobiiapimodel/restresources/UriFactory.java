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

//    public  RestUri resourceByUriIdParam() throws Exception {
//
//        RestUri returnVal;
//
//        String baseUrl = ResourceBuilder.getRequestUrl(ControllerType.BRAPI,
//                ServiceRequestId.URL_CONTACTS);
//        String parameterizedUrl = appendPathVariable(baseUrl, "contactId");
//        returnVal = new RestUri(parameterizedUrl, DELIM_PARAM_BEGIN, DELIM_PARAM_END);
//        returnVal.addParam(ResourceParam.ResourceParamType.UriParam, "contactId");
//
//        return returnVal;
//
//    } // resourceByUriIdParam();


    public RestUri RestUriFromUri(String uri) {
        return new RestUri(uri,DELIM_PARAM_BEGIN, DELIM_PARAM_END);
    }


    public  RestUri contacts() throws Exception {

        return new RestUri(ResourceBuilder.getRequestUrl(ControllerType.BRAPI,
                this.cropContextRoot,
                ServiceRequestId.URL_CONTACTS),
                DELIM_PARAM_BEGIN, DELIM_PARAM_END);

    } // resourceByUriIdParam();


    public  RestUri resourceByUriIdParam(ServiceRequestId serviceRequestId) throws Exception {

        RestUri returnVal;

        String baseUrl = ResourceBuilder.getRequestUrl(ControllerType.BRAPI,
                this.cropContextRoot,
                serviceRequestId);
        String parameterizedUrl = appendPathVariable(baseUrl, "id");
        returnVal = new RestUri(parameterizedUrl, DELIM_PARAM_BEGIN, DELIM_PARAM_END);
        returnVal.addParam(ResourceParam.ResourceParamType.UriParam, "id");

        return returnVal;

    } // resourceByUriIdParam();

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

}
