package org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Failure {
    @JsonProperty
    String reason;
    @JsonProperty
    List<String> columnName;
    @JsonProperty
    ArrayList values;
}
