package org.gobiiproject.gobiiweb;

import org.gobiiproject.gobiimodel.types.GobiiCropType;
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
public class UrlAnalyzier {

    private static Logger LOGGER = LoggerFactory.getLogger(UrlAnalyzier.class);


    public static GobiiCropType findCurrentCropType() {

        GobiiCropType returnVal = null;

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (null != requestAttributes && requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String requestUrl = request.getRequestURI();

            List<GobiiCropType> matchedCrops =
                    Arrays.asList(GobiiCropType.values())
                            .stream()
                            .filter(c -> requestUrl.toLowerCase().contains(c.toString().toLowerCase()))
                            .collect(Collectors.toList());

            if (matchedCrops.size() >= 1) {

                returnVal = matchedCrops.get(0);
                if (matchedCrops.size() > 1) {
                    LOGGER.error("The current url ("
                            + requestUrl
                            + ") matched more than one one crop: selecting crop: "
                            + returnVal.toString());
                }

            } else {

                returnVal = GobiiCropType.RICE;
                LOGGER.error(("The current url ("
                        + requestUrl
                        + ") did not match any crops: selecting crop: "
                        + returnVal.toString()));

            }

        } else {
            returnVal = GobiiCropType.RICE;
            LOGGER.error(("Unable to retrieve crop type form url: selecting crop: "
                    + returnVal.toString()));
        }

        return returnVal;
    }
}
