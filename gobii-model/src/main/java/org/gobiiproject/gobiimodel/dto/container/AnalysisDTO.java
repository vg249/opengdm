package org.gobiiproject.gobiimodel.dto.container;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;

import java.util.Date;
import java.util.List;

/**
 * Created by Phil on 4/21/2016.
 */
public class AnalysisDTO extends DtoMetaData {

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

    @GobiiEntityParam(paramName="analysisId")
    public int getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(int analysisId) {
        this.analysisId = analysisId;
    }

    @GobiiEntityParam(paramName="analysisName")
    public String getAnalysisName() {
        return analysisName;
    }

    public void setAnalysisName(String analysisName) {
        this.analysisName = analysisName;
    }

    @GobiiEntityParam(paramName="analysisDescription")
    public String getAnalysisDescription() {
        return analysisDescription;
    }

    public void setAnalysisDescription(String analysisDescription) {
        this.analysisDescription = analysisDescription;
    }

    @GobiiEntityParam(paramName="analysisTypeId")
    public int getAnlaysisTypeId() {
        return anlaysisTypeId;
    }

    public void setAnlaysisTypeId(int anlaysisTypeId) {
        this.anlaysisTypeId = anlaysisTypeId;
    }

    @GobiiEntityParam(paramName="program")
    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    @GobiiEntityParam(paramName="programVersion")
    public String getProgramVersion() {
        return programVersion;
    }

    public void setProgramVersion(String programVersion) {
        this.programVersion = programVersion;
    }

    @GobiiEntityParam(paramName="algorithm")
    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    @GobiiEntityParam(paramName="sourceName")
    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    @GobiiEntityParam(paramName="sourceVersion")
    public String getSourceVersion() {
        return sourceVersion;
    }

    public void setSourceVersion(String sourceVersion) {
        this.sourceVersion = sourceVersion;
    }

    @GobiiEntityParam(paramName="sourceUri")
    public String getSourceUri() {
        return sourceUri;
    }

    public void setSourceUri(String sourceUri) {
        this.sourceUri = sourceUri;
    }

    @GobiiEntityParam(paramName="referenceId")
    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    @GobiiEntityParam(paramName="timeExecuted")
    public Date getTimeExecuted() {
        return timeExecuted;
    }

    public void setTimeExecuted(Date timeExecuted) {
        this.timeExecuted = timeExecuted;
    }

    @GobiiEntityParam(paramName="status")
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
