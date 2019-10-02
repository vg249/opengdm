package org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityMap;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityParam;
import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

/**
 * Created by VCalaminos on 5/1/2019.
 */

@JsonIgnoreProperties(ignoreUnknown = true, value={
        "id", "allowedProcessTypes", "entityNameType",
        "dataFilePath", "dataFileUrl"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExperimentDTO extends DTOBaseAuditable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GobiiEntityMap(paramName="experimentId", entity = Experiment.class)
    private int experimentId;

    @GobiiEntityMap(paramName="experimentName", entity= Experiment.class)
    private String experimentName;

    @GobiiEntityMap(paramName="experimentCode", entity= Experiment.class)
    private String experimentCode;

    @GobiiEntityMap(paramName="dataFile", entity= Experiment.class)
    private String dataFilePath;

    @GobiiEntityMap(paramName="projectId", entity= Experiment.class)
    private Integer projectId;

    @GobiiEntityMap(paramName="vendorProtocolId", entity= Experiment.class)
    private Integer vendorProtocolId;

    @GobiiEntityMap(paramName="manifestId", entity= Experiment.class)
    private Integer manifestId;

    @GobiiEntityMap(paramName="experimentStatus", entity= Experiment.class)
    private Integer experimentStatus;

    private String dataFileUrl;

    public ExperimentDTO() { super(GobiiEntityNameType.EXPERIMENT); }

    @Override
    public Integer getId() { return this.experimentId; }

    @Override
    public void setId(Integer id) { this.experimentId = id; }

    public Integer getExperimentId() { return this.experimentId; }

    public void setExperimentId(Integer id) { this.experimentId = id; }

    public String getName() { return this.experimentName; }

    public void setName(String name) { this.experimentName = name; }

    public String getCode() { return this.experimentCode; }

    public void setCode(String code) { this.experimentCode = code; }

    public String getDataFilePath() { return this.dataFilePath; }

    public void setDataFilePath(String dataFile) { this.dataFilePath = dataFile; }

    public String getDataFileUrl() { return this.dataFileUrl; }

    public void setDataFileUrl(String dataFileUrl) { this.dataFileUrl = dataFileUrl; }

    public Integer getProjectId() { return this.projectId; }

    public void setProjectId(Integer projectId) { this.projectId = projectId; }

    public Integer getVendorProtocolId() { return this.vendorProtocolId; }

    public void setVendorProtocolId(Integer vendorProtocolId) { this.vendorProtocolId = vendorProtocolId; }

    public Integer getManifestId() { return this.manifestId; }

    public void setManifestId(Integer manifestId) { this.manifestId = manifestId; }

    public Integer getStatus() { return this.experimentStatus; }

    public void setStatus(Integer status) { this.experimentStatus = status; }

}
