package org.gobiiproject.gobiimodel.dto.instructions.validation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.gobiiproject.gobiimodel.dto.instructions.validation.errorMessage.Failure;

@Data
public class ValidationResult {
    @JsonProperty
    public String fileName;
    @JsonProperty
    public String status;
    @JsonProperty
    public List<Failure> failures;

    public ValidationResult() {
        fileName = "";
        status = "";
        failures = new ArrayList<>();
    }
}
