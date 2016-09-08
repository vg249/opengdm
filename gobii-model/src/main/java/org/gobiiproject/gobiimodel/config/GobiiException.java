package org.gobiiproject.gobiimodel.config;

import org.gobiiproject.gobiimodel.dto.response.Status;

/**
 * Created by Phil on 5/10/2016.
 */
public class GobiiException extends RuntimeException {


    private Status.StatusLevel statusLevel;
    private Status.ValidationStatusType validationStatusType;

    public GobiiException(String message) {
        super(message);
    }

    public GobiiException(Status.StatusLevel statusLevel,
                          Status.ValidationStatusType validationStatusType,
                          String message) {

        super(message);
        this.statusLevel = statusLevel;
        this.validationStatusType = validationStatusType;

    } //


    public Status.StatusLevel getStatusLevel() {
        return statusLevel;
    }

    public void setStatusLevel(Status.StatusLevel statusLevel) {
        this.statusLevel = statusLevel;
    }

    public Status.ValidationStatusType getValidationStatusType() {
        return validationStatusType;
    }

    public void setValidationStatusType(Status.ValidationStatusType validationStatusType) {
        this.validationStatusType = validationStatusType;
    }


}
