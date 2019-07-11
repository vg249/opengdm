package org.gobiiproject.gobiimodel.dto.entity.auditable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityParam;

import java.util.Date;

/**
 * Created by VCalaminos on 7/11/2019.
 */

@JsonIgnoreProperties(ignoreUnknown = true, value={
        "id", "allowedProcessTypes", "entityNameType"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnalysisBrapiDTO extends DTOBase {

    private int analysisDbId;
    private String analysisName;
    private Date created;
    private String type;
    private String description;
    private String software;

    @Override
    public Integer getId() { return this.analysisDbId; }

    @Override
    public void setId(Integer analysisDbId) { this.analysisDbId = analysisDbId; }

    @GobiiEntityParam(paramName = "analysisDbId")
    public Integer getAnalysisDbId() { return this.analysisDbId; }

    @GobiiEntityColumn(columnName = "analysis_id")
    public void setAnalysisDbId(Integer analysisDbId) { this.analysisDbId = analysisDbId; }

    @GobiiEntityParam(paramName = "analysisName")
    public String getAnalysisName() { return this.analysisName; }

    @GobiiEntityColumn(columnName = "analysis_name")
    public void setAnalysisName(String analysisName) { this.analysisName = analysisName; }

    @GobiiEntityParam(paramName = "created")
    public Date getCreated() { return this.created; }

    @GobiiEntityColumn(columnName = "created")
    public void setCreated(Date created) { this.created = created; }

    @GobiiEntityParam(paramName = "type")
    public String getType() { return this.type; }

    @GobiiEntityColumn(columnName = "type")
    public void setType(String type) { this.type = type; }

    @GobiiEntityParam(paramName = "description")
    public String getDescription() { return this.description; }

    @GobiiEntityColumn(columnName = "description")
    public void setDescription(String description) { this.description = description; }

    @GobiiEntityParam(paramName = "software")
    public String getSoftware() { return this.software; }

    @GobiiEntityColumn(columnName = "software")
    public void setSoftware(String software) { this.software = software; }

}
