package org.gobiiproject.gobiimodel.dto.entity.flex;

import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiEntitySubType;
import org.gobiiproject.gobiimodel.types.GobiiVertexType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 4/8/2016.
 */
public class VertexDTO extends DTOBase{


    public VertexDTO() {}


    public VertexDTO(Integer vertexId,
                     GobiiVertexType gobiiVertexType,
                     String vertexName,
                     GobiiEntityNameType entityType,
                     GobiiEntitySubType entitySubType,
                     CvGroup cvGroup,
                     String cvTerm) {
        this.vertexId = vertexId;
        this.gobiiVertexType = gobiiVertexType;
        this.vertexName = vertexName;
        this.entityType = entityType;
        this.entitySubType = entitySubType;
        this.cvGroup = cvGroup;
        this.cvTerm = cvTerm;
    }

    @Override
    public Integer getId() {
        return this.getVertexId();
    }

    @Override
    public void setId(Integer id) {
        this.vertexId = id;
    }


    private Integer vertexId = 0;
    private GobiiVertexType gobiiVertexType  = GobiiVertexType.UNKNOWN;
    private String vertexName;
    private List<Integer> filterVals = new ArrayList<>();
    private GobiiEntityNameType entityType;
    private GobiiEntitySubType entitySubType;
    private CvGroup cvGroup;
    private String cvTerm;


    public Integer getVertexId() {
        return vertexId;
    }

    public void setVertexId(Integer vertexId) {
        this.vertexId = vertexId;
    }

    public GobiiVertexType getGobiiVertexType() {
        return gobiiVertexType;
    }

    public void setGobiiVertexType(GobiiVertexType gobiiVertexType) {
        this.gobiiVertexType = gobiiVertexType;
    }

    public String getVertexName() {
        return vertexName;
    }

    public void setVertexName(String vertexName) {
        this.vertexName = vertexName;
    }

    public List<Integer> getFilterVals() {
        return filterVals;
    }

    public void setFilterVals(List<Integer> filterVals) {
        this.filterVals = filterVals;
    }

    public GobiiEntityNameType getEntityType() {
        return entityType;
    }

    public void setEntityType(GobiiEntityNameType entityType) {
        this.entityType = entityType;
    }

    public GobiiEntitySubType getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(GobiiEntitySubType entitySubType) {
        this.entitySubType = entitySubType;
    }

    public CvGroup getCvGroup() {
        return cvGroup;
    }

    public void setCvGroup(CvGroup cvGroup) {
        this.cvGroup = cvGroup;
    }

    public String getCvTerm() {
        return cvTerm;
    }

    public void setCvTerm(String cvTerm) {
        this.cvTerm = cvTerm;
    }
}
