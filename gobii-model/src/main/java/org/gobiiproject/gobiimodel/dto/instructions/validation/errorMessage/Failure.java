package org.gobiiproject.gobiimodel.dto.instructions.validation.errorMessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class Failure {
    @JsonProperty
    public String reason;
    @JsonProperty
    public List<String> columnName;
    @JsonProperty
    public List<String> values;

    public Failure() {
        this.reason = "";
        this.columnName = new ArrayList<>();
        this.values = new ArrayList<>();
    }
}
