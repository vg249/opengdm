package org.gobiiproject.gobiiclient.core.restmethods;


import org.gobiiproject.gobiiclient.core.ResourceBuilder;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.dto.types.ServiceRequestId;

/**
 * Created by Phil on 9/7/2016.
 */
public class UriFactory {

    private static final String DELIM_PARAM_BEGIN = "{";
    private static final String DELIM_PARAM_END = "}";
    private static final char URL_SEPARATOR = '/';

    private static String appendPathVariable(String requestUrl, String paramName) {

        String returnVal = requestUrl;

        if (returnVal.charAt(returnVal.length() - 1) != URL_SEPARATOR) {
            returnVal += URL_SEPARATOR;
        }

        returnVal += DELIM_PARAM_BEGIN + paramName + DELIM_PARAM_END;

        return returnVal;

    }

//    public static RestUri contactByContactUriParam() throws Exception {
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
//    } // contactByContactUriParam();



    public static RestUri contacts() throws Exception {

        return new RestUri(ResourceBuilder.getRequestUrl(ControllerType.BRAPI,
                ServiceRequestId.URL_CONTACTS),
                DELIM_PARAM_BEGIN, DELIM_PARAM_END);

    } // contactByContactUriParam();


    public static RestUri contactByContactUriParam() throws Exception {

        RestUri returnVal;

        String baseUrl = ResourceBuilder.getRequestUrl(ControllerType.BRAPI,
                ServiceRequestId.URL_CONTACTS);
        String parameterizedUrl = appendPathVariable(baseUrl, "contactId");
        returnVal = new RestUri(parameterizedUrl, DELIM_PARAM_BEGIN, DELIM_PARAM_END);
        returnVal.addParam(ResourceParam.ResourceParamType.UriParam, "contactId");

        return returnVal;

    } // contactByContactUriParam();

    public static RestUri contactsByQueryParams() throws Exception {

        RestUri returnVal;

        String baseUrl = ResourceBuilder.getRequestUrl(ControllerType.BRAPI,
                ServiceRequestId.URL_CONTACT_SEARCH);
        returnVal = new RestUri(baseUrl, DELIM_PARAM_BEGIN, DELIM_PARAM_END);
        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "email");
        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "lastName");
        returnVal.addParam(ResourceParam.ResourceParamType.QueryParam, "firstName");

        return returnVal;

    } // contactByContactUriParam();

}
