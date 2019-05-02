package org.gobiiproject.gobiimodel.dto.entity.noaudit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.DnaSampleDTO;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectSamplesDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Integer projectId;

    public List<DnaSampleDTO> samples;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public List<DnaSampleDTO> getSamples() {
        return this.samples;
    }

    public void setSamples(List<DnaSampleDTO> sampleList) {
        this.samples = sampleList;
    }
}
