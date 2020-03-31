/**
 * ExperimentDTO.java
 * 
 * Experiment DTO Class
 * @author Rodolfo N. Duldulao, Jr.
 * @since 2020-03-28
 */

package org.gobiiproject.gobiimodel.dto.gdmv3;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityMap;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.entity.gdmv3.ExperimentV3;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = false, value={
    "id", "allowedProcessTypes", "entityNameType", "status"
})
public class ExperimentDTO extends DTOBaseAuditable {
    
    public ExperimentDTO() {
        super(GobiiEntityNameType.EXPERIMENT);
    }

    @Override
    public Integer getId() {
        return null;
    }

    @Override
    public void setId(Integer id) {

    }

    @GobiiEntityMap(paramName = "experimentId", entity = ExperimentV3.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer experimentId;

    @GobiiEntityMap(paramName = "experimentName", entity = ExperimentV3.class)
    private String experimentName;

    @GobiiEntityMap(paramName = "project.projectId",  entity = ExperimentV3.class, deep = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer projectId;

    @GobiiEntityMap(paramName = "project.projectName", entity = ExperimentV3.class, deep = true)
    private String projectName;

    @GobiiEntityMap(paramName = "vendorProtocol.vendorProtocolId", entity = ExperimentV3.class, deep = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer vendorProtocolId;
    
    @GobiiEntityMap(paramName = "vendorProtocol.name", entity = ExperimentV3.class, deep = true)
    private String vendorProtocolName;
    
    @GobiiEntityMap(paramName = "vendorProtocol.protocol.platform.platformId", entity = ExperimentV3.class, deep = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer platformId;

    @GobiiEntityMap(paramName = "vendorProtocol.protocol.platform.platformName", entity = ExperimentV3.class, deep = true)
    private String platformName;

    
}