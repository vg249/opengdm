package org.gobiiproject.gobiimodel.dto.gdmv3;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityMap;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.entity.LoaderTemplate;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

@JsonIgnoreProperties(ignoreUnknown = false, value={
    "id", "allowedProcessTypes", "entityNameType", "status"
})
public class LoaderTemplateDTO extends DTOBaseAuditable {

    public LoaderTemplateDTO() {
        super(GobiiEntityNameType.LOADERTEMPLATE);
    }

    @Override
    public Integer getId() {
        return this.templateId;
    }

    @Override
    public void setId(Integer id) {
        this.templateId = id;

    }

    @JsonSerialize(using = ToStringSerializer.class)
    @GobiiEntityMap(paramName = "templateId", entity = LoaderTemplate.class)
    private Integer templateId;

    @GobiiEntityMap(paramName = "templateType.term", entity = LoaderTemplate.class, deep = true)
    private String templateType;

    @GobiiEntityMap(paramName = "templateType.term", entity = LoaderTemplate.class, deep = true)
    private JsonNode template;
}
