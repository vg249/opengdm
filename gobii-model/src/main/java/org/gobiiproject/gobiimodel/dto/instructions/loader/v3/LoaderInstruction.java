package org.gobiiproject.gobiimodel.dto.instructions.loader.v3;

import java.util.Map;

public class LoaderInstruction {

    private String cropType;
    private String loadType;
    private String datasetType;
    private String inputFile;
    private String outputDir;

    private Map<String, Table> aspects;

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

    public Map<String, Table> getAspects() {
        return aspects;
    }

    public void setAspects(Map<String, Table> aspects) {
        this.aspects = aspects;
    }
}
