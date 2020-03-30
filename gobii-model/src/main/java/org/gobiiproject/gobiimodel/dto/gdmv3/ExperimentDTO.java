/**
 * ExperimentDTO.java
 * 
 * Experiment DTO Class
 * @author Rodolfo N. Duldulao, Jr.
 * @since 2020-03-28
 */

 package org.gobiiproject.gobiimodel.dto.gdmv3;

import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import lombok.Data;

@Data
public class ExperimentDTO extends DTOBaseAuditable {

    private Integer experimentId;
    
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
    
}