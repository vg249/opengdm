package org.gobiiproject.gobiimodel.dto.container;

import org.gobiiproject.gobiimodel.dto.annotations.StoredProcParamVal;

import java.util.Date;
import java.util.List;

/**
 * Created by Phil on 4/21/2016.
 */
public class AnalysisDTO {

    private int analysisId;
    private String analysisName;
    private String analysisDescription;
    private int anlaysisTypeId;
    private String program;
    private String programVersion;
    private String algorithm;
    private String sourceName;
    private String sourceVersion;
    private String sourceUri;
    private Integer referenceId;
    private Date timeExecuted;
    private int status;
    private List<EntityPropertyDTO> parameters;

    @StoredProcParamVal(paramName="analysisId")
    public int getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(int analysisId) {
        this.analysisId = analysisId;
    }

    @StoredProcParamVal(paramName="analysisName")
    public String getAnalysisName() {
        return analysisName;
    }

    public void setAnalysisName(String analysisName) {
        this.analysisName = analysisName;
    }

    @StoredProcParamVal(paramName="analysisDescription")
    public String getAnalysisDescription() {
        return analysisDescription;
    }

    public void setAnalysisDescription(String analysisDescription) {
        this.analysisDescription = analysisDescription;
    }

    @StoredProcParamVal(paramName="analysisTypeId")
    public int getAnlaysisTypeId() {
        return anlaysisTypeId;
    }

    public void setAnlaysisTypeId(int anlaysisTypeId) {
        this.anlaysisTypeId = anlaysisTypeId;
    }

    @StoredProcParamVal(paramName="program")
    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    @StoredProcParamVal(paramName="programVersion")
    public String getProgramVersion() {
        return programVersion;
    }

    public void setProgramVersion(String programVersion) {
        this.programVersion = programVersion;
    }

    @StoredProcParamVal(paramName="algorithm")
    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    @StoredProcParamVal(paramName="sourceName")
    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    @StoredProcParamVal(paramName="sourceVersion")
    public String getSourceVersion() {
        return sourceVersion;
    }

    public void setSourceVersion(String sourceVersion) {
        this.sourceVersion = sourceVersion;
    }

    @StoredProcParamVal(paramName="sourceUri")
    public String getSourceUri() {
        return sourceUri;
    }

    public void setSourceUri(String sourceUri) {
        this.sourceUri = sourceUri;
    }

    @StoredProcParamVal(paramName="referenceId")
    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    @StoredProcParamVal(paramName="timeExecuted")
    public Date getTimeExecuted() {
        return timeExecuted;
    }

    public void setTimeExecuted(Date timeExecuted) {
        this.timeExecuted = timeExecuted;
    }

    @StoredProcParamVal(paramName="status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<EntityPropertyDTO> getParameters() {
        return parameters;
    }

    public void setParameters(List<EntityPropertyDTO> parameters) {
        this.parameters = parameters;
    }
}
