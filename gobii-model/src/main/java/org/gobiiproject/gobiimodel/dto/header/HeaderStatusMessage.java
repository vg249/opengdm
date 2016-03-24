package org.gobiiproject.gobiimodel.dto.header;

import java.io.Serializable;

public class HeaderStatusMessage implements Serializable {

    private DtoHeaderResponse.StatusLevel statusLevel;
    private String Message;

    public HeaderStatusMessage(DtoHeaderResponse.StatusLevel statusLevel, String message) {
        this.statusLevel = statusLevel;
        Message = message;
    }

    public DtoHeaderResponse.StatusLevel getStatusLevel() {
        return statusLevel;
    }

    public void setStatusLevel(DtoHeaderResponse.StatusLevel statusLevel) {
        this.statusLevel = statusLevel;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    
    public String toString() {
        return "HeaderStatusMessage{" +
                "statusLevel=" + statusLevel +
                ", Message='" + Message + '\'' +
                "}\n";
    }//toString()
}
