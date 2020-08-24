
/**
 * ContactDTO.java
 * 
 * GDM V3 Version
 * 
 * @author Rodolfo N. Duldulao, Jr.
 * @since 2020-03-26
 */
package org.gobiiproject.gobiimodel.dto.gdmv3;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityMap;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonIgnoreProperties(ignoreUnknown = false, value={
    "id", "allowedProcessTypes", "entityNameType", "status"
})
@JsonInclude(JsonInclude.Include.ALWAYS)
@Data
@EqualsAndHashCode(callSuper=false)
public class ContactDTO extends DTOBaseAuditable {

    public ContactDTO() {
        super(GobiiEntityNameType.CONTACT);
    }

    @Override
    public Integer getId() {
        return this.piContactId;
    }

    @Override
    public void setId(Integer id) {
        this.piContactId = id;

    }

    @GobiiEntityMap(paramName = "contactId", entity = Contact.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer piContactId;

    @GobiiEntityMap(paramName = "lastName", entity = Contact.class)
    private String piContactLastName;

    @GobiiEntityMap(paramName = "firstName", entity = Contact.class)
    private String piContactFirstName;

    @GobiiEntityMap(paramName = "organization.organizationId", entity = Contact.class, deep=true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer organizationId;

    @GobiiEntityMap(paramName = "organization.name", entity = Contact.class, deep = true)
    private String organizationName;


}