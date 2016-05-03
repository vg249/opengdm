package org.gobiiproject.gobiimodel.dto.header;


import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by MrPhil on 6/18/2015.
 */
public class DtoHeaderResponse implements Serializable {

    public enum StatusLevel {ERROR, VALIDATION, WARNING, INFO, OK}

    public enum ValidationStatusType {
        UNKNOWN,
        VALIDATION_COMPOUND_UNIQUE,
        VALIDATION_NOT_UNIQUE
    }

    private boolean succeeded = true;
    private ArrayList<HeaderStatusMessage> statusMessages = new ArrayList<>();

    @JsonIgnore
    public void addException(Exception exception) {
        succeeded = false;
        addStatusMessage(StatusLevel.ERROR,
                exception.getMessage() + ": " + exception.getStackTrace());
    }


    @JsonIgnore
    public void addStatusMessage(StatusLevel statusLevel, String message) {
        succeeded = (StatusLevel.ERROR != statusLevel);
        statusMessages.add(new HeaderStatusMessage(statusLevel, ValidationStatusType.UNKNOWN, message));
    }

    @JsonIgnore
    public void addStatusMessage(StatusLevel statusLevel, ValidationStatusType validationStatusType, String message) {
        succeeded = (StatusLevel.ERROR != statusLevel);
        statusMessages.add(new HeaderStatusMessage(statusLevel, validationStatusType, message));
    }

    public ArrayList<HeaderStatusMessage> getStatusMessages() {
        return statusMessages;
    }

    public void setStatusMessages(ArrayList<HeaderStatusMessage> statusMessages) {
        this.statusMessages = statusMessages;
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public void setSucceeded(boolean succeeded) {
        this.succeeded = succeeded;
    }


}//DtoHeaderResponse
