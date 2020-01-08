package org.gobiiproject.gobiimodel.dto.entity.auditable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityMap;
import org.gobiiproject.gobiimodel.entity.*;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Model for the Brapi Samples endpoint
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true, value={
        "id", "allowedProcessTypes", "entityNameType", "createdDate", "modifiedDate"
})
public class VariantSetDTO extends DTOBaseAuditable {

    public VariantSetDTO() {
        super(GobiiEntityNameType.DATASET);
    }

    @GobiiEntityMap(paramName="datasetId", entity = Dataset.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer variantSetDbId;

    @GobiiEntityMap(paramName="datasetName", entity = Dataset.class)
    private String variantSetName;

    @GobiiEntityMap(paramName="experiment.experimentId", entity = Dataset.class, deep=true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer studyDbId;

    @GobiiEntityMap(paramName="callingAnalysis.reference.referenceId", entity = Dataset.class, deep=true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer referenceSetDbId;

    private String fileUrl;

    private String fileFormat = "text/tab-seperated-values";

    private String dataFormat = "tabular";

    private List<Object> availableFormats;

    private Set<AnalysisBrapiDTO> analyses;

    public Integer getId() {
        return 0;
    }

    public void setId(Integer i) {
    }

    public Date getUpdated() {
        return this.getModifiedDate();
    }

    public Date getCreated() {
        return this.getCreatedDate();
    }

    public Integer getVariantSetDbId() {
        return variantSetDbId;
    }

    public void setVariantSetDbId(Integer variantSetDbId) {
        this.variantSetDbId = variantSetDbId;
    }

    public String getVariantSetName() {
        return variantSetName;
    }

    public void setVariantSetName(String variantSetName) {
        this.variantSetName = variantSetName;
    }

    public Integer getStudyDbId() {
        return studyDbId;
    }

    public void setStudyDbId(Integer studyDbId) {
        this.studyDbId = studyDbId;
    }

    public Integer getReferenceSetDbId() {
        return referenceSetDbId;
    }

    public void setReferenceSetDbId(Integer referenceSetDbId) {
        this.referenceSetDbId = referenceSetDbId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }

    public List<Object> getAvailableFormats() {
        return availableFormats;
    }

    public void setAvailableFormats(List<Object> availableFormats) {
        this.availableFormats = availableFormats;
    }

    public Set<AnalysisBrapiDTO> getAnalyses() {
        return analyses;
    }

    public void setAnalyses(Set<AnalysisBrapiDTO> analyses) {
        this.analyses = analyses;
    }

    public Date created() {
        return this.getCreatedDate();
    }
}
