package org.gobiiproject.gobiimodel.dto.instructions.loader.v3;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiAspectTable;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
@GobiiAspectTable(name = "marker_group")
@Data
public class MarkerGroupTable implements Table {

    @NotNull(message = "platform id cannot be null")
    private String platformId;

    @NotNull(message = "marker name cannot be null")
    private Aspect markerName;

    private Aspect platformName;

    @NotNull(message = "marker group name cannot be null")
    private Aspect markerGroupName;

    private Aspect germplasmGroup;

    private Aspect favorableAlleles;

}
