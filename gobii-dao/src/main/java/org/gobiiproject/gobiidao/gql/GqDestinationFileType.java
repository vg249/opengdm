package org.gobiiproject.gobiidao.gql;

/**
 * Created by Phil on 5/13/2016.
 * These are intended to codify cv term names. You cannot add new values unless there exists a corresponding cv term in the database
 *
 */


public enum GqDestinationFileType {

    DST_VALUES("values"),
    DST_COUNT_MARKER("markers"),
    DST_COUNT_SAMPLE("samples");

    private String destination;

    GqDestinationFileType(String destination) {
        this.destination = destination;
    }

    public String getDestination() {
        return this.destination;
    }

    public static GqDestinationFileType byValue(String val) {

        GqDestinationFileType returnVal = null;

        for (GqDestinationFileType currentVertexNameType : values()) {
            if (currentVertexNameType.getDestination().equals(val)) {
                returnVal =  currentVertexNameType;
                break;
            }
        }

        return returnVal;

    } // end byValue()

} // end enum
