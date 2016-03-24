package org.gobiiproject.gobiimodel.dto.header;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrPhil on 6/18/2015.
 */
public class DtoHeaderResponse implements Serializable {

    public enum StatusLevel {Error, Warning, Info, Ok}

    private boolean succeded = true;
    private ArrayList<HeaderStatusMessage> statusMessages = new ArrayList<>();


    public void addException(Exception exception) {
        succeded = false;
        addStatusMessage(StatusLevel.Error, exception.getMessage() + ": " + exception.getStackTrace());
    }


    public void addStatusMessage(StatusLevel statusLevel, String message) {
        succeded = (StatusLevel.Error != statusLevel);
        statusMessages.add(new HeaderStatusMessage(statusLevel, message));
    }


    public List<HeaderStatusMessage> getStatusMessages() {
        return (statusMessages);
    }


    public boolean getSucceeded() {
        return (succeded);
    }


}//DtoHeaderResponse
