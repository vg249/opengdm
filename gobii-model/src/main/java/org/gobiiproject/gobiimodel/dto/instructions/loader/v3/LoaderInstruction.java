package org.gobiiproject.gobiimodel.dto.instructions.loader.v3;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoaderInstruction {

    @NotNull(message = "Required Crop Type")
    private String cropType;

    @NotNull(message = "Required Load Type")
    private String loadType;

    private String datasetType;

    @NotNull(message = "Required Input file")
    private String inputFile;

    @NotNull(message = "Output direcotory not defined")
    private String outputDir;

    private String contactEmail;

    private Map<String, Object> aspects;

    private Object matrix;

    public String getCropType() {
        return cropType;
    }

    public void setCropType(String cropType) {
        this.cropType = cropType;
    }

    public String getLoadType() {
        return loadType;
    }

    public void setLoadType(String loadType) {
        this.loadType = loadType;
    }

    public String getDatasetType() {
        return datasetType;
    }

    public void setDatasetType(String datasetType) {
        this.datasetType = datasetType;
    }

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public Map<String, Object> getAspects() {
        return aspects;
    }

    public void setAspects(Map<String, Object> aspects) {
        this.aspects = aspects;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Object getMatrix() {
        return matrix;
    }

    public void setMatrix(Object matrix) {
        this.matrix = matrix;
    }
}
