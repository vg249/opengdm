package org.gobiiproject.gobiimodel.dto.response;

import java.io.Serializable;

public class HeaderStatusMessage implements Serializable {


    private Status.StatusLevel statusLevel;


    private Status.ValidationStatusType validationStatusType;
    private String message;

    public HeaderStatusMessage() {
    }

    public HeaderStatusMessage(Status.StatusLevel statusLevel,
                               Status.ValidationStatusType validationStatusType,
                               String message) {
        this.statusLevel = statusLevel;
        this.validationStatusType = validationStatusType;
        this.message = message;
    }

    public Status.StatusLevel getStatusLevel() {
        return statusLevel;
    }

    public Status.ValidationStatusType getValidationStatusType() {
        return validationStatusType;
    }

    public void setStatusLevel(Status.StatusLevel statusLevel) {
        this.statusLevel = statusLevel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String toString() {
        return "HeaderStatusMessage{" +
                "statusLevel=" + statusLevel +
                ", Message='" + message + '\'' +
                "}\n";
    }//toString()
}
