package org.gobiiproject.gobiimodel.dto.instructions.loader;

import org.gobiiproject.gobiimodel.types.GobiiFileType;

/**
 * Created by Phil on 4/12/2016.
 */
public class File {



    String source = null;
    String destination = null;
    String delimiter = null;
    GobiiFileType gobiiFileType = null;

    public String getSource() {
        return source;
    }

    public File setSource(String source) {
        this.source = source;
        return this;
    }

    public String getDestination() {
        return destination;
    }

    public File setDestination(String destination) {
        this.destination = destination;
        return this;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public File setDelimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public GobiiFileType getGobiiFileType() {
        return gobiiFileType;
    }

    public File setGobiiFileType(GobiiFileType gobiiFileType) {
        this.gobiiFileType = gobiiFileType;
        return this;
    }
}
