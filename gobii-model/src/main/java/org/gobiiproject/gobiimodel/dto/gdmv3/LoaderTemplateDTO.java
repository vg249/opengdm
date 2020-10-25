package org.gobiiproject.gobiimodel.dto.gdmv3;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityMap;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.entity.LoaderTemplate;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.utils.customserializers.UtcDateSerializer;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = false, value={
    "id", "allowedProcessTypes", "entityNameType", "status"
})
public class LoaderTemplateDTO {

    @JsonSerialize(using = ToStringSerializer.class)
    @GobiiEntityMap(paramName = "templateId", entity = LoaderTemplate.class)
    private Integer templateId;

    @GobiiEntityMap(paramName = "templateName", entity = LoaderTemplate.class)
    private String templateName;

    @GobiiEntityMap(paramName = "templateType.term", entity = LoaderTemplate.class, deep = true)
    private String templateType;

    @GobiiEntityMap(paramName = "template", entity = LoaderTemplate.class)
    private JsonNode template;

    @GobiiEntityMap(paramName="createdBy.contactId", entity = LoaderTemplate.class, deep = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer createdBy = null;

    @JsonSerialize(using= UtcDateSerializer.class)
    @GobiiEntityMap(paramName="createdDate", base=true)
    private Date createdDate = null;

    @GobiiEntityMap(paramName="modifiedBy.contactId", entity = LoaderTemplate.class, deep = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer modifiedBy = null;

    @JsonSerialize(using=UtcDateSerializer.class)
    @GobiiEntityMap(paramName="modifiedDate", base=true)
    private Date modifiedDate = null;


    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public JsonNode getTemplate() {
        return template;
    }

    public void setTemplate(JsonNode template) {
        this.template = template;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
