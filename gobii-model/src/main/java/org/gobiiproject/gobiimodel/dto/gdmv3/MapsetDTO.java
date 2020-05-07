/**
 * MapsetDTO.java
 * 
 * DTO class for GDM v3 API.
 * 
 * @author Rodolfo N. Duldulao, Jr.
 */
package org.gobiiproject.gobiimodel.dto.gdmv3;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityMap;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.entity.Mapset;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({"id", "allowedProcessTypes", "entityNameType"})
public class MapsetDTO extends DTOBaseAuditable {

    public MapsetDTO(){
        super(GobiiEntityNameType.MAPSET);
    }

    @Override
    public Integer getId() {
        return mapsetId;
    }

    @Override
    public void setId(Integer id) {
    }

    @GobiiEntityMap(paramName = "mapsetId", entity = Mapset.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer mapsetId;

    @GobiiEntityMap(paramName = "mapsetName", entity = Mapset.class)
    private String mapsetName;

    @GobiiEntityMap(paramName = "mapSetDescription", entity = Mapset.class)
    private String mapsetDescription;

    @GobiiEntityMap(paramName = "type.cvId", entity = Mapset.class, deep = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer mapsetTypeId;

    @GobiiEntityMap(paramName = "type.term", entity = Mapset.class, deep = true)
    private String mapsetTypeName;

    @GobiiEntityMap(paramName = "reference.referenceId", entity = Mapset.class, deep = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer referenceId;

    @GobiiEntityMap(paramName = "reference.name", entity = Mapset.class, deep = true)
    private String referenceName; 


    
    
}