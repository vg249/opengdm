package org.gobiiproject.gobiiclient.core.restmethods.get;


import org.gobiiproject.gobiiclient.core.Urls;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.dto.types.ServiceRequestId;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 9/7/2016.
 */
public class GetRequestFactory {

    private static final String DELIM_PARAM_BEGIN = "{";
    private static final String DELIM_PARAM_END = "}";
    private static final char URL_SEPARATOR = '/';

    private static String appendParam(String requestUrl, String paramName) {

        String returnVal = requestUrl;

        if (returnVal.charAt(returnVal.length() - 1) != URL_SEPARATOR) {
            returnVal += URL_SEPARATOR;
        }

        returnVal += DELIM_PARAM_BEGIN + paramName + DELIM_PARAM_END;

        return returnVal;

    }


    public static GetRequest makeGetRequestContactById() throws Exception {

        GetRequest returnVal;

        String baseUrl = Urls.getRequestUrl(ControllerType.BRAPI,
                ServiceRequestId.URL_CONTACT);
        String parameterizedUrl = appendParam(baseUrl, "contactId");
        returnVal = new GetRequest(parameterizedUrl, DELIM_PARAM_BEGIN, DELIM_PARAM_END);
        returnVal.addParam(GetParam.ParamType.PathVariable, "contactId");

        return returnVal;

    } // makeGetRequestContactById();
}
