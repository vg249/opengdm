package org.gobiiproject.gobiimodel.dto.instructions.loader;

import com.google.gson.internal.$Gson$Preconditions;
import org.gobiiproject.gobiimodel.config.GobiiCropConfig;

import java.io.File;
import java.util.List;
import java.util.Map;

public class DigesterResult {

    private boolean success;

    private boolean sendQc;

    private String cropType;

    private GobiiCropConfig cropConfig;

    private String intermediateFilePath;

    private String loadType;

    private Map<String, File> loaderInstructionsMap;

    private List<String> loaderInstructionsList;

    private String datasetType;

    private Object jobStatusObject;

    private Integer datasetId;

    private String jobName;

    private String contactEmail;

    public DigesterResult(boolean success,
                          boolean sendQc,
                          String cropType,
                          GobiiCropConfig cropConfig,
                          String intermediateFilePath,
                          String loadType,
                          Map<String, File> loaderInstructionsMap,
                          List<String> loaderInstructionsList,
                          String datasetType,
                          Object jobStatusObject,
                          Integer datasetId,
                          String jobName) {

        this.success = success;
        this.sendQc = sendQc;
        this.cropType = cropType;
        this.cropConfig = cropConfig;
        this.intermediateFilePath = intermediateFilePath;
        this.loadType = loadType;
        this.loaderInstructionsMap = loaderInstructionsMap;
        this.loaderInstructionsList = loaderInstructionsList;
        this.datasetType = datasetType;
        this.jobStatusObject = jobStatusObject;
        this.datasetId = datasetId;
        this.jobName = jobName;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSendQc() {
        return sendQc;
    }

    public void setSendQc(boolean sendQc) {
        this.sendQc = sendQc;
    }

    public String getIntermediateFilePath() {
        return intermediateFilePath;
    }

    public void setIntermediateFilePath(String intermediateFilePath) {
        this.intermediateFilePath = intermediateFilePath;
    }

    public String getCropType() {
        return cropType;
    }

    public void setCropType(String cropType) {
        this.cropType = cropType;
    }

    public GobiiCropConfig getCropConfig() {
        return cropConfig;
    }

    public void setCropConfig(GobiiCropConfig cropConfig) {
        this.cropConfig = cropConfig;
    }

    public String getLoadType() {
        return loadType;
    }

    public void setLoadType(String loadType) {
        this.loadType = loadType;
    }

    public Map<String, File> getLoaderInstructionsMap() {
        return loaderInstructionsMap;
    }

    public void setLoaderInstructionsMap(Map<String, File> loaderInstructionsMap) {
        this.loaderInstructionsMap = loaderInstructionsMap;
    }

    public List<String> getLoaderInstructionsList() {
        return loaderInstructionsList;
    }

    public void setLoaderInstructionsList(List<String> loaderInstructionsList) {
        this.loaderInstructionsList = loaderInstructionsList;
    }

    public boolean isJobStatusSet() {
        if(jobStatusObject != null) {
            return true;
        }
        return false;
    }


    public String getDatasetType() {
        return datasetType;
    }

    public void setDatasetType(String datasetType) {
        this.datasetType = datasetType;
    }

    public Object getJobStatusObject() {
        return jobStatusObject;
    }

    public void setJobStatusObject(Object jobStatusObject) {
        this.jobStatusObject = jobStatusObject;
    }

    public boolean hasGenotypeMatrix() {
        if(datasetId  != null) {
            return true;
        }
        return false;
    }

    public Integer getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(Integer datasetId) {
        this.datasetId = datasetId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}
