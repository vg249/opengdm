package org.gobiiproject.gobiimodel.dto.instructions.loader;

/**
 * Created by Phil on 4/12/2016.
 */
public class File {

    public enum FileType {HAPMAP, GENERIC, VCF}

    String source = null;
    String destination = null;
    String delimiter = null;
    FileType fileType = null;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }
}
