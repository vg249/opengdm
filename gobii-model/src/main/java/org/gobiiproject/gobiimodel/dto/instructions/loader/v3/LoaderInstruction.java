package org.gobiiproject.gobiimodel.dto.instructions.loader.v3;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class LoaderInstruction {

    @NotNull(message = "Required Crop Type")
    private String cropType;

    @NotNull(message = "Required Load Type")
    private String loadType;

    private String datasetType;

    @NotNull(message = "Required Input file")
    private String inputFile;

    @NotNull(message = "Required Input file content type")
    private String inputFileMimeType;

    @NotNull(message = "Output direcotory not defined")
    private String outputDir;

    private String contactEmail;

    private Integer templateId;

    private String instructionType;

    private Map<String, Object> aspects;

    private Object userRequest;

}
