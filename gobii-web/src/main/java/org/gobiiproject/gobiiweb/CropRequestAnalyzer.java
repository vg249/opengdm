package org.gobiiproject.gobiiweb;

import org.gobiiproject.gobiimodel.config.ConfigSettings;

import org.gobiiproject.gobiimodel.types.GobiiHttpHeaderNames;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Phil on 5/25/2016.
 */
public class CropRequestAnalyzer {

    private static Logger LOGGER = LoggerFactory.getLogger(CropRequestAnalyzer.class);
    private static ConfigSettings CONFIG_SETTINGS = new ConfigSettings();


    private static HttpServletRequest getRequest() {

        HttpServletRequest returnVal = null;

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (null != requestAttributes && requestAttributes instanceof ServletRequestAttributes) {
            returnVal = ((ServletRequestAttributes) requestAttributes).getRequest();
        }

        return returnVal;
    }


    private static String getCropTypeFromHeaders(HttpServletRequest httpRequest) throws Exception {

        String returnVal = null;

        String errorMessage = null;

        if (null != httpRequest) {

            returnVal = httpRequest.getHeader(GobiiHttpHeaderNames.HEADER_GOBII_CROP);

            if (LineUtils.isNullOrEmpty(returnVal)) {

                // this is not an exception because if we didn't get the crop ID from
                // the header we'll infer it from uri
                LOGGER.error("Request did not include the response "
                        + GobiiHttpHeaderNames.HEADER_GOBII_CROP);
            }

        } else {
            throw new Exception("Unable to retreive servlet request for crop type analysis from response");

        }

        return returnVal;

    }

    private static String getCropTypeFromUri(HttpServletRequest httpRequest) throws Exception {

        String returnVal = null;

        String errorMessage = null;

        if (null != httpRequest) {
            String requestUrl = httpRequest.getRequestURI();

            List<String> matchedCrops =
                    CONFIG_SETTINGS
                            .getActiveCropTypes()
                            .stream()
                            .filter(c -> requestUrl.toLowerCase().contains(c.toString().toLowerCase()))
                            .collect(Collectors.toList());

            if (matchedCrops.size() > 0) {

                if (1 == matchedCrops.size()) {
                    returnVal = matchedCrops.get(0);
                } else {
                    errorMessage = "The current url ("
                            + requestUrl
                            + ") matched more than one one crop; the service app root must contain only one crop ID";
                }

            } else {

                errorMessage = "The current url ("
                        + requestUrl
                        + ") did not match any crops; ; service app root must contain one, and only one, crop ID";
            }

        } else {
            errorMessage = "Unable to retreive servlet request for crop type analysis";
        }

        if (errorMessage != null) {
            LOGGER.error(errorMessage);
            throw new Exception(errorMessage);
        }

        return returnVal;
    }

    public static String getGobiiCropType() throws Exception {

        String returnVal = null;

        HttpServletRequest httpRequest = null;

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (null != requestAttributes && requestAttributes instanceof ServletRequestAttributes) {
            httpRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        }

        if (httpRequest != null) {
            returnVal = CropRequestAnalyzer.getGobiiCropType(httpRequest);
        } else {
            // this will be the case when the server is initializing
            // we used to have a "default" crop for this purpose but that approach
            // led to some really bad bugs.
            if( CONFIG_SETTINGS.getActiveCropTypes().size() <= 0 ) {
                String message = "Unable to initialize: there are no active crop types in the configuration";
                LOGGER.error(message);
                throw new Exception(message);
            }

            // so we just randomly use the first active crop in the list
            // again, this should only occur upon initialization, when ther eis no
            // http context; when there is, we will get the cropId from the url
            // (GobiiConfig's validator enforces that all cropIds must be contained
            // within all server context paths)
            returnVal = CONFIG_SETTINGS.getActiveCropTypes().get(0);
        }

        return returnVal;

    }


    public static String getGobiiCropType(HttpServletRequest httpRequest) throws Exception {

        String returnVal = CropRequestAnalyzer.getCropTypeFromHeaders(httpRequest);

        if (null == returnVal) {

            returnVal = CropRequestAnalyzer.getCropTypeFromUri(httpRequest);

            if (null == returnVal) {
                String message = "Unable to determine crop type from response or uri";
                LOGGER.error(message);
                throw new Exception(message);
            }
        }

        return returnVal;
    }
}
