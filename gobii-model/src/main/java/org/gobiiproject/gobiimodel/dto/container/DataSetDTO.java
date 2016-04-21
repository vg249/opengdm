package org.gobiiproject.gobiimodel.dto.container;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.annotations.StoredProcParamVal;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Phil on 4/21/2016.
 */
public class DataSetDTO extends DtoMetaData {

    private int datasetId;
    private int experimentId;
    private int callinganalysisId;
    private String dataTable;
    private String dataFile;
    private String qualityTable;
    private String qualityFile;
    private String createdBy;
    private Date createdDate;
    private String modifiedBy;
    private Date modifiedDate;
    private Integer status;
    private List<Integer> analyses;
    private List<Integer> scores;

    @StoredProcParamVal(paramName = "datasetId")
    public int getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(int datasetId) {
        this.datasetId = datasetId;
    }

    @StoredProcParamVal(paramName = "experimentId")
    public int getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(int experimentId) {
        this.experimentId = experimentId;
    }

    @StoredProcParamVal(paramName = "callAnalysisId")
    public int getCallinganalysisId() {
        return callinganalysisId;
    }

    public void setCallinganalysisId(int callinganalysisId) {
        this.callinganalysisId = callinganalysisId;
    }

    @StoredProcParamVal(paramName = "dataTable")
    public String getDataTable() {
        return dataTable;
    }

    public void setDataTable(String dataTable) {
        this.dataTable = dataTable;
    }

    @StoredProcParamVal(paramName = "dataFile")
    public String getDataFile() {
        return dataFile;
    }

    public void setDataFile(String dataFile) {
        this.dataFile = dataFile;
    }

    @StoredProcParamVal(paramName = "qualityTable")
    public String getQualityTable() {
        return qualityTable;
    }

    public void setQualityTable(String qualityTable) {
        this.qualityTable = qualityTable;
    }

    @StoredProcParamVal(paramName = "qualityFile")
    public String getQualityFile() {
        return qualityFile;
    }

    public void setQualityFile(String qualityFile) {
        this.qualityFile = qualityFile;
    }

    @StoredProcParamVal(paramName = "createdBy")
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @StoredProcParamVal(paramName = "createdDate")
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @StoredProcParamVal(paramName = "modifiedBy")
    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @StoredProcParamVal(paramName = "modifiedDate")
    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @StoredProcParamVal(paramName = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Integer> getAnalyses() {
        return analyses;
    }

    public void setAnalyses(List<Integer> analyses) {
        this.analyses = analyses;
    }

    public List<Integer> getScores() {
        return scores;
    }

    public void setScores(List<Integer> scores) {
        this.scores = scores;
    }
}
