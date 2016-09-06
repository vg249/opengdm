package org.gobiiproject.gobiimodel.config;

import org.gobiiproject.gobiimodel.dto.header.HeaderResponse;

/**
 * Created by Phil on 5/10/2016.
 */
public class GobiiException extends RuntimeException {


    private HeaderResponse.StatusLevel statusLevel;
    private HeaderResponse.ValidationStatusType validationStatusType;

    public GobiiException(String message) {
        super(message);
    }

    public GobiiException(HeaderResponse.StatusLevel statusLevel,
                          HeaderResponse.ValidationStatusType validationStatusType,
                          String message) {

        super(message);
        this.statusLevel = statusLevel;
        this.validationStatusType = validationStatusType;

    } //


    public HeaderResponse.StatusLevel getStatusLevel() {
        return statusLevel;
    }

    public void setStatusLevel(HeaderResponse.StatusLevel statusLevel) {
        this.statusLevel = statusLevel;
    }

    public HeaderResponse.ValidationStatusType getValidationStatusType() {
        return validationStatusType;
    }

    public void setValidationStatusType(HeaderResponse.ValidationStatusType validationStatusType) {
        this.validationStatusType = validationStatusType;
    }


}
