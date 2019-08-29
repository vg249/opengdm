package org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityParam;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

/**
 * Created by VCalaminos on 5/1/2019.
 */

@JsonIgnoreProperties(ignoreUnknown = true, value={
        "id", "allowedProcessTypes", "entityNameType",
        "dataFilePath"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExperimentDTO extends DTOBaseAuditable {

    private int id;
    private String name;
    private String code;
    private String dataFilePath;
    private Integer projectId;
    private Integer vendorProtocolId;
    private Integer manifestId;
    private Integer status;
    private String dataFileUrl;

    public ExperimentDTO() { super(GobiiEntityNameType.EXPERIMENT); }

    @Override
    public Integer getId() { return this.id; }

    @Override
    public void setId(Integer id) { this.id = id; }

    @GobiiEntityParam(paramName = "experimentId")
    public Integer getExperimentId() { return this.id; }

    @GobiiEntityColumn(columnName = "experiment_id")
    public void setExperimentId(Integer id) { this.id = id; }

    @GobiiEntityParam(paramName = "experimentName")
    public String getName() { return this.name; }

    @GobiiEntityColumn(columnName = "name")
    public void setName(String name) { this.name = name; }

    @GobiiEntityParam(paramName = "experimentCode")
    public String getCode() { return this.code; }

    @GobiiEntityColumn(columnName = "code")
    public void setCode(String code) { this.code = code; }

    @GobiiEntityParam(paramName = "experimentDataFile")
    public String getDataFilePath() { return this.dataFilePath; }

    @GobiiEntityColumn(columnName = "data_file")
    public void setDataFilePath(String dataFile) { this.dataFilePath = dataFile; }

    public String getDataFileUrl() { return this.dataFilePath; }

    public void setDataFileUrl(String dataFile) { this.dataFilePath = dataFile; }

    @GobiiEntityParam(paramName = "projectId")
    public Integer getProjectId() { return this.projectId; }

    @GobiiEntityColumn(columnName = "project_id")
    public void setProjectId(Integer projectId) { this.projectId = projectId; }

    @GobiiEntityParam(paramName = "vendorProtocolId")
    public Integer getVendorProtocolId() { return this.vendorProtocolId; }

    @GobiiEntityColumn(columnName = "vendor_protocol_id")
    public void setVendorProtocolId(Integer vendorProtocolId) { this.vendorProtocolId = vendorProtocolId; }

    @GobiiEntityParam(paramName = "manifestId")
    public Integer getManifestId() { return this.manifestId; }

    @GobiiEntityColumn(columnName = "manifest_id")
    public void setManifestId(Integer manifestId) { this.manifestId = manifestId; }

    @GobiiEntityParam(paramName = "status")
    public Integer getStatus() { return this.status; }

    @GobiiEntityColumn(columnName = "status")
    public void setStatus(Integer status) { this.status = status; }

}
