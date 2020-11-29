package org.gobiiproject.gobiimodel.dto.gdmv3;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class DnaRunUploadRequestDTO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Integer projectId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Integer experimentId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Integer dnaRunTemplateId;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(Integer experimentId) {
        this.experimentId = experimentId;
    }

    public Integer getDnaRunTemplateId() {
        return dnaRunTemplateId;
    }

    public void setDnaRunTemplateId(Integer dnaRunTemplateId) {
        this.dnaRunTemplateId = dnaRunTemplateId;
    }
}
