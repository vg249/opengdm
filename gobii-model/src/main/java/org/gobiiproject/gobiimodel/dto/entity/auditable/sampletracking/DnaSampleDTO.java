package org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityParam;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by VCalaminos on 5/2/2019.
 */

@JsonIgnoreProperties(ignoreUnknown = true, value={
        "allowedProcessTypes", "entityNameType"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DnaSampleDTO extends DTOBaseAuditable{

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer projectId;
    private String sampleUuid;
    private String name;
    private String code;
    private String plateName;
    private String num;
    private String wellRow;
    private String wellCol;
    private Map<String, Object> properties = new HashMap<>();

    public DnaSampleDTO() { super(GobiiEntityNameType.DNASAMPLE); }

    @Override
    @GobiiEntityParam(paramName = "dnaSampleId")
    public Integer getId() { return this.id; }

    @Override
    @GobiiEntityColumn(columnName = "dnasample_id")
    public void setId(Integer id) { this.id = id; }

    @GobiiEntityParam(paramName = "dnaSampleName")
    public String getName() { return this.name; }

    @GobiiEntityColumn(columnName = "name")
    public void setName(String name) { this.name = name; }

    @GobiiEntityParam(paramName = "code")
    public String getCode() { return this.code; }

    @GobiiEntityColumn(columnName = "code")
    public void setCode(String code) { this.code = code; }

    @GobiiEntityParam(paramName = "plateName")
    public String getPlateName() { return this.plateName; }

    @GobiiEntityColumn(columnName = "platename")
    public void setPlateName(String plateName) { this.plateName = plateName; }

    @GobiiEntityParam(paramName = "num")
    public String getNum() { return this.num; }

    @GobiiEntityColumn(columnName = "num")
    public void setNum(String num) { this.num = num; }

    @GobiiEntityParam(paramName = "wellRow")
    public String getWellRow() { return this.wellRow; }

    @GobiiEntityColumn(columnName = "well_row")
    public void setWellRow(String wellRow) { this.wellRow = wellRow; }

    @GobiiEntityParam(paramName = "wellCol")
    public String getWellCol() { return this.wellCol; }

    @GobiiEntityColumn(columnName = "well_col")
    public void setWellCol(String wellCol) { this.wellCol = wellCol; }

    @GobiiEntityParam(paramName = "projectId")
    public Integer getProjectId() { return this.projectId; }

    @GobiiEntityColumn(columnName = "project_id")
    public void setProjectId(Integer projectId) { this.projectId = projectId; }

    @GobiiEntityParam(paramName = "sampleUuid")
    public String getSampleUuid() { return this.sampleUuid; }

    @GobiiEntityColumn(columnName = "sample_uuid")
    public void setSampleUuid(String sampleUuid) { this.sampleUuid = sampleUuid; }

    public Map<String, Object> getProperties() {
        return this.properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
