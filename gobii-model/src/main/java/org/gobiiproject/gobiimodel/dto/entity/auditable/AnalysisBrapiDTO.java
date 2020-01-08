package org.gobiiproject.gobiimodel.dto.entity.auditable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityMap;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityParam;
import org.gobiiproject.gobiimodel.entity.Analysis;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by VCalaminos on 7/11/2019.
 */

@JsonIgnoreProperties(ignoreUnknown = true, value={
        "id", "allowedProcessTypes", "entityNameType", "createdBy", "modifiedBy", "modifiedDate"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnalysisBrapiDTO extends DTOBaseAuditable {


    @GobiiEntityMap(paramName="analysisId", entity = Analysis.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer analysisDbId;

    @GobiiEntityMap(paramName="analysisName", entity = Analysis.class)
    private String analysisName;

    @GobiiEntityMap(paramName="type.term", entity = Analysis.class, deep = true)
    private String type;

    @GobiiEntityMap(paramName="description", entity = Analysis.class)
    private String description;

    @GobiiEntityMap(paramName="software", entity = Analysis.class)
    private String software;

    public AnalysisBrapiDTO() {
        super(GobiiEntityNameType.ANALYSIS);
    }

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
