package org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ValidationError {
    @JsonProperty
    public String digestFileName;
    @JsonProperty
    public String status;
    @JsonProperty
    public List<Failure> failures;

    public ValidationError() {
        digestFileName = "";
        status = "";
        failures = new ArrayList<>();
    }
}
