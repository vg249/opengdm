package org.gobiiproject.gobiimodel.dto.entity.auditable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityMap;
import org.gobiiproject.gobiimodel.entity.*;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import java.util.List;
import java.util.Map;

/**
 * Model for the Brapi Samples endpoint
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    private String fileFormat;

    private String dataFormat;

    private List<Object> availableFormats;

    @GobiiEntityMap(paramName="callingAnalysis.type.term", entity = Dataset.class, deep=true)
    @JsonSerialize(using = ToStringSerializer.class)
    private String type;

    @GobiiEntityMap(paramName="callingAnalysis.description", entity = Dataset.class, deep=true)
    @JsonSerialize(using = ToStringSerializer.class)
    private String description;

    public Integer getId() {
        return 0;
    }

    public void setId(Integer i) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
