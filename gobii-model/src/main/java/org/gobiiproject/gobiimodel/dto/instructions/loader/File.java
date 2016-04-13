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

    public FileType getFileType() {
        return fileType;
    }

    public File setFileType(FileType fileType) {
        this.fileType = fileType;
        return this;
    }
}
