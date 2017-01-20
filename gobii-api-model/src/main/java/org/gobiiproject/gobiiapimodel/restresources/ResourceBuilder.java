/// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiapimodel.restresources;

import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;

public class ResourceBuilder {

    public static String getRequestUrl(ControllerType controllerType,
                                       String cropContextRoot,
                                       ServiceRequestId requestId) throws Exception {

        return ResourceBuilder.getRequestUrl(controllerType,
                requestId,
                cropContextRoot);
    }


    public static String getRequestUrl(ControllerType controllerType,
                                       ServiceRequestId requestId,
                                       String contextRoot) throws Exception {

        String controllerPath = controllerType.getControllerPath();

        String returnVal = contextRoot + controllerPath;

        returnVal += requestId.getRequestPath();

        return returnVal;
    }

} // ResourceBuilder
