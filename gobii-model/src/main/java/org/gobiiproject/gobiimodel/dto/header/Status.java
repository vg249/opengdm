package org.gobiiproject.gobiimodel.dto.header;


import org.codehaus.jackson.annotate.JsonIgnore;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.utils.ExceptionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MrPhil on 6/18/2015.
 */
public class Status implements Serializable {

    public enum StatusLevel {ERROR, VALIDATION, WARNING, INFO, OK}

    public enum ValidationStatusType {
        NONE,
        UNKNOWN,
        VALIDATION_COMPOUND_UNIQUE,
        VALIDATION_NOT_UNIQUE,
        NONEXISTENT_FK_ENTITY,
        BAD_REQUEST,
        MISSING_REQUIRED_VALUE,
        ENTITY_DOES_NOT_EXIST
    }

    private boolean succeeded = true;
    private ArrayList<HeaderStatusMessage> statusMessages = new ArrayList<>();

    private Map<String,String> statusMessagesByCode = new HashMap<>();

    @JsonIgnore
    public void addException(Exception e) {
        succeeded = false;
        String message = ExceptionUtils.makeMessageFromException(e);
        addStatusMessage(StatusLevel.ERROR,
                message);
    }


    @JsonIgnore
    public void addException(GobiiException gobiiException) {
        succeeded = false;

        String message = ExceptionUtils.makeMessageFromException(gobiiException);
        addStatusMessage(gobiiException.getStatusLevel(),
                gobiiException.getValidationStatusType(),
                message);
    }


    @JsonIgnore
    public void addStatusMessage(StatusLevel statusLevel, String message) {
        this.addStatusMessage(statusLevel, ValidationStatusType.NONE, message);
    }

    @JsonIgnore
    public void addStatusMessage(StatusLevel statusLevel, ValidationStatusType validationStatusType, String message) {

        succeeded = (StatusLevel.ERROR != statusLevel);
        statusMessages.add(new HeaderStatusMessage(statusLevel, validationStatusType, message));

        // BRAPI requires messages by "code"
        // Java maps don't have duplicate keys, so we need to make sure our key is unique
        Integer keyIndex = 0;
        boolean addedMessage = false;
        do {
            String codeKey = keyIndex + "-" + statusLevel.toString() + "-" + validationStatusType.toString();
            if(false == statusMessagesByCode.containsKey(codeKey)) {

                statusMessagesByCode.put(codeKey,message);
                addedMessage = true;
            } else {
                keyIndex++;
            }

        } while( false == addedMessage);

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

    public Map<String, String> getStatusMessagesByCode() {
        return statusMessagesByCode;
    }
}//Status
