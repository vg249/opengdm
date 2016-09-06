package org.gobiiproject.gobiimodel.dto.header;

import java.io.Serializable;

public class HeaderStatusMessage implements Serializable {


    private HeaderResponse.StatusLevel statusLevel;


    private HeaderResponse.ValidationStatusType validationStatusType;
    private String message;

    public HeaderStatusMessage() {
    }

    public HeaderStatusMessage(HeaderResponse.StatusLevel statusLevel,
                               HeaderResponse.ValidationStatusType validationStatusType,
                               String message) {
        this.statusLevel = statusLevel;
        this.validationStatusType = validationStatusType;
        this.message = message;
    }

    public HeaderResponse.StatusLevel getStatusLevel() {
        return statusLevel;
    }

    public HeaderResponse.ValidationStatusType getValidationStatusType() {
        return validationStatusType;
    }

    public void setStatusLevel(HeaderResponse.StatusLevel statusLevel) {
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
