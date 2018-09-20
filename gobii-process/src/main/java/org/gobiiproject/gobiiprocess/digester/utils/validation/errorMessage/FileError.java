package org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FileError {
    @JsonProperty
    String digestFileName;
    @JsonProperty
    String status;
    @JsonProperty
    List<Failure> failures;
}
