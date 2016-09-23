package org.gobiiproject.gobiiweb.controllers;

import org.gobiiproject.gobiimodel.dto.response.Header;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Phil on 9/23/2016.
 */
public class ControllerUtils {

    public static void setHeaderResponse(Header header,
                                         HttpServletResponse response,
                                         HttpStatus successResponseCode) {

        if (header.getStatus().isSucceeded()) {
            response.setStatus(successResponseCode.value());
        } else {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }
}
