package org.gobiiproject.gobiimodel.dto.header;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrPhil on 6/18/2015.
 */
public class DtoHeaderResponse implements Serializable {

    public enum StatusLevel {Error, Warning, Info, Ok}

    private boolean succeeded = true;
    private ArrayList<HeaderStatusMessage> statusMessages = new ArrayList<>();

    public void addException(Exception exception) {
        succeeded = false;
        addStatusMessage(StatusLevel.Error, exception.getMessage() + ": " + exception.getStackTrace());
    }


    public void addStatusMessage(StatusLevel statusLevel, String message) {
        succeeded = (StatusLevel.Error != statusLevel);
        statusMessages.add(new HeaderStatusMessage(statusLevel, message));
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public void setSucceeded(boolean succeeded) {
        this.succeeded = succeeded;
    }

    public ArrayList<HeaderStatusMessage> getStatusMessages() {
        return statusMessages;
    }

    public void setStatusMessages(ArrayList<HeaderStatusMessage> statusMessages) {
        this.statusMessages = statusMessages;
    }

}//DtoHeaderResponse
