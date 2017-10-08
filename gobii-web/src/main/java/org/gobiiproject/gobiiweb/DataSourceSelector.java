package org.gobiiproject.gobiiweb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Phil on 5/25/2016.
 */
public class DataSourceSelector extends AbstractRoutingDataSource {

    Logger LOGGER = LoggerFactory.getLogger(DataSourceSelector.class);

    /*
    For reasons that are not clear, for the very first request that comes in,
    the ServletRequest is not available through the ServletRequestAttributes.
    In this scenario, the CroRequestAnalyzer was defaulting to provide the
    ID of the first active gobii server instance listed in the config file.
    For single crop configurations this didn't matter. But when more than
    one crop was configured, it meant that for the authentication request --
    and only for that request before the request context was establsihed --
    the user credentials were being retrieved from the wrong database. Accordingly
    I set up a oncePerRequest filter that sets the servlet request in a thread local.
    I will need to research whether this is really a correct long term solution.
    But it seems to work fine.
     */
    private ThreadLocal<HttpServletRequest> currentRequest;
    public ThreadLocal<HttpServletRequest> getCurrentRequest() {
        return currentRequest;
    }

    public void setCurrentRequest(ThreadLocal<HttpServletRequest> currentRequest) {
        this.currentRequest = currentRequest;
    }
    @Override
    protected Object determineCurrentLookupKey() {

        Object returnVal = null;

        try {
            if( currentRequest.get() != null ) {
                returnVal = CropRequestAnalyzer.getGobiiCropType(currentRequest.get());
            } else {
                returnVal = CropRequestAnalyzer.getGobiiCropType();
            }

        } catch( Exception e) {

            LOGGER.error("Error looking up lookup key",e);
        }

        return returnVal;
    }


}
