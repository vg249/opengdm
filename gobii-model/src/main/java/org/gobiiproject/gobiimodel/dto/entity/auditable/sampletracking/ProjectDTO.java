
package org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking;


import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

public class ProjectDTO extends DTOBaseAuditable {

    private int projectId;
    private String genotypingPurpose;

    public ProjectDTO() {
        super(GobiiEntityNameType.PROJECT);
    }

    @Override
    public Integer getId() {
        return this.projectId;
    }

    @Override
    public void setId(Integer id) {
        this.projectId = id;
    }

    public String getGenotypingPurpose() {
        return this.genotypingPurpose;
    }

    public void setGenotypingPurpose(String genotypingPurpose) {
        this.genotypingPurpose = genotypingPurpose;
    }



}
